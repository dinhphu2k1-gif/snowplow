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

//        System.out.println(loaderService.getMappingUserId("1a6cfa58-9bac-4aae-9fe3-a1c829a1cc03"));
        DomainUserIdList domainUserIdList = loaderService.getMappingDomainUserId(4545);
        for(Map.Entry<String, Long> entry : domainUserIdList.map.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
    }
}
