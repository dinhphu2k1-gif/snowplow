package org.hust.event.model;

import java.util.UUID;

/**
 * Thuộc tính của 1 bảng event
 * <br>
 * <a href="https://docs.snowplow.io/docs/understanding-your-pipeline/canonical-event/#overview">Structure data Snowplow</a>
 */
public class Event {
    /**
     * Application ID
     */
    private String app_id;
    /**
     * Platform. Example: "web"
     */
    private String platform;
    /**
     * Time khi event được validated và enriched
     */
    private String etl_tstamp;
    /**
     * Time event được lưu bởi collector
     */
    private String collector_tstamp;
    /**
     * Time event được tạo từ thiết bị client
     */
    private String dvce_created_tstamp;
    /**
     * Loại event
     */
    private String event;
    /**
     * Event ID
     */
    private String event_id;
    /**
     * Transaction ID
     */
    private int txn_id;
    /**
     * Tracker namespace
     */
    private String name_tracker;
    /**
     * Tracker version
     */
    private String v_tracker;
    /**
     * Collector version. Example: "ssc-2.1.0-kinesis"
     */
    private String v_collector;
    /**
     * ETL version. Example: "snowplow-micro-1.1.0-common-1.4.2"
     */
    private String v_etl;
    /**
     * Unique ID được tạo bởi business
     */
    private String user_id;
    /**
     * User IP address
     */
    private String user_ipaddress;
    /**
     *
     */
    private String user_fingerprint;
    /**
     * User ID - định danh bên thứ nhất 1st
     */
    private String domain_userid;
    /**
     * Số lần thăm / session cookie. Session cookie tính từ lúc tạo cho đến lúc cookie thay đổi
     */
    private int domain_sessionIdx;
    /**
     * User ID - định danh bên thứ ba 3rd
     */
    private String network_userid;

    private String geo_country;
    private String geo_region;
    private String geo_city;
    private String geo_zipcode;
    private String geo_latitude;
    private String geo_longitude;
    private String geo_region_name;
    private String ip_isp;
    private String ip_organization;
    private String ip_domain;
    private String ip_netspeed;
    /**
     * Page url
     */
    private String page_url;
    /**
     * Web page title. Example: 'Snowplow Docs - Understanding the structure of Snowplow data'
     */
    private String page_title;
    /**
     * URL tham chiếu đến
     */
    private String page_referrer;
    /**
     * Protocal
     */
    private String page_urlscheme;
    /**
     * Domain
     */
    private String page_urlhost;
    /**
     * Port (mặc định là 80)
     */
    private int page_urlport;
    /**
     * Đường dẫn đến page. Example: '/product/index.html'
     */
    private String page_urlpath;
    /**
     * Query String (giống truyền tham số trong phương thức GET). Example: 'id=GTM-DLRG'
     */
    private String page_urlquery;
    /**
     *
     */
    private String page_urlfragment;
    /**
     * Referer protocal
     */
    private String refr_urlscheme;
    /**
     * Referer host
     */
    private String refr_urlhost;
    private int refr_urlport;
    private String refr_urlpath;
    private String refr_urlquery;
    private String refr_urlfragment;
    private String refr_medium;
    private String refr_source;
    private String refr_term;
    private String useragent;
    private String mkt_medium;
    private String mkt_source;
    private String mkt_term;
    private String mkt_content;
    private String mkt_campaign;


}
