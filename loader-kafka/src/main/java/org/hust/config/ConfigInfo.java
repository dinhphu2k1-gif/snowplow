package org.hust.config;

import com.vcc.bigdata.util.ActiveHadoopNameNode;
import org.apache.http.HttpHost;
import org.hust.utils.HostUtils;

public class ConfigInfo {
    public static class HdfsNamenode {
        public static final String ACTIVE_NAMENODE_HADOOP_23202 = ActiveHadoopNameNode.get(new String[]{"192.168.23.202", "192.168.23.203"});
        public static final String ACTIVE_NAMENODE_HADOOP_586 = ActiveHadoopNameNode.get(new String[]{"172.18.5.86", "172.18.5.87"});
    }

    public static class Elasticsearch {
        public final static HttpHost[] ES_HOST = HostUtils.getEsHost();
    }

    public static class Kafka {
        public final static String KAFKA_HOST = HostUtils.getKafkaHost();
    }

    public static class Mysql {
        public final static String MYSQL_HOST = HostUtils.getMysqlHost();
        public final static String MYSQL_PORT = "3306";
        public final static String MYSQL_DATABASE = "customer-data-platform";
        public final static String MYSQL_USER = "book_shop";
        public final static String MYSQL_PASSWORD = "package1107N";
    }

    public final static String GEOLITE2_CITY = "properties/GeoLite2-City.mmdb";

    public static class Hbase {
        public static final String HBASE_ZOOKEEPER_QUORUM = "10.3.105.174,10.3.105.211,10.3.104.174"; // con hbase dev
        public final static String HBASE_ZOOKEEPER_PORT = "2181";
        public final static String HBASE_TABLE_CDP_MAPPING = "cdp_mapping";
        public final static String HBASE_FAMILY_MAPPING = "mapping";
        public final static String HBASE_QUALIFIER_MAPPING = "mapping";
    }
}
