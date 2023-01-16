package io.chocotea.core.annotations;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
@Inherited
public @interface ChocoResponseTest {
}
