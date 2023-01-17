
package io.chocotea.core.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Inherited
public @interface JavaxCollection {


    String name() default "Sample Collection";
    boolean createTest() default false;
    String baseUrl() default "{{BASE_URL}}";
    String protocol() default "https";
}
