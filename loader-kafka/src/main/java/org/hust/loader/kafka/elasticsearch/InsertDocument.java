package org.hust.loader.kafka.elasticsearch;

import org.elasticsearch.client.RestHighLevelClient;
import org.hust.storage.elasticsearch.ElasticsearchClient;

/**
 * Insert các event vào graph
 */
public class InsertDocument {
    private final static RestHighLevelClient esClient = ElasticsearchClient.getEsClient();

    public static void insertDocument(String eventType, String data) {
        switch (eventType) {
//            case UnstructEventType.ACTION_PRODUCT: {
//                IndexRequest request = new IndexRequest(IndexName.TRACKING_ACTION_PRODUCT);
//                request.source(data, XContentType.JSON);
//
//                try {
//                    esClient.index(request, RequestOptions.DEFAULT);
//                } catch (IOException e) {
////                            throw new RuntimeException(e);
////                            e.printStackTrace();
//                }
//            }
//            break;
//            case UnstructEventType.PURCHASE: {
//                IndexRequest request = new IndexRequest(IndexName.TRACKING_ACTION_SEARCH);
//                request.source(data, XContentType.JSON);
//
//                try {
//                    esClient.index(request, RequestOptions.DEFAULT);
//                } catch (IOException e) {
////                            throw new RuntimeException(e);
////                            e.printStackTrace();
//                }
//            }
//            break;

            default:
                break;
        }
    }
}
