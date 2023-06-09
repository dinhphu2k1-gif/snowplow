package org.hust.utils;

import org.apache.http.HttpHost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HostUtils {
    public static String getKafkaHost() {
        return System.getenv("BOOTSTRAP_SERVERS") == null
                ? "172.19.0.8:9092,172.19.0.9:9092"
                : System.getenv("BOOTSTRAP_SERVERS");
    }

    public static HttpHost[] getEsHost() {
        String addressString = System.getenv("ES_HOSTS");
        if (addressString == null) {

            return new HttpHost[]{
                    new HttpHost("172.19.0.10", 9200, "http")
            };
        } else {
            List<String> addressList = Arrays.asList(addressString.split(","));

            List<HttpHost> esHosts = new ArrayList<>();
            for (String address : addressList) {
                String host = address.split(":")[0];
                int port = Integer.parseInt(address.split(":")[1]);

                HttpHost httpHost = new HttpHost(host, port, "http");
                esHosts.add(httpHost);

            }

            return esHosts.toArray(new HttpHost[0]);
        }
    }

    public static String getMysqlHost() {
        return System.getenv("MYSQL_HOST") == null
                ? "172.19.0.2"
                : System.getenv("MYSQL_HOST");
    }
}
