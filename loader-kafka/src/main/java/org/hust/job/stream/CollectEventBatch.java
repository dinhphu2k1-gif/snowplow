package org.hust.job.stream;

import com.maxmind.geoip2.model.CityResponse;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.*;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.hust.job.ArgsOptional;
import org.hust.job.IJobBuilder;
import org.hust.model.event.Event;
import org.hust.utils.KafkaUtils;
import org.hust.utils.SparkUtils;
import org.hust.utils.maxmind.MaxMindWrapper;
import org.joda.time.DateTime;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.spark.sql.functions.call_udf;
import static org.apache.spark.sql.functions.col;

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
        sparkUtils = new SparkUtils("collect event to log", "yarn", args.getDuration());
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
        MaxMindWrapper maxMindWrapper = new MaxMindWrapper();
        Broadcast<MaxMindWrapper> readerBroadcast = sparkUtils.getJavaSparkContext().broadcast(maxMindWrapper);
        System.out.println(maxMindWrapper.getReader());

        stream.foreachRDD((consumerRecordJavaRDD, time) -> {
            JavaRDD<Event> rows = consumerRecordJavaRDD
                    .map(consumerRecord -> RowFactory.create(consumerRecord.value(), consumerRecord.topic()))
                    .map(CollectEventBatch::transformRow)
                    .filter(Objects::nonNull);

            Dataset<Event> ds = spark.createDataset(rows.rdd(), eventEncoder);

            spark.udf().register("getCity", (UDF1<String, String>) ip -> {
                ip = ip.substring(0, ip.length() - 1) + 1;

                try {
                    InetAddress inetAddress = InetAddress.getByName(ip);
                    CityResponse response = readerBroadcast.getValue().getReader().city(inetAddress);
                    String city = response.getCity().getName();
                    System.out.println("ip: " + ip + "\tcity: " + city);

                    return city;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }, DataTypes.StringType);


            Dataset<Row> data = ds.select("app_id", "platform", "dvce_created_tstamp", "event", "event_id",
                    "user_id", "user_ipaddress", "domain_userid", "contexts", "unstruct_event");

            System.out.println("get city");
            data = data.withColumn("geo_city", call_udf("getCity", col("user_ipaddress")))
                    .persist();

            System.out.println("num record: " + data.count());
            data.show();

            String dateTime = dateTimeFormat.format(new DateTime(time.milliseconds()).toDate());
            String path = "hdfs://hadoop23202:9000/user/phuld/data/event/" + dateTime;
            data.coalesce(1)
                    .write()
                    .mode(SaveMode.Append)
                    .parquet(path);

            System.out.println("write to: " + path);

            data.unpersist();
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
