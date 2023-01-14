package com.chocotea.utility;

import com.chocotea.bean.postman.DynamicVariables;
import com.chocotea.core.annotations.ChocoRandom;
import org.json.JSONObject;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.chocotea.utility.RandomGenerator.stringGenerator;

public class BeanReader {

    public static String generate(TypeMirror typeMirror){

        AtomicBoolean generateRandom = new AtomicBoolean(false);
        AtomicReference<DynamicVariables> variable = new AtomicReference<>();
        JSONObject bod = new JSONObject();

        if (typeMirror instanceof DeclaredType) {
            if (((DeclaredType) typeMirror).asElement() instanceof TypeElement) {

                //loop through fields
                (((DeclaredType) typeMirror).asElement()).getEnclosedElements().stream().skip(1).forEach(element -> {
                    if(element.getKind().isVariable()) {
                        if (element.getAnnotation(ChocoRandom.class) != null) {
                            generateRandom.set(true);
                            variable.set(element.getAnnotation(ChocoRandom.class).dynamic());
                        }

                        //TODO  switch between generators
                        bod.put(element.getSimpleName().toString(), stringGenerator(generateRandom.get(), variable.get()));

                        //TODO: if inner class

                    }
                });
            }
        }


        return bod.toString();
    }


}
