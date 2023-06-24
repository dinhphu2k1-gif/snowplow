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
import org.hust.utils.DateTimeUtils;
import org.hust.utils.SparkUtils;

import java.util.ArrayList;
import java.util.List;

public class AggregateData {
    private SparkUtils sparkUtils;
    private SparkSession spark;

    public AggregateData() {
        sparkUtils = new SparkUtils("aggregate data", "yarn", 60);
        spark = sparkUtils.getSparkSession();
    }

    /**
     * Lọc data product
     */
    public Dataset<Row> transformProductDf(Dataset<Event> ds){
        StructType schema = new StructType()
                .add("action", DataTypes.StringType, false)
                .add("product_id", DataTypes.IntegerType, false)
                .add("product_name", DataTypes.StringType, false)
                .add("quantity", DataTypes.IntegerType, false)
                .add("price", DataTypes.IntegerType, false)
                .add("category_id", DataTypes.IntegerType, false)
                .add("publisher_id", DataTypes.IntegerType, false)
                .add("author_id", DataTypes.IntegerType, false);
        ExpressionEncoder<Row> encoder = RowEncoder.apply(schema);

        Dataset<Row> df = ds.mapPartitions((MapPartitionsFunction<Event, Row>) t -> {
            List<Row> rowList = new ArrayList<>();

            while (t.hasNext()) {
                Event event = t.next();
                List<IContext> contextList = IContext.createContext(event);
                IUnstructEvent unstructEvent = IUnstructEvent.createEvent(event);

                // nếu không phải là product action thì bỏ qua
                if (!(unstructEvent instanceof ProductAction)) {
                    continue;
                }

                ProductAction productAction = (ProductAction) unstructEvent;

                for (IContext context : contextList) {
                    if (context instanceof ProductContext) {
                        ProductContext productContext = (ProductContext) context;

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

    public void topViewProduct(Dataset<Event> ds) {


        Dataset<Event> productActionDs = ds.filter("event_name = 'product_action'");

    }

    public void run() {
        String path = "hdfs://172.19.0.20:9000/data/event/" + DateTimeUtils.getDate() + "/*";
        Encoder<Event> eventEncoder = Encoders.bean(Event.class);

        Dataset<Row> df = spark.read().parquet(path);
        df.show();

        Dataset<Event> ds = spark.read().parquet(path).as(eventEncoder);
        ds.show();

        Dataset<Event> unstructDs = ds.filter("event = 'unstruct'");
        unstructDs.show();

        // thực hiện các hàm tổng hợp
//        topViewProduct(unstructDs);
        Dataset<Row> data = transformProductDf(unstructDs);
        data.show();

    }

    public static void main(String[] args) {
        AggregateData aggregateData = new AggregateData();
        aggregateData.run();
    }
}
