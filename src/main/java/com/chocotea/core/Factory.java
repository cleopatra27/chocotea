package com.chocotea.core;

import java.lang.annotation.Annotation;
import java.util.Optional;

public class Factory {
    public static Optional<Controller> getOperation(Annotation requestAnnotation) {
        if(requestAnnotation.annotationType().getSimpleName().contentEquals("SpringRequest")){
            return Optional.of(new SpringController());
        }else if(requestAnnotation.annotationType().getSimpleName().contentEquals("JavaxRequest")){
            return Optional.of(new JavaxController());
        }else if(requestAnnotation.annotationType().getSimpleName().contentEquals("JakartaRequest")){
            return Optional.of(new JakartaController());
        }else{
            return Optional.empty();
        }
    }
}
