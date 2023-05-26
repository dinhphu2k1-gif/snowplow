package org.hust.model.entity.impl;

import lombok.Getter;
import org.hust.model.entity.IContext;
import org.json.JSONObject;

/**
 * User Context
 */
@Getter
public class UserContext implements IContext {
    private String user_id;
    private String user_name;
    private String phone_number;
    private String email;
    private String address;

    public UserContext(JSONObject data) {
        parseContext(data);
    }

    @Override
    public void parseContext(JSONObject data) {
        user_id = data.getString("user_id");
        user_name = data.getString("user_name");
        phone_number = data.getString("phone_number");
        email = data.getString("email");
        address = data.getString("address");
    }
}
