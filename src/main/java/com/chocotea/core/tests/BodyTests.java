package com.chocotea.core.tests;

import com.chocotea.bean.HTTPVerbs;
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

public class BodyTests extends TestGenerator.PostmanVerify {

    public void performNegativeInvalidTests(List<? extends Element> fields, List<Item> negativeItems, Item itemSent){
        AtomicReference<Item> itemTemp = new AtomicReference<>();
        itemTemp.set(itemSent.copy());

        //add random invalid field to request body
        itemTemp.get().getRequest().getBody().setRaw(
                new JSONObject(itemTemp.get().getRequest().getBody().getRaw())
                        .put("tempKey", "more toast").toString());
        itemTemp.get().setName("VERIFY_ENDPOINT_WITH_INVALID_PAYLOAD");
        itemTemp.get().setEvent(new ArrayList<>());
        itemTemp.get().getEvent().add(new Event("test", postmanVerifyStatusCode(400)));

        //look for chocoExpect
        Map<String, Object> expect = new HashMap<>();
        fields.forEach(field -> {
            if(field.getAnnotationsByType(ChocoExpect.class) != null){
                expect.put(field.asType().toString(),
                        field.getSimpleName().toString());
            }
        });

        itemTemp.get().getEvent().add(new Event("test", postmanVerifyResponseBody(expect)));

        negativeItems.add(itemTemp.get());

    }

    public void performNegativeEmptyTests(List<? extends Element> fields, List<Item> negativeItems, Item itemSent){
        AtomicReference<Item> itemTemp = new AtomicReference<>();
        itemTemp.set(itemSent.copy());

        //replace fields values with empty string
        //look for chocoExpect
        Map<String, Object> expect = new HashMap<>();
        fields.forEach(field -> {
            if(field.getKind().isVariable()) {
                itemTemp.get().getRequest().getBody().setRaw(
                        new JSONObject(itemTemp.get().getRequest().getBody().getRaw())
                                .put(field.getSimpleName().toString(), "").toString()
                );

                if (field.getAnnotationsByType(ChocoExpect.class) != null) {
                    expect.put(field.asType().toString(),
                            field.getSimpleName().toString());
                }
            }
        });

        itemTemp.get().setName("VERIFY_ENDPOINT_WITH_EMPTY_PAYLOAD");
        itemTemp.get().setEvent(new ArrayList<>());
        itemTemp.get().getEvent().add(new Event("test", postmanVerifyStatusCode(400)));
        itemTemp.get().getEvent().add(new Event("test", postmanVerifyResponseBody(expect)));

        negativeItems.add(itemTemp.get());

    }

    public void performNegativeHttpCodeTests(List<Item> negativeItems, Item itemSent){
        AtomicReference<Item> itemTemp = new AtomicReference<>();
        itemTemp.set(itemSent.copy());

        List<HTTPVerbs> methods = new LinkedList<>(Arrays.asList(HTTPVerbs.values()));
        methods.remove(HTTPVerbs.valueOf(
                itemTemp.get().getRequest().getMethod()
        ));

        itemTemp.get().getRequest().setMethod(methods.get(1));

        itemTemp.get().setName("VERIFY_ENDPOINT_WITH_INVALID_HTTP_METHOD");
        itemTemp.get().setEvent(new ArrayList<>());
        itemTemp.get().getEvent().add(new Event("test", postmanVerifyStatusCode(400)));

        negativeItems.add(itemTemp.get());

    }

    public void verifySpecialCharacters(){
        //$%$#$
    }

    public void performTests(List<? extends Element> fields, List<Item> mixedItems, List<Item> negativeItems, Item itemSent){
        performMixedTests(fields, mixedItems,  itemSent);
        performNegativeHttpCodeTests(negativeItems,  itemSent);
        performNegativeEmptyTests(fields, negativeItems,  itemSent);
        performNegativeInvalidTests(fields, negativeItems,  itemSent);
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

                // perform more  decimal place test
            }
        }

    }

    private Item verifyRandomNumbers(Item item, Element field){
        AtomicReference<Item> itemTemp = new AtomicReference<>();
                itemTemp.set(item.copy());
                itemTemp.get().setName("VERIFY_ENDPOINT_WITH_FIELD_AS_RANDOM_NUMBERS_"+ field.getSimpleName().toString());
                itemTemp.get().getRequest().getBody().setRaw(
                        new JSONObject(itemTemp.get().getRequest().getBody().getRaw())
                                .put(field.getSimpleName().toString(), new Random().nextInt(100 + 28) + 20).toString()
                );
                itemTemp.get().setEvent(new ArrayList<>());
                itemTemp.get().getEvent().add(new Event("test", postmanVerifyStatusCode(400)));

        if(itemTemp != null){
            return itemTemp.get();
        }else{
            return null;
        }
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
                itemTemp.get().getEvent().add(new Event("test", postmanVerifyStatusCode(400)));

            }
        });

        if(itemTemp != null){
            return itemTemp.get();
        }else{
          return null;
        }
    }

    private Item validateSize(Item item, Element field){
        AtomicInteger min = new AtomicInteger();
        AtomicInteger max = new AtomicInteger();
        if(field.getAnnotation(Size.class) != null){
            min.set(field.getAnnotation(Size.class).min());
            max.set(field.getAnnotation(Size.class).max());
        }

        //add less than charcter lenght

        return getItemTest(item, field, "VERIFY_ENDPOINT_ERROR_MESSAGE_WITH_GREATER_SIZE_FIELDS_",
                new Random().nextInt(min.get(),max.get() + 100) + min.get(),
                "Size");
    }
    private Item validateNotBlank(Item item, Element field){
        return getItemTest(item, field, "VERIFY_ENDPOINT_WITH_FIELD_AS_BLANK_", "", "NotBlank");
    }

    private Item validateDecimal(Item item, Element field){
        return getItemTest(item, field, "VERIFY_ENDPOINT_WITH_MORE_DECIMAL_PLACES",
                "change to more decimal places than defined", "Decimal");
    }

    private Item validateNotEmpty(Item item, Element field){
        return getItemTest(item, field, "VERIFY_ENDPOINT_WITH_FIELD_AS_EMPTY_", "", "NotEmpty");
    }

    private Item validateNotNull(Item item, Element field){
        return getItemTest(item, field, "VERIFY_ENDPOINT_WITH_FIELD_AS_NULL_", null, "NotNull");
    }

}
