package org.hust.service.hbase;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.util.Bytes;
import org.hust.config.ConfigInfo;
import org.hust.storage.hbase.HbaseConnection;
import org.hust.utils.HashUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HbaseService {
    public Connection connection;
    public Admin admin;

    public HbaseService() {
        connection = HbaseConnection.getConnection();

        try {
            admin = connection.getAdmin();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            admin.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createTableMapping(int numRegion) throws IOException {
        TableName tableName = TableName.valueOf(ConfigInfo.Hbase.HBASE_TABLE_CDP_MAPPING);

        if (connection.getAdmin().tableExists(tableName)) {
            System.out.println("Table " + tableName.getNameAsString() + " exist!!!");
            return;
        }

        TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(tableName);

        List<ColumnFamilyDescriptor> familyDescriptors = new ArrayList<>();

        ColumnFamilyDescriptorBuilder indexBuilder = ColumnFamilyDescriptorBuilder
                .newBuilder(Bytes.toBytes(ConfigInfo.Hbase.HBASE_FAMILY_MAPPING))
                .setMaxVersions(1)
                .setCompressionType(Compression.Algorithm.GZ);
        familyDescriptors.add(indexBuilder.build());

        tableDescriptorBuilder.setColumnFamilies(familyDescriptors);

        // split point
        byte[] startKey = Bytes.toBytes(0);
        byte[] endKey = Bytes.toBytes(-1);

        connection.getAdmin().createTable(tableDescriptorBuilder.build(), startKey, endKey, numRegion);
    }

    public void pushMapping(byte[] key, byte[] data) throws IOException {
        Table table = connection.getTable(TableName.valueOf(ConfigInfo.Hbase.HBASE_TABLE_CDP_MAPPING));
        byte[] familyByte = Bytes.toBytes(ConfigInfo.Hbase.HBASE_FAMILY_MAPPING);
        byte[] qualifierByte = Bytes.toBytes(ConfigInfo.Hbase.HBASE_FAMILY_MAPPING);

        Put put = new Put(key);
        put.addColumn(familyByte, qualifierByte, data);
        table.put(put);
    }

    public byte[] getMapping(byte[] key) throws IOException {
        Table table = connection.getTable(TableName.valueOf(ConfigInfo.Hbase.HBASE_TABLE_CDP_MAPPING));

        byte[] family = Bytes.toBytes(ConfigInfo.Hbase.HBASE_FAMILY_MAPPING);
        byte[] qualifier = Bytes.toBytes(ConfigInfo.Hbase.HBASE_FAMILY_MAPPING);;

        Get get = new Get(key);
        get.addColumn(family, qualifier);

        Result result = table.get(get);
        return result.getValue(family, qualifier);
    }

    public static void main(String[] args) throws IOException {
        HbaseService hbaseService = new HbaseService();

        hbaseService.createTableMapping(5);

        hbaseService.close();
    }
}
