package org.hust.loader.kafka.elasticsearch;

import org.hust.loader.kafka.elasticsearch.index.TrackingActionProduct;
import org.hust.loader.kafka.elasticsearch.index.TrackingActionSearch;
import org.hust.model.entity.IContext;
import org.hust.model.entity.impl.ProductContext;
import org.hust.model.entity.impl.UserContext;
import org.hust.model.event.Event;
import org.hust.model.event.unstruct.IUnstructEvent;
import org.hust.model.event.unstruct.impl.ProductAction;
import org.hust.model.event.unstruct.impl.SearchAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Các document được tạo từ các unstruct event
 */
public interface IUnstructDocument {
    static List<IUnstructDocument> createDocument(Event event) {
        List<IContext> contextList = IContext.createContext(event);
        IUnstructEvent unstructEvent = IUnstructEvent.createEvent(event);

        List<ProductContext> productContextList = new ArrayList<>();
        UserContext userContext = null;

        for (IContext context : contextList) {
            if (context instanceof ProductContext) {
                ProductContext productContext = (ProductContext) context;
                productContextList .add(productContext);
            } else if (context instanceof UserContext) {
                userContext = (UserContext) context;
            }
        }

        List<IUnstructDocument> unstructDocumentList = new ArrayList<>();
        if (unstructEvent instanceof ProductAction) {
            for (ProductContext productContext : productContextList) {
                IUnstructDocument unstructDocument = new TrackingActionProduct(event, productContext, userContext, (ProductAction) unstructEvent);
                unstructDocumentList.add(unstructDocument);
            }
        } else if (unstructEvent instanceof SearchAction) {
            IUnstructDocument unstructDocument = new TrackingActionSearch(event, userContext,(SearchAction) unstructEvent);
            unstructDocumentList.add(unstructDocument);
        }

        return unstructDocumentList;
    }
}
