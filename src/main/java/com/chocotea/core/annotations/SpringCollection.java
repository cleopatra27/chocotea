package com.chocotea.core.annotations;

import com.chocotea.bean.Library;
import org.springframework.core.annotation.AliasFor;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface SpringCollection {
    String name() default "Sample Collection";
    boolean createTest() default false;
    String baseUrl() default "{{BASE_URL}}";
    String jsonPath() default "/resources/postman-collection.json";
    String protocol() default "https";
}
