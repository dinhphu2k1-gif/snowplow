package org.hust.utils;

import org.apache.http.HttpHost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HostUtils {
    public static String getKafkaHost() {
        return System.getenv("BOOTSTRAP_SERVERS") == null
                ? "20.196.248.69:9092,20.196.245.32:9092"
                : System.getenv("BOOTSTRAP_SERVERS");
    }

    public static HttpHost[] getEsHost() {
        String addressString = System.getenv("ES_HOSTS");
        if (addressString == null) {

            return new HttpHost[]{
                    new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9201, "http"),
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
                ? "localhost"
                : System.getenv("MYSQL_HOST");
    }
}
