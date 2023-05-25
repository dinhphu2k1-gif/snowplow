package org.example;


import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;

public class Test {
    public static void main(String[] args) {
        String time = "2023-05-15T10:15:01.178Z";
        System.out.println(LocalDateTime.parse(time, ISODateTimeFormat.dateTime()).toDateTime().getMillis());
    }
}
