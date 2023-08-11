package org.hust.storage.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.hust.config.ConfigInfo;

public class HbaseConnection {
    private static Connection connection;

    public static void initConnection() {
        if (connection == null) {
            synchronized (HbaseConnection.class) {
                if (connection == null) {
                    Configuration conf = HBaseConfiguration.create();
                    conf.set("hbase.zookeeper.quorum", ConfigInfo.Hbase.HBASE_ZOOKEEPER_QUORUM);
                    conf.set("hbase.zookeeper.property.clientPort", ConfigInfo.Hbase.HBASE_ZOOKEEPER_PORT);
                    conf.set("hbase.client.scanner.caching", "5000");

                    try {
                        connection = ConnectionFactory.createConnection(conf);
                        System.out.println("INIT HBASE CONNECTION!!!!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static Connection getConnection(){
        initConnection();

        return connection;
    }
}
