
package io.chocotea.core.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Inherited
public @interface JakartaCollection {


    String name() default "Sample Collection";
    boolean createTest() default false;
    String baseUrl() default "{{BASE_URL}}";
    String jsonPath() default "/resources/postman-collection.json";
    String protocol() default "https";
}
