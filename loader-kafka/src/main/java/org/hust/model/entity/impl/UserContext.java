package org.hust.model.entity.impl;

import org.hust.model.entity.IContext;

/**
 *
 */
public class UserContext implements IContext {
    private String user_id;
    private String user_name;
    private String phone_number;
    private String email;
    private String address;


    @Override
    public void parseContext(String context) {

    }
}
