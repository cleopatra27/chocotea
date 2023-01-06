package com.chocotea.tests;

import com.chocotea.bean.postman.Event;
import com.chocotea.bean.postman.Item;
import org.json.JSONObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class StringTests {
    private String tobe400 = "pm.test(\"Status test\", function () {\n" +
            "    pm.response.to.have.status(400);\n" +
            "});";

    private String responseCheck = "pm.test (\"Validate response body contains valid response\", function (){\n" +
            "var jsonData = pm. response.json();\n" +
            "pm. expect (jsonData.message).to.be.a(\"String\");\n" +
            "pm.expect (jsonData.success).to.equal(false);\n" +
            "});";

    public void performNegativeTests(Field[] fields, List<Item> negativeItems, Item itemSent){
        Field[] tempFields = new Field[fields.length];
        int vak = 0;
        //loop through fields
        for (Field field: fields) {
            //if string
            if(field.getType().equals(String.class)){
                tempFields[vak] = field;
                vak++;
            }
        }

    }


    public void performMixedTests(Field[] fields, List<Item> mixedItems, Item itemSent){;
        //loop through fields
        for (Field field: fields) {
            //if string
            if(field.getType().equals(String.class)){
                //replicate existing item to a new item
                //Item itemTemp = itemSent.copy();

                //handle the test on said field
                if(validateNotNull(itemSent, field) != null){
                    mixedItems.add(validateNotNull(itemSent, field));
                }
                mixedItems.add(verifyRandomNumbers(itemSent, field));
            }
        }
        mixedItems.add(validateNotBlank(itemSent, fields));

    }

    public Item verifyRandomNumbers(Item item, Field field){
        return getItemTest(item, field, "VERIFY_ENDPOINT_WITH_FIELD_AS_RANDOM_NUMBERS_", 1342, Pattern.class);
    }

    private Item getItemTest(Item item, Field field, String name, Object val, Class<?> annotationClass) {
        AtomicReference<Item> itemTemp = null;
        Arrays.stream(field.getDeclaredAnnotations()).forEach(annotation -> {
            if(annotation.annotationType().equals(annotationClass)){
                itemTemp.set(item.copy());
                itemTemp.get().setName(name+ field.getName());
                itemTemp.get().getRequest().getBody().setRaw(
                        new JSONObject(itemTemp.get().getRequest().getBody().getRaw())
                                .put(field.getName(), val).toString()
                );
                itemTemp.get().setEvent(new ArrayList<>());
                itemTemp.get().getEvent().add(new Event("test", tobe400));
            }
        });

        if(itemTemp != null){
            return itemTemp.get();
        }else{
          return null;
        }
    }

    public Item validateNotBlank(Item item, Field[] fields){
        Item itemTemp = item.copy();

        for (Field field:fields) {
            Arrays.stream(field.getDeclaredAnnotations()).forEach(annotation -> {
                if(annotation.annotationType().equals(NotBlank.class)){
                    itemTemp.setName("VERIFY_ENDPOINT_ERROR_MESSAGE_WITH_EMPTY_FIELDS_" + field.getName());
                    itemTemp.getRequest().getBody().setRaw(
                            new JSONObject(itemTemp.getRequest().getBody().getRaw())
                                    .put(field.getName(), "").toString()
                    );
                }
            });
        }

        itemTemp.setEvent(new ArrayList<>());
        itemTemp.getEvent().add(new Event("test", tobe400));
        return itemTemp;

    }


    public Item validateNotNull(Item item, Field field){
        return getItemTest(item, field, "VERIFY_ENDPOINT_WITH_FIELD_AS_EMPTY_", "", NotNull.class);
    }
}