package org.hust.utils;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.apache.hadoop.hbase.util.Bytes;

public class HashUtils {
    public static final HashFunction hashjoin = Hashing.murmur3_32(-1467523828);

    /**
     * key = hashInt(value) + value
     */
    public static byte[] hashPrefixKey(String string) {
        byte[] value = string.getBytes();
        byte[] prefix = hashjoin.hashBytes(value).asBytes();

        return Bytes.add(prefix, value);
    }
}
