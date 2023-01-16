package io.chocotea.core.annotations;

import io.chocotea.bean.postman.DynamicVariables;

import java.lang.annotation.*;

import static io.chocotea.bean.postman.DynamicVariables.none;

/**
 * Used to specify fields they would like to be generated at runtime in postamn.
 * This is done using the pre-request scripts on postman.
 * The field type is used to generate this.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
@Inherited
public @interface ChocoRandom {
    DynamicVariables dynamic() default none;
}
