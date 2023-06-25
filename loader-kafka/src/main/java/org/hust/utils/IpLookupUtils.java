package org.hust.utils;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Location;
import org.hust.config.ConfigInfo;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;

public class IpLookupUtils implements Serializable {
    private static final DatabaseReader reader;

    static {
        File database = new File(ConfigInfo.GEOLITE2_CITY);
        try {
            reader = new DatabaseReader.Builder(database).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Lấy tên quốc gia từ ip
     */
    public static String getCountry(String ipAddress) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            CityResponse response = reader.city(inetAddress);

            return response.getCountry().getName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

//    public static String getRegion(String ipAddress) {
//        try {
//            InetAddress inetAddress = InetAddress.getByName(ipAddress);
//            CityResponse response = reader.city(inetAddress);
//
//            return response.getCountry().;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return "-1";
//    }

    /**
     * Lấy tên thành phố từ ip
     * @return
     */
    public static String getCity(String ipAddress) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            CityResponse response = reader.city(inetAddress);

            return response.getCity().getName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getZipcode(String ipAddress) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            CityResponse response = reader.city(inetAddress);

            return response.getPostal().getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getLatitude(String ipAddress) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            CityResponse response = reader.city(inetAddress);

            Location location = response.getLocation();

            return String.valueOf(location.getLatitude());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getLongitude(String ipAddress) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            CityResponse response = reader.city(inetAddress);

            Location location = response.getLocation();

            return String.valueOf(location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
