package com.chocotea.core.annotations;

import com.chocotea.bean.Library;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Collections {

    @AliasFor("name")
    String name() default "Sample Collection";
    @AliasFor("library")
    Library[] library() default {};
    @AliasFor("createTest")
    boolean createTest() default false;

    @AliasFor("baseUrl")
    String baseUrl() default "{{BASE_URL}}";

    @AliasFor("jsonPath")
    String jsonPath() default "/resources/postman-collection.json";
    @AliasFor("protocol")
    String protocol() default "https";
}
