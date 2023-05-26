package org.hust.model.entity.impl;

import lombok.Getter;
import org.hust.model.entity.IContext;
import org.json.JSONObject;

/**
 * Product context
 */
@Getter
public class ProductContext implements IContext {
    private String product_id;
    private String product_name;
    private int quantity;
    private int price;
    private String category;
    private String publisher;
    private String author;

    public ProductContext(JSONObject data) {
        parseContext(data);
    }

    @Override
    public void parseContext(JSONObject data) {
        product_id = data.getString("product_id");
        product_name = data.getString("product_name");
        quantity = data.getInt("quantity");
        price = data.getInt("price");
        category = data.getString("category");
        publisher = data.getString("publisher");
        author = data.getString("author");
    }
}
