package org.example.postman;

import org.example.core.annotations.SpringCollection;
import org.example.utility.PathGenerator;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class CollectionTest {

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