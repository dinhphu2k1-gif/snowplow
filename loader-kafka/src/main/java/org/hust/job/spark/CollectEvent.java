package org.hust.job.spark;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.hust.model.event.Event;
import org.hust.utils.KafkaUtils;
import org.hust.utils.SparkUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CollectEvent {
    private SparkUtils sparkUtils;
    private SparkSession spark;
    private JavaInputDStream<ConsumerRecord<Object, Object>> stream;
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy_MM_dd/HH_mm_ss");
    private Set<String> topicList;
    private int duration;
    private String groupId;

    public CollectEvent() {
        duration = 10;
        groupId = "abc";
        topicList = new HashSet<>(Arrays.asList("enriched"));

        sparkUtils = new SparkUtils("collect event", "local", duration);
        spark = sparkUtils.getSparkSession();

        KafkaUtils kafkaUtils = new KafkaUtils(groupId, topicList);

        stream = org.apache.spark.streaming.kafka010.KafkaUtils
                .createDirectStream(sparkUtils.getJavaStreamingContext(),
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.Subscribe(kafkaUtils.getTopics(), kafkaUtils.getKafkaParams())
                );
    }

    public static Event transformRow(Row row) {
        String value = row.getAs(0);
        return new Event(value);
    }

    public void run() {
        stream.foreachRDD((consumerRecordJavaRDD, time) -> {
            JavaRDD<Event> rows = consumerRecordJavaRDD
                    .map(consumerRecord -> RowFactory.create(consumerRecord.value(), consumerRecord.topic()))
                    .map(CollectEvent::transformRow)
                    .filter(Objects::nonNull);

            Dataset<Row> df = spark.createDataFrame(rows, Event.class);
            df.select("contexts", "unstruct_event").show();

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

    public static void main(String[] args) {
        CollectEvent collectEvent = new CollectEvent();
        collectEvent.run();
    }
}
