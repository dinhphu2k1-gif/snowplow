package org.hust.service.loader;

import org.apache.hadoop.hbase.util.Bytes;
import org.hust.service.hbase.DomainUserIdList;
import org.hust.service.hbase.HbaseService;
import org.hust.utils.HashUtils;
import org.hust.utils.SerializationUtils;

import java.io.IOException;
import java.util.Map;

public class LoaderService {
    private HbaseService hbaseService;

    public LoaderService() {
        hbaseService = new HbaseService();
    }

    public void close() {
        hbaseService.close();
    }

    public void pushMapping(int user_id, DomainUserIdList domainUserIdList) {

    }

    public void pushMappingUserId(int user_id, String domain_userid) throws IOException {
        byte[] key = HashUtils.hashPrefixKey(domain_userid);
        byte[] value = Bytes.toBytes(user_id);

        hbaseService.pushMapping(key, value);
    }

    public int getMappingUserId(String domain_userid) throws IOException {
        byte[] key = HashUtils.hashPrefixKey(domain_userid);
        byte[] value = hbaseService.getMapping(key);

        return Bytes.toInt(value);
    }

    public DomainUserIdList getMappingDomainUserId(int user_id) throws IOException {
        byte[] key = HashUtils.hashPrefixKey(String.valueOf(user_id));
        byte[] value = hbaseService.getMapping(key);

        return (DomainUserIdList) SerializationUtils.deserialize(value);
    }

    public static void main(String[] args) throws IOException {
        LoaderService loaderService = new LoaderService();

        System.out.println(loaderService.getMappingUserId("00195799-8fb5-4dfe-b911-18989d57cb18"));
        DomainUserIdList domainUserIdList = loaderService.getMappingDomainUserId(1245);
        for(Map.Entry<String, Long> entry : domainUserIdList.map.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }

        loaderService.close();
    }
}
