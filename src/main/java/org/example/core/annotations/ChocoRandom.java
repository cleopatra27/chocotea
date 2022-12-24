package org.example.core.annotations;

import org.example.bean.postman.DynamicVariables;

import java.lang.annotation.*;

import static org.example.bean.postman.DynamicVariables.none;

/**
 * Used to specify fields they would like to be generated at runtime in postamn.
 * This is done using the pre-request scripts on postman.
 * The field type is used to generate this.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ChocoRandom {
    DynamicVariables dynamic() default none;
}
