package com.chocotea.core;

import com.chocotea.bean.postman.Item;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import java.util.List;
import java.util.Map;

public interface Controller {
    default String getValue(AnnotationMirror annotationMirror, String key) {
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry
                : annotationMirror.getElementValues().entrySet()) {
            if (key.equals(entry.getKey().getSimpleName().toString())) {
                AnnotationValue annotationValue = entry.getValue();
                return annotationValue.getValue().toString();
            }
        }
        return null;
    }
     void handleMappings(List<? extends AnnotationMirror> methodAnnotations, Item item, String baseUrl);

    void handleHeaderParameters(List<List<? extends AnnotationMirror>> parameterAnnotations, Item item);

     void handleQueryParameters(List<List<? extends AnnotationMirror>> parameterAnnotations, Item item);

     void handlePathParameters(List<List<? extends AnnotationMirror>> parameterAnnotations, Item item);
}
