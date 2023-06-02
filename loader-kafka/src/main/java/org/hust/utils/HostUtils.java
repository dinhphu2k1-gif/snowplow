package org.hust.utils;

import org.apache.http.HttpHost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HostUtils {
    public static String getKafkaHost() {
        return System.getenv("BOOTSTRAP_SERVERS") == null
                ? "localhost:29092,localhost:29093,localhost:29094"
                : System.getenv("BOOTSTRAP_SERVERS");
    }

    public static HttpHost[] getEsHost() {

        String addressString = System.getenv("ES_HOSTS");
        if (addressString == null) {

            return new HttpHost[]{
                    new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9201, "http"),
                    new HttpHost("localhost", 9202, "http")
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
}