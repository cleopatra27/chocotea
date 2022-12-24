package org.example.core.annotations;

import org.example.bean.postman.Auth;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
//TODO: reconfirm the policy here
@Retention(RetentionPolicy.RUNTIME)
public @interface SpringCollection {

    String name() default "Sample Collection";
    boolean createTest() default false;
    String baseUrl() default "{{BASE_URL}}";
    String jsonPath() default "/resources/postman-collection.json";
    String protocol() default "https";

//    AccessLevel value() default AccessLevel.PUBLIC;

    AnyAnnotation[] onMethod() default {};

    AnyAnnotation[] onParam() default {};


    @Deprecated
    @Retention(RetentionPolicy.SOURCE)
    @Target({})
    public @interface AnyAnnotation {
    }
}
