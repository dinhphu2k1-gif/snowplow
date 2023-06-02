package org.hust.config;

import org.apache.http.HttpHost;
import org.hust.utils.HostUtils;

public class ConfigInfo {
    public static class Elasticsearch {
        public final static HttpHost[] ES_HOST = HostUtils.getEsHost();
    }

    public static class Kafka {
        public final static String KAFKA_HOST = HostUtils.getKafkaHost();
    }

    public static void main(String[] args) {
        System.out.println(Kafka.KAFKA_HOST);
    }
}
