package org.hust.job.stream;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.*;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.hust.job.ArgsOptional;
import org.hust.job.IJobBuilder;
import org.hust.model.event.Event;
import org.hust.utils.KafkaUtils;
import org.hust.utils.SparkUtils;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.*;

public class CollectEventBatch implements IJobBuilder {
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

    @Override
    public void run(ArgsOptional args) {
        loadAgrs(args);
        init();

        Encoder<Event> eventEncoder = Encoders.bean(Event.class);

        stream.foreachRDD((consumerRecordJavaRDD, time) -> {
            JavaRDD<Event> rows = consumerRecordJavaRDD
                    .map(consumerRecord -> RowFactory.create(consumerRecord.value(), consumerRecord.topic()))
                    .map(CollectEventBatch::transformRow)
                    .filter(Objects::nonNull);

            Dataset<Event> ds = spark.createDataset(rows.rdd(), eventEncoder);
            ds.select("user_id", "geo_city", "contexts", "unstruct_event").show();


            String dateTime = dateTimeFormat.format(new DateTime(time.milliseconds()).plusHours(7).toDate());
            String path = "hdfs://172.19.0.20:9000/data/event/" + dateTime;
            ds.coalesce(1)
                    .write()
                    .parquet(path);

            System.out.println("write to: " + path);
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
