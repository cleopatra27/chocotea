package io.chocotea.core.annotations;

import io.chocotea.bean.postman.Auth;
import io.chocotea.bean.postman.Language;
import io.chocotea.utility.DefaultClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface JakartaRequest {
    String name() default "Sample Request";
    Language language() default Language.json;
    Auth.Type auth() default Auth.Type.noauth;
    String[] authValue() default "";
    Class<?> request() default DefaultClass.class;
    Class<?> response() default DefaultClass.class;
}
