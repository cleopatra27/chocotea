package org.example.core.annotations;

import org.example.bean.postman.Auth;
import org.example.bean.postman.Language;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.example.bean.postman.Auth.Type.noauth;
import static org.example.bean.postman.Language.json;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SpringRequest {

    //TODO; set default to method name
    String name() default "Sample Request";
    Language language() default json;
    Auth.Type auth() default noauth;
    String[] authValue() default "";
    //TODO: make optional
    Class<?>  requestBean();
}
