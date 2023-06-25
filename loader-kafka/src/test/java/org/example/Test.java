package org.example;


import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import org.hust.utils.IpLookupUtils;

import java.io.File;
import java.net.InetAddress;

public class Test {
    public static void main(String[] args) {
        String ipAddress = "116.99.7.x"; // Replace with the IP address you want to retrieve the address for
        System.out.println(IpLookupUtils.getCity(ipAddress));
    }
}
