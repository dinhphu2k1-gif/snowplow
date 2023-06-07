package org.hust.utils;

import lombok.Getter;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

@Getter
public class SparkUtils {
    private SparkSession sparkSession = null;
    private JavaSparkContext javaSparkContext = null;
    private JavaStreamingContext javaStreamingContext = null;

    public SparkUtils(String jobName, String master, int duration) {
        sparkSession = SparkSession.builder().appName(jobName)
                .master(master)
                .getOrCreate();
        sparkSession.sparkContext().setLogLevel("ERROR");

        javaSparkContext = new JavaSparkContext(sparkSession.sparkContext());
        javaStreamingContext = new JavaStreamingContext(javaSparkContext, Durations.seconds(duration));
    }
}
