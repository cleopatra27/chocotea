package com.chocotea.core.tests;

import com.chocotea.bean.postman.Event;
import com.chocotea.bean.postman.Item;
import com.chocotea.core.annotations.ChocoExpect;
import org.json.JSONObject;

import javax.lang.model.element.Element;
import javax.validation.constraints.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class BodyTests {
    private String tobe400 = "pm.test(\"Status test\", function () {\n" +
            "    pm.response.to.have.status(400);\n" +
            "});";

    private String responseCheck = "pm.test (\"Validate response body contains valid response\", function (){\n" +
            "var jsonData = pm. response.json();\n" +
            "pm.expect (jsonData.message).to.be.a(\"String\");\n" +
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

    public void performMixedTests(List<? extends Element> fields, List<Item> mixedItems, Item itemSent){

        //loop through fields
        for (Element field: fields) {
                //handle the test on said field
//            if(field.getKind().isVariable() && field.asType().toString().contains("String")) {
            if(field.getKind().isVariable()) {
                mixedItems.add(validateNotNull(itemSent, field));
                mixedItems.add(validateSize(itemSent, field));
                mixedItems.add(validateNotBlank(itemSent, field));
                mixedItems.add(validateNotEmpty(itemSent, field));
                mixedItems.add(verifyRandomNumbers(itemSent, field));
            }
        }

    }

    private Item verifyRandomNumbers(Item item, Element field){
        AtomicReference<Item> itemTemp = new AtomicReference<>();
       // Arrays.stream(field.getDeclaredAnnotations()).forEach(annotation -> {
                itemTemp.set(item.copy());
                itemTemp.get().setName("VERIFY_ENDPOINT_WITH_FIELD_AS_RANDOM_NUMBERS_"+ field.getSimpleName().toString());
                itemTemp.get().getRequest().getBody().setRaw(
                        new JSONObject(itemTemp.get().getRequest().getBody().getRaw())
                                .put(field.getSimpleName().toString(), new Random().nextInt(100 + 28) + 20).toString()
                );
                itemTemp.get().setEvent(new ArrayList<>());
                itemTemp.get().getEvent().add(new Event("test", tobe400));

        //});

        if(itemTemp != null){
            return itemTemp.get();
        }else{
            return null;
        }

        //return getItemTest(item, field, "VERIFY_ENDPOINT_WITH_FIELD_AS_RANDOM_NUMBERS_", 1342, Pattern.class);
    }

    private Item getItemTest(Item item, Element field, String name, Object val,  String annotationClass) {
        AtomicReference<Item> itemTemp = new AtomicReference<>();

        field.getAnnotationMirrors().forEach(annotation -> {
            if(annotation.getAnnotationType().asElement().getSimpleName().toString().contains(annotationClass)){
                itemTemp.set(item.copy());
                itemTemp.get().setName(name+ field.getSimpleName().toString());
                itemTemp.get().getRequest().getBody().setRaw(
                        new JSONObject(itemTemp.get().getRequest().getBody().getRaw())
                                .put(field.getSimpleName().toString(), val).toString()
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

    private String responseCheck(Class<?> responseClass){

        Arrays.stream(responseClass.getDeclaredAnnotations()).forEach(annotation -> {
            if(annotation.annotationType().equals(ChocoExpect.class)){

            }
        });

        return "pm.test (\"Validate response body contains valid response\", function (){\n" +
                "var jsonData = pm. response.json();\n" +
                "pm.expect (jsonData.message).to.be.a(\"String\");\n" +
                "pm.expect (jsonData.success).to.equal(false);\n" +
                "});";
    }

    private Item validateSize(Item item, Element field){
        AtomicInteger min = new AtomicInteger();
        AtomicInteger max = new AtomicInteger();
        if(field.getAnnotation(Size.class) != null){
            min.set(field.getAnnotation(Size.class).min());
            max.set(field.getAnnotation(Size.class).max());
        }

        return getItemTest(item, field, "VERIFY_ENDPOINT_ERROR_MESSAGE_WITH_GREATER_SIZE_FIELDS_",
                new Random().nextInt(min.get(),max.get() + 100) + min.get(),
                "Size");
    }
    private Item validateNotBlank(Item item, Element field){
        return getItemTest(item, field, "VERIFY_ENDPOINT_WITH_FIELD_AS_BLANK_", "", "NotBlank");
    }

    private Item validateNotEmpty(Item item, Element field){
        return getItemTest(item, field, "VERIFY_ENDPOINT_WITH_FIELD_AS_EMPTY_", "", "NotEmpty");
    }

    private Item validateNotNull(Item item, Element field){
        return getItemTest(item, field, "VERIFY_ENDPOINT_WITH_FIELD_AS_NULL_", null, "NotNull");
    }

}
