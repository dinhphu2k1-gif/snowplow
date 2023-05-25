package org.hust.loader.kafka.elasticsearch.index;

public class TrackingActionSearch {
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

}
