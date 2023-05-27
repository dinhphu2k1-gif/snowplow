package org.hust.loader.kafka.elasticsearch;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.hust.loader.kafka.elasticsearch.index.TrackingActionProduct;
import org.hust.loader.kafka.elasticsearch.index.TrackingActionSearch;
import org.hust.storage.elasticsearch.ElasticsearchClient;

import java.io.IOException;
import java.util.List;

/**
 * Insert các event vào graph
 */
public class InsertDocument {
    private final static RestHighLevelClient esClient = ElasticsearchClient.getEsClient();

    public static void insertDocument(List<IUnstructDocument> unstructDocumentList) {
        for (IUnstructDocument unstructDocument : unstructDocumentList) {
            IndexRequest request = null;

            if (unstructDocument instanceof TrackingActionProduct) {
                TrackingActionProduct document = (TrackingActionProduct) unstructDocument;
                System.out.println(document.toString());

                request = new IndexRequest(IndexName.TRACKING_ACTION_PRODUCT);
                request.source(document.toString(), XContentType.JSON);
                System.out.println("insert action product!!");
            } else if (unstructDocument instanceof TrackingActionSearch) {
                TrackingActionSearch document = (TrackingActionSearch) unstructDocument;
                System.out.println(document.toString());

                request = new IndexRequest(IndexName.TRACKING_ACTION_SEARCH);
                request.source(document.toString(), XContentType.JSON);
                System.out.println("insert action search!!");
            }

            try {
                esClient.index(request, RequestOptions.DEFAULT);
            } catch (IOException ignore) {
            }
        }


    }
}
