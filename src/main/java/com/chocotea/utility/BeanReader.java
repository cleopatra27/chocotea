package com.chocotea.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.chocotea.bean.postman.DynamicVariables;
import com.chocotea.core.annotations.ChocoRandom;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.chocotea.utility.RandomGenerator.integerGenerator;
import static com.chocotea.utility.RandomGenerator.stringGenerator;

public class BeanReader {

    public static String toString(String val) {

        String value;
        AtomicBoolean generateRandom = new AtomicBoolean(false);
        AtomicReference<DynamicVariables> variable = new AtomicReference<>();
        try {
            Object vd = Class.forName(val).newInstance();
            for(Field field : vd.getClass().getDeclaredFields()){

                Arrays.stream(field.getDeclaredAnnotations()).forEach(annotation -> {
                    if(annotation.annotationType().equals(ChocoRandom.class)){
                        generateRandom.set(true);
                        variable.set(field.getAnnotation(ChocoRandom.class).dynamic());
                        }
                    });

                field.setAccessible(true);

                if(field.getType().equals(String.class)){
                    field.set(vd, stringGenerator(generateRandom.get(), variable.get()));
                }
                else if(field.getType().equals(Integer.class)){
                    field.set(vd, integerGenerator(generateRandom.get(), variable.get()));
                }
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            value = objectMapper.writeValueAsString(vd);
        } catch (InstantiationException | IllegalAccessException
                 | ClassNotFoundException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return value;

    }

}
