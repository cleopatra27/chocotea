package com.chocotea.core.annotations;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
public @interface ChocoCurrencyTest {
    String[] accept() default "";
    String[] block() default "";
}
