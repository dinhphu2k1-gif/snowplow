package org.hust.loader.kafka.elasticsearch.index;

/**
 * Lưu lại cac sự kiện xảy ra với sản phẩm
 */
public class TrackingActionProduct {
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
     * Sản phẩm liên quan đến sự kiện
     */
    private String product;


}
