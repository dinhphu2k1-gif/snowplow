package org.hust.loader.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * Nhận các event đã được enrich từ Kafka
 */
public class CollectEvent {
    private KafkaConsumer<String, String> consumer;

    public CollectEvent() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:29092,localhost:29093");
        props.put("group.id", "snowplow1");
        props.put("enable.auto.commit", "false");
        props.put("auto.offset.reset", "latest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<>(props);
    }

    public void run() {
        consumer.subscribe(Collections.singletonList("enriched"));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.value());
            }
        }

    }

    public static void main(String[] args) {
        CollectEvent collectEvent = new CollectEvent();
        collectEvent.run();
    }
}
