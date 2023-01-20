package io.chocotea.utility;

import io.chocotea.bean.postman.DynamicVariables;
import io.chocotea.core.annotations.ChocoRandom;
import org.json.JSONObject;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class BeanReader {

    public static String generate(TypeMirror typeMirror){
        AtomicBoolean generateRandom = new AtomicBoolean(false);
        AtomicReference<DynamicVariables> variable = new AtomicReference<>();
        JSONObject bod = new JSONObject();

        if (typeMirror instanceof DeclaredType) {
            if (((DeclaredType) typeMirror).asElement() instanceof TypeElement) {

                //loop through fields
                (((DeclaredType) typeMirror).asElement()).getEnclosedElements().stream().skip(1).forEach(element -> {
                    if(element.getKind().isField()) {
                        if (element.getAnnotation(ChocoRandom.class) != null) {
                            generateRandom.set(true);
                            variable.set(element.getAnnotation(ChocoRandom.class).dynamic());
                        }

                        bod.put(element.getSimpleName().toString(),
                                RandomGenerator.generate(generateRandom.get(),
                                        variable.get(), element.asType().toString())
                        );

                    }
                });
            }
        }


        return bod.toString();
    }


}
