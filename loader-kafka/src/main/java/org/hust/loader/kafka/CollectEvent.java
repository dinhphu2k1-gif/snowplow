package org.hust.loader.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.elasticsearch.client.RestHighLevelClient;
import org.hust.model.entity.IContext;
import org.hust.model.event.Event;
import org.hust.model.event.EventType;
import org.hust.storage.elasticsearch.ElasticsearchClient;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Nhận các event đã được enrich từ Kafka
 */
public class CollectEvent {
    private final KafkaConsumer<String, String> consumer;
    private final RestHighLevelClient esClient;

    public CollectEvent() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:29092,localhost:29093");
        props.put("group.id", "snowplow1");
        props.put("enable.auto.commit", "false");
        props.put("auto.offset.reset", "latest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<>(props);
        esClient = ElasticsearchClient.getEsClient();
    }

    public void run() {
        consumer.subscribe(Collections.singletonList("enriched"));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1));
            for (ConsumerRecord<String, String> record : records) {
                String value = record.value();
                System.out.println(value);
                Event event = new Event(value);

                List<IContext> contextList = IContext.createContext(event);

                switch (event.getEvent()) {
                    case EventType.UNSTRUCT: {

                    }
                    break;
                }
            }
        }

    }

    public static void main(String[] args) {
        CollectEvent collectEvent = new CollectEvent();
        collectEvent.run();
    }
}
