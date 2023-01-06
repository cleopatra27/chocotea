package com.chocotea.core.annotations;

import com.chocotea.bean.Library;
import com.chocotea.bean.postman.Auth;
import com.chocotea.bean.postman.Language;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.chocotea.bean.postman.Auth.Type.noauth;
import static com.chocotea.bean.postman.Language.json;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Requests(library = {Library.spring})
public @interface SpringRequest {

    //TODO; set default to method name
    @AliasFor(
            annotation = Requests.class
    )
    String name() default "Sample Request";
    @AliasFor(
            annotation = Requests.class
    )
    Language language() default json;
    @AliasFor(
            annotation = Requests.class
    )
    Auth.Type auth() default noauth;
    @AliasFor(
            annotation = Requests.class
    )
    String[] authValue() default "";
    //TODO: make optional
    @AliasFor(
            annotation = Requests.class
    )
    Class<?>  requestBean();
}
