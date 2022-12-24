package org.example.core;

import org.example.bean.postman.*;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.Method;
import java.util.List;
import static org.example.bean.HTTPVerbs.*;

public class SpringControllerClassReader {

    public void handleMappings(Method method, Item item, String baseUrl) {
        if (method.isAnnotationPresent(PostMapping.class)) {
            //set method
            item.getRequest().setMethod(POST);
            item.getRequest().setUrl(new Url(baseUrl + method.getAnnotation(PostMapping.class).value()[0]));

            //add paths
            item.getRequest().getUrl().getPath().addAll(
                        List.of(method.getAnnotation(PostMapping.class).value()[0].split("/"))
                );

        } else if (method.isAnnotationPresent(GetMapping.class)) {
            //set method
            item.getRequest().setMethod(GET);
            item.getRequest().setUrl(new Url(baseUrl + method.getAnnotation(GetMapping.class).value()[0]));

            //add paths
            item.getRequest().getUrl().getPath().addAll(
                    List.of(method.getAnnotation(GetMapping.class).value()[0].split("/"))
            );
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            //set method
            item.getRequest().setMethod(PUT);
            item.getRequest().setUrl(new Url(baseUrl + method.getAnnotation(PutMapping.class).value()[0]));

            //add paths
            item.getRequest().getUrl().getPath().addAll(
                    List.of(method.getAnnotation(PutMapping.class).value()[0].split("/"))
            );
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            //set method
            item.getRequest().setMethod(DELETE);
            item.getRequest().setUrl(new Url(baseUrl + method.getAnnotation(DeleteMapping.class).value()[0]));

            //add paths
            item.getRequest().getUrl().getPath().addAll(
                    List.of(method.getAnnotation(DeleteMapping.class).value()[0].split("/"))
            );
        }
    }
}
