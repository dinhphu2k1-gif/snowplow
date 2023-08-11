package org.hust.job.stream;

import com.vcc.bigdata.util.Bytes;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.*;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.kafka010.*;
import org.hust.job.ArgsOptional;
import org.hust.job.IJobBuilder;
import org.hust.loader.IRecord;
import org.hust.loader.kafka.elasticsearch.InsertEs;
import org.hust.model.event.Event;
import org.hust.model.event.EventType;
import org.hust.service.hbase.DomainUserIdList;
import org.hust.service.hbase.HbaseService;
import org.hust.service.mysql.MysqlService;
import org.hust.utils.HashUtils;
import org.hust.utils.KafkaUtils;
import org.hust.utils.SerializationUtils;
import org.hust.utils.SparkUtils;
import org.joda.time.DateTime;
import scala.Tuple2;

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
        String taskName = "collect event to es";
        String groupId = taskName + args.getGroupId();

        sparkUtils = new SparkUtils(taskName, "yarn", args.getDuration());
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
        System.out.println(value);
        return new Event(value);
    }

    public void insertIntoEs(Dataset<Event> ds) {
        ds.foreachPartition(t -> {
            InsertEs insertEs = new InsertEs();
            while (t.hasNext()) {
                Event event = t.next();

                switch (event.getEvent()) {
                    case EventType.UNSTRUCT: {
                        IRecord iRecord = IRecord.createRecord(event);
                        try {
                            insertEs.insertDocument(iRecord);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        });
    }

//    public void insertMapping(Dataset<Event> ds) {
//        Dataset<Row> data = spark.createDataFrame(ds.rdd(), Event.class);
//
//        Dataset<Row> mapping = data
//                .select("user_id", "domain_userid")
//                .filter("user_id != '' and domain_userid != ''")
//                .dropDuplicates();
//
//        mapping.toJavaRDD()
//                .mapPartitionsToPair(t -> {
//                    List<Tuple2<Integer, List<String>>> out = new ArrayList<>();
//                    while (t.hasNext()) {
//                        Row row = t.next();
//                        int user_id = Integer.parseInt(row.getString(0));
//                        String domain_userid = row.getString(1);
//
//                        out.add(new Tuple2<>(user_id, Collections.singletonList(domain_userid)));
//                    }
//
//                    return out.iterator();
//                })
//                .filter(Objects::nonNull)
//                .reduceByKey((a, b) -> {
//                    List<String> out = new ArrayList<>();
//                    out.addAll(a);
//                    out.addAll(b);
//
//                    return out;
//                })
//                .foreachPartition(t -> {
//                    HbaseService hbaseService = new HbaseService();
//
//                    while (t.hasNext()) {
//                        Tuple2<Integer, List<String>> tuple2 = t.next();
//
//                        try {
//                            int user_id = tuple2._1;
//                            List<String> domainUserIdList = tuple2._2;
//
//                            for (String domain_userid : domainUserIdList) {
//                                System.out.println("user_id: " + user_id + "\tdomain_userid: " + domain_userid);
//
//                                byte[] key = HashUtils.hashPrefixKey(domain_userid);
//                                byte[] value = Bytes.toBytes(user_id);
//
//                                hbaseService.pushMapping(key, value);
//                            }
//
//                            // push user_id -> list domain user id
//                            byte[] key = HashUtils.hashPrefixKey(String.valueOf(user_id));
//                            byte[] valueOld = hbaseService.getMapping(key);
//                            DomainUserIdList domainUserIdListNew;
//                            if (valueOld == null) {
//                                domainUserIdListNew = new DomainUserIdList();
//                            } else {
//                                domainUserIdListNew = (DomainUserIdList) SerializationUtils.deserialize(valueOld);
//                            }
//
//                            for (String domain_userid : domainUserIdList) {
//                                domainUserIdListNew.map.put(domain_userid, System.currentTimeMillis());
//                            }
//
//                            byte[] valueNew = SerializationUtils.serialize(domainUserIdListNew);
//                            hbaseService.pushMapping(key, valueNew);
//                            System.out.println("insert mapping");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//    }

    @Override
    public void run(ArgsOptional args) {
        loadAgrs(args);
        init();

        Encoder<Event> eventEncoder = Encoders.bean(Event.class);

        stream.foreachRDD((consumerRecordJavaRDD, time) -> {
//            OffsetRange[] offsetRanges = ((HasOffsetRanges) consumerRecordJavaRDD.rdd()).offsetRanges();

            String dateTime = dateTimeFormat.format(new DateTime(time.milliseconds()).toDate());
            System.out.println("time: " + dateTime);

            JavaRDD<Event> rows = consumerRecordJavaRDD
                    .map(consumerRecord -> RowFactory.create(consumerRecord.value(), consumerRecord.topic()))
                    .map(CollectEventStream::transformRow)
                    .filter(Objects::nonNull);

            Dataset<Event> ds = spark.createDataset(rows.rdd(), eventEncoder)
                    .repartition(20)
                    .persist();
            System.out.println("num record: " + ds.count());

            ds.select("app_id", "platform", "dvce_created_tstamp", "event", "event_id", "page_url",
                    "user_id", "user_ipaddress", "domain_userid", "geo_city", "contexts", "unstruct_event").show();

            long t2 = System.currentTimeMillis();
            insertIntoEs(ds);
            System.out.println("time insert es: " + (System.currentTimeMillis() - t2) + " ms");

            long t3 = System.currentTimeMillis();
//            insertMapping(ds);
            System.out.println("time insert hbase: " + (System.currentTimeMillis() - t3) + " ms");

            ds.unpersist();

//            ((CanCommitOffsets) stream.inputDStream()).commitAsync(offsetRanges);
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
