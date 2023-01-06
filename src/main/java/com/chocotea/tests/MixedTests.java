package com.chocotea.tests;

import com.chocotea.bean.postman.Event;
import com.chocotea.bean.postman.Item;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MixedTests implements Tests{

    public Item handle(Item item, Field field){
        verifyEmptyError(item, field);
        return null;
    }

    /**
     * Sests strings in body to an empty string for field passed
     * @param item cloned Item
     * @param field
     * @return
     */
    public Item verifyEmptyError(Item item, Field field){
        item.getRequest().getBody().setRaw(
                new JSONObject(item.getRequest().getBody().getRaw())
                        .put(field.getName(), "").toString()
        );

        item.setEvent(new ArrayList<>());
        item.getEvent().add(new Event("test", "tobe400"));
        return item;

    }
}
