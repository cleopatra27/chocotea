package com.chocotea.core;

import com.chocotea.bean.postman.*;
import com.chocotea.core.annotations.ChocoRandom;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.chocotea.bean.HTTPVerbs.*;
import static com.chocotea.bean.HTTPVerbs.DELETE;

public class JavaxControllerClassReader extends ControllerReader {

    public JavaxControllerClassReader(Method method, Collection collection, Class<?> clazz) {
        super(method, collection, clazz, false);
    }

    @Override
    public void handleMappings(Method method, Item item, String baseUrl) {
        System.out.println(Arrays.toString(method.getDeclaredAnnotations()));
        if (method.isAnnotationPresent(jakarta.ws.rs.POST.class)) {
            //set method
            item.getRequest().setMethod(POST);
        } else if (method.isAnnotationPresent(jakarta.ws.rs.GET.class)) {
            //set method
            item.getRequest().setMethod(GET);
        } else if (method.isAnnotationPresent(jakarta.ws.rs.PUT.class)) {
            //set method
            item.getRequest().setMethod(PUT);
        } else if (method.isAnnotationPresent(jakarta.ws.rs.DELETE.class)) {
            //set method
            item.getRequest().setMethod(DELETE);
        }else{
            throw new RuntimeException("Missing mapping");
        }

        //add url
        item.getRequest().setUrl(new Url(baseUrl + method.getAnnotation(jakarta.ws.rs.Path.class).value()));

        //add paths
        item.getRequest().getUrl().getPath().addAll(
                List.of(method.getAnnotation(jakarta.ws.rs.Path.class).value().split("/"))
        );

    }

    @Override
    public void handleHeaderParameters(Method method, Item item) {
        Arrays.stream(method.getParameterAnnotations()).forEach(n ->
                {
                    Arrays.stream(n).forEach(header -> {
                        AtomicReference<String> head = new AtomicReference<>("");
                        if (header.annotationType().equals(HeaderParam.class)) {

                            //if ChocoRandom; handle
                            Arrays.stream(n).forEach(val -> {
                                if (val.annotationType().equals(ChocoRandom.class)) {
                                    head.set("{{$" + ((ChocoRandom) val).dynamic() + "}}");
                                }
                            });

                            item.getRequest().getHeader().add(new Header(((HeaderParam) header).value(), head.get()));
                        }
                    });
                }
        );
    }

    @Override
    public void handleQueryParameters(Method method, Item item) {
        final String[] query = {"?"};
        Arrays.stream(method.getParameterAnnotations()).forEach(n ->
                {
                    final String concatenator = "&";
                    Arrays.stream(n).forEach(qury -> {
                        AtomicReference<String> param = new AtomicReference<>("");

                        if (qury.annotationType().equals(QueryParam.class)) {

                            //if ChocoRandom; handle
                            Arrays.stream(n).forEach(val -> {
                                if (val.annotationType().equals(ChocoRandom.class)) {
                                    param.set("{{$" + ((ChocoRandom) val).dynamic() + "}}");
                                }
                            });

                            item.getRequest().getUrl().getQuery().add(new Query(((QueryParam) qury).value(), param.get()));

                            if (query[0].equals("?")) {
                                query[0] = query[0] + ((QueryParam) qury).value() + param + "=";
                            } else {
                                query[0] = query[0] + concatenator
                                        + ((QueryParam) qury).value() + param + "=";
                            }
                        }
                    });
                }
        );
    }

    @Override
    public void handlePathParameters(Method method, Item item) {
        Arrays.stream(method.getParameterAnnotations()).forEach(n ->
                {
                    Arrays.stream(n).forEach(param -> {
                        if (param.annotationType().equals(PathParam.class)) {
                            //if ChocoRandom; handle
                            Arrays.stream(n).forEach(val -> {
                                if(val.annotationType().equals(ChocoRandom.class)){

                                    List<String> paths = item.getRequest().getUrl().getPath();
                                    //get variable with name from list and replace
                                    for (int i = 0; i < paths.size() ; i++) {
                                        if(paths.get(i).contains(((PathParam) param).value())){
                                            paths.set(i, "{{$"+((ChocoRandom) val).dynamic() + "}}");
                                        }
                                    }

                                }
                            });
                        }
                    });
                }
        );
    }
}

