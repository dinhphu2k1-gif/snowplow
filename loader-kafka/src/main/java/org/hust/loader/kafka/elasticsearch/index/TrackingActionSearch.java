package org.hust.loader.kafka.elasticsearch.index;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.ToString;
import org.hust.loader.kafka.elasticsearch.IUnstructDocument;
import org.hust.model.entity.impl.ProductContext;
import org.hust.model.entity.impl.UserContext;
import org.hust.model.event.Event;
import org.hust.model.event.unstruct.impl.ProductAction;
import org.hust.model.event.unstruct.impl.SearchAction;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
public class TrackingActionSearch implements IUnstructDocument {
    /**
     * Thời gian xảy ra sự kiện. VD 1684418681417
     */
    private long time;
    /**
     * Thòi gian xảy ra sự kiện. VD 18-05-2023 21:26:30
     */
    private String date;
    /**
     * id của sự kiện
     */
    private String event_id;
    /**
     * id của user, do trang web định danh
     */
    private String user_id;
    /**
     * id cuả user, do snowplow định danh
     */
    private String domain_userid;
    /**
     * Hành động liên quan đến sản phẩm
     */
    private String action;
    /**
     * Key word
     */
    private String search_value;

    public TrackingActionSearch(Event event, UserContext userContext, SearchAction searchAction) {
        time = event.getDvce_created_tstamp();
        date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date(time));
        event_id = event.getEvent_id();
        user_id = userContext.getUser_id();
        domain_userid = event.getDomain_userid();
        action = searchAction.getAction();
        search_value = searchAction.getSearch_value();
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
