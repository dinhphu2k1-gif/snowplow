package org.hust.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy_MM_dd/HH_mm_ss");

    /**
     * Mặc định là ngày hôm nay
     */
    public static String getDate() {
        return dateFormat.format(new Date());
    }

    public static String getDate(long time) {
        return dateFormat.format(new Date(time));
    }

    /**
     * Mặc định là ngày hôm nay
     */
    public static String getDateTime() {
        return dateTimeFormat.format(new Date());
    }

    public static String getDateTime(long time) {
        return dateTimeFormat.format(new Date(time));
    }

}
