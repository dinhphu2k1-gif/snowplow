package org.hust.model.entity;

import org.hust.model.entity.impl.ProductContext;
import org.hust.model.entity.impl.UserContext;
import org.hust.model.event.Event;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public interface IContext {
    static IContext createContext(String contextType, String context) {
        switch (contextType) {
            case ContextType.USER_CONTEXT:
                return new UserContext();
            case ContextType.PRODUCT_CONTEXT:
                return new ProductContext();
        }

        return null;
    }

    void parseContext(String context);

    /**
     * Tách context từ event
     * @param event event được gửi từ kafka
     * @return danh sách context
     */
    static List<IContext> createContext(Event event) {
        JSONObject contexts = new JSONObject(event.getContexts());
        JSONArray data = contexts.getJSONArray("data");

        for (int i = 0; i < data.length(); i++) {
            JSONObject context = data.getJSONObject(i);
            String schema = context.getString("schema");

            

        }

        return null;
    }
}
