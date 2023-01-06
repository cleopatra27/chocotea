//package com.chocotea.tests;
//
//import com.chocotea.bean.postman.Event;
//import com.chocotea.bean.postman.Item;
//import org.json.JSONObject;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicReference;
//
//public class CurrencyTest {
//
//    public void performMixedTests(Field[] fields, List<Item> mixedItems, Item itemSent){
//
//        //loop through fields
//        for (Field field: fields) {
//            AtomicReference<Item> itemTemp = new AtomicReference<>();
//            Arrays.stream(field.getDeclaredAnnotations()).forEach(annotation -> {
//                if(annotation.annotationType().equals(CurrencyTest.class)){
//                    itemTemp.set(itemSent.copy());
//                    itemTemp.get().setName(name+ field.getName());
//                    itemTemp.get().getRequest().getBody().setRaw(
//                            new JSONObject(itemTemp.get().getRequest().getBody().getRaw())
//                                    .put(field.getName(), val).toString()
//                    );
//                    itemTemp.get().setEvent(new ArrayList<>());
//                    itemTemp.get().getEvent().add(new Event("test", tobe400));
//
//                }
//            });
//
//            if(itemTemp != null){
//                return itemTemp.get();
//            }else{
//                return null;
//            }
//        }
//    }
//}
