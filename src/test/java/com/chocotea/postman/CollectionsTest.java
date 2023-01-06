package com.chocotea.postman;

import com.chocotea.core.annotations.SpringCollection;
import com.chocotea.utility.PathGenerator;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class CollectionsTest {

    @Test
    void process() {

        Annotation annotation = PathGenerator.class.getAnnotation(SpringCollection.class);

        if(annotation instanceof SpringCollection){
            SpringCollection info = (SpringCollection) annotation;

            assertEquals("ABC", info.name());
            assertEquals("1.1.1", info.name());
        } else {
            fail("Did not find annotation");
        }
    }

}