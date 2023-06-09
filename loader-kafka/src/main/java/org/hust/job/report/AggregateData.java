package org.hust.job.report;

import org.apache.spark.api.java.function.MapPartitionsFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.hust.model.entity.IContext;
import org.hust.model.entity.impl.ProductContext;
import org.hust.model.event.Event;
import org.hust.model.event.unstruct.IUnstructEvent;
import org.hust.model.event.unstruct.impl.ProductAction;
import org.hust.model.event.unstruct.impl.SearchAction;
import org.hust.utils.DateTimeUtils;
import org.hust.utils.SparkUtils;

import java.util.ArrayList;
import java.util.List;

import static org.apache.spark.sql.functions.*;

public class AggregateData {
    private SparkUtils sparkUtils;
    private SparkSession spark;

    public AggregateData() {
        sparkUtils = new SparkUtils("aggregate data", "yarn", 60);
        spark = sparkUtils.getSparkSession();
    }

    /**
     * Lọc data product và hành động liên quan đến product đó
     */
    public Dataset<Row> transformProductDf(Dataset<Event> ds) {
        StructType schema = new StructType()
                .add("action", DataTypes.StringType, true)
                .add("product_id", DataTypes.IntegerType, true)
                .add("product_name", DataTypes.StringType, true)
                .add("quantity", DataTypes.IntegerType, true)
                .add("price", DataTypes.IntegerType, true)
                .add("category_id", DataTypes.IntegerType, true)
                .add("publisher_id", DataTypes.IntegerType, true)
                .add("author_id", DataTypes.IntegerType, true);
        ExpressionEncoder<Row> encoder = RowEncoder.apply(schema);

        Dataset<Row> df = ds
                .filter("event_name = 'product_action'")
                .mapPartitions((MapPartitionsFunction<Event, Row>) t -> {
                    List<Row> rowList = new ArrayList<>();

                    while (t.hasNext()) {
                        Event event = t.next();
                        List<IContext> contextList = IContext.createContext(event);
                        IUnstructEvent unstructEvent = IUnstructEvent.createEvent(event);

                        ProductAction productAction = (ProductAction) unstructEvent;

                        for (IContext context : contextList) {
                            if (context instanceof ProductContext) {
                                ProductContext productContext = (ProductContext) context;

                                assert productAction != null;
                                Row row = RowFactory.create(productAction.getAction(),
                                        productContext.getProduct_id(),
                                        productContext.getProduct_name(),
                                        productContext.getQuantity(),
                                        productContext.getPrice(),
                                        productContext.getCategory_id(),
                                        productContext.getPublisher_id(),
                                        productContext.getAuthor_id());
                                System.out.println(row);
                                rowList.add(row);
                            }
                        }
                    }

                    return rowList.iterator();
                }, encoder);

        return df;
    }

    /**
     *
     * @param df
     */
    public void productAnalysis(Dataset<Row> df) {
        Dataset<Row> res = df.filter("action = 'view'")
                .groupBy("product_id")
                .agg(count("*").as("num_view"));

        res.show();
    }

    /**
     *
     * @param df
     */
    public void categoryAnalysis(Dataset<Row> df) {
        Dataset<Row> res = df.filter("action = 'view'")
                .groupBy("category_id")
                .agg(count("*").as("num_view"));

        res.show();
    }

    /**
     *
     * @param df
     */
    public void rangeAnalysis(Dataset<Row> df) {
        Dataset<Row> res = df.filter("action = 'view'")
                .withColumn("range", when(col("price").lt(100000), "0 - 100000")
                        .when(col("price").geq(100000).and(col("price").lt(500000)), "100000 - 500000")
                        .otherwise(">= 500000"))
                .groupBy("range")
                .agg(count("*").as("num_view"));

        res.show();
    }

    /**
     *
     * @param df
     */
    public void viewAnalysis(Dataset<Row> df) {
        Dataset<Row> data = df.filter("event = 'page_view'");


    }

    /**
     *
     * @param df
     */
    public void locationAnalysis(Dataset<Row> df) {
        Dataset<Row> res = df.filter("action = 'purchase'")
                .groupBy("category_id")
                .agg(count("*").as("num_purchase"));

        res.show();
    }

    public void run() {
        String path = "hdfs://172.19.0.20:9000/data/event/" + DateTimeUtils.getDate() + "/*";
        System.out.println(path);

        Dataset<Row> data = spark.read().parquet(path);

//        productAnalysis(dataProduct);
//        categoryAnalysis(dataProduct);
//        rangeAnalysis(dataProduct);
        viewAnalysis(data);
//        locationAnalysis(dataProduct);
    }

    public static void main(String[] args) {
        AggregateData aggregateData = new AggregateData();
        aggregateData.run();
    }
}
