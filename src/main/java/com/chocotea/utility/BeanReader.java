package com.chocotea.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.chocotea.bean.postman.DynamicVariables;
import com.chocotea.core.annotations.ChocoRandom;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.chocotea.utility.RandomGenerator.integerGenerator;
import static com.chocotea.utility.RandomGenerator.stringGenerator;

public class BeanReader {

    public static String generate(TypeMirror typeMirror){

        AtomicReference<String> value = new AtomicReference<>();
        AtomicBoolean generateRandom = new AtomicBoolean(false);
        AtomicReference<DynamicVariables> variable = new AtomicReference<>();

        TypeSpec.Builder tempClass = TypeSpec.classBuilder("Temp")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .build().toBuilder();

        JavaFile javaFile = JavaFile.builder(typeMirror.toString(), tempClass.build())
                .build();

        if (typeMirror instanceof DeclaredType) {
            if (((DeclaredType) typeMirror).asElement() instanceof TypeElement) {

                //loop through fields
                (((DeclaredType) typeMirror).asElement()).getEnclosedElements().stream().skip(1).forEach(element -> {

                    if(element.getAnnotation(ChocoRandom.class) != null){
                        generateRandom.set(true);
                        variable.set(element.getAnnotation(ChocoRandom.class).dynamic());
                    }

                    tempClass.addField(TypeName.get(element.asType()),
                            element.getSimpleName().toString(),
                            Modifier.PRIVATE);

                    //TODO: if inner class

                });
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            value.set(objectMapper.writeValueAsString(javaFile));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return value.get();
    }


}
