package org.hust.job.impl.stream;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.*;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.hust.job.ArgsOptional;
import org.hust.job.IJobBuilder;
import org.hust.loader.IRecord;
import org.hust.loader.kafka.elasticsearch.InsertDocument;
import org.hust.model.event.Event;
import org.hust.model.event.EventType;
import org.hust.utils.KafkaUtils;
import org.hust.utils.SparkUtils;

import java.text.SimpleDateFormat;
import java.util.*;

public class CollectEventStream implements IJobBuilder {
    private SparkUtils sparkUtils;
    private SparkSession spark;
    private JavaInputDStream<ConsumerRecord<Object, Object>> stream;
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy_MM_dd/HH_mm_ss");
    private ArgsOptional args;
    private Set<String> topicList;

    public void loadAgrs(ArgsOptional args) {
        this.args = args;
        this.topicList = new HashSet<>(Arrays.asList(args.getTopics().split(",")));
    }

    public void init() {
        sparkUtils = new SparkUtils("collect event", "yarn", args.getDuration());
        spark = sparkUtils.getSparkSession();

        KafkaUtils kafkaUtils = new KafkaUtils(args.getGroupId(), topicList);

        stream = org.apache.spark.streaming.kafka010.KafkaUtils
                .createDirectStream(sparkUtils.getJavaStreamingContext(),
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.Subscribe(kafkaUtils.getTopics(), kafkaUtils.getKafkaParams())
                );
    }

    public static Event transformRow(Row row) {
        String value = row.getAs(0);
        System.out.println(value);
        return new Event(value);
    }

    public void insertIntoEs(Dataset<Event> ds) {
        ds.foreachPartition(t -> {
            while (t.hasNext()) {
                Event event = t.next();

                switch (event.getEvent()) {
                    case EventType.UNSTRUCT: {
                        IRecord iRecord = IRecord.createRecord(event);
                        try {
                            InsertDocument.insertDocument(iRecord);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        });
    }

    @Override
    public void run(ArgsOptional args) {
        loadAgrs(args);
        init();

        String path = "";

        Encoder<Event> eventEncoder = Encoders.bean(Event.class);

        stream.foreachRDD((consumerRecordJavaRDD, time) -> {
            JavaRDD<Event> rows = consumerRecordJavaRDD
                    .map(consumerRecord -> RowFactory.create(consumerRecord.value(), consumerRecord.topic()))
                    .map(CollectEventStream::transformRow)
                    .filter(Objects::nonNull);

            Dataset<Event> ds = spark.createDataset(rows.rdd(), eventEncoder);
            ds.select("user_id", "contexts", "unstruct_event").show();

            insertIntoEs(ds);
//            df.show();
//            df.coalesce(1)
//                    .write()
//                    .parquet("path");
        });

        // start
        sparkUtils.getJavaStreamingContext().start();

        // await
        try {
            sparkUtils.getJavaStreamingContext().awaitTermination();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
