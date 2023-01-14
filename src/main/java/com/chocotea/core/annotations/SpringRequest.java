package com.chocotea.core.annotations;

import com.chocotea.bean.postman.Auth;
import com.chocotea.bean.postman.Language;
import com.chocotea.utility.DefaultClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.chocotea.bean.postman.Auth.Type.noauth;
import static com.chocotea.bean.postman.Language.json;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface SpringRequest {

    //TODO; set default to method name
    String name() default "Sample Request";
    Language language() default json;
    Auth.Type auth() default noauth;
    String[] authValue() default "";
    //TODO: make optional
    Class<?> request() default DefaultClass.class;
    Class<?> response() default DefaultClass.class;
}
