package com.chocotea.core.annotations;

import com.chocotea.bean.Library;
import org.springframework.core.annotation.AliasFor;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Collections(library = {Library.spring})
public @interface SpringCollection {
    @AliasFor(
            annotation = Collections.class
    )
    String name() default "Sample Collection";
    @AliasFor(
            annotation = Collections.class
    )
    boolean createTest() default false;
    @AliasFor(
            annotation = Collections.class
    )
    String baseUrl() default "{{BASE_URL}}";
    @AliasFor(
            annotation = Collections.class
    )
    String jsonPath() default "/resources/postman-collection.json";
    @AliasFor(
            annotation = Collections.class
    )
    String protocol() default "https";
}
