package org.hust.storage.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.hust.config.ConfigInfo;

import java.io.IOException;

/**
 * Khởi tạo connect tới Elasticsearch
 */
public class ElasticsearchClient {
    private static RestHighLevelClient esClient;

    public static void openConnect() {
        if (esClient == null) {
            synchronized (ElasticsearchClient.class) {
                if (esClient == null) {
                    esClient = new RestHighLevelClient(
                            RestClient.builder(
                                    ConfigInfo.Elasticsearch.ES_HOST
                            ));
                }
            }
        }
    }

    public static RestHighLevelClient getEsClient() {
        openConnect();
        return esClient;
    }

    public static void close() {
        try {
            esClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ElasticsearchClient.getEsClient();
        ElasticsearchClient.close();
    }
}
