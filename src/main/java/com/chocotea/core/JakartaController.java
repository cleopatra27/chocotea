package com.chocotea.core;

import com.chocotea.bean.postman.Header;
import com.chocotea.bean.postman.Item;
import com.chocotea.bean.postman.Query;
import com.chocotea.bean.postman.Url;

import javax.lang.model.element.AnnotationMirror;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.chocotea.bean.HTTPVerbs.*;

public class JakartaController implements Controller {

    @Override
    public void handleMappings(List<? extends AnnotationMirror> methodAnnotations, Item item, String baseUrl) {
        methodAnnotations.forEach(annotationMirror -> {
            if (annotationMirror.getAnnotationType().asElement().getSimpleName().toString().equals("POST")) {
                //set method
                item.getRequest().setMethod(POST);
            } else if (annotationMirror.getAnnotationType().asElement().getSimpleName().toString().equals("GET")) {
                //set method
                item.getRequest().setMethod(GET);
            } else if (annotationMirror.getAnnotationType().asElement().getSimpleName().toString().equals("PUT")) {
                //set method
                item.getRequest().setMethod(PUT);
            }else if (annotationMirror.getAnnotationType().asElement().getSimpleName().toString().equals("DELETE")) {
                //set method
                item.getRequest().setMethod(DELETE);
            }
        });

        //add url
        methodAnnotations.forEach(annotationMirro -> {
            if (annotationMirro.getAnnotationType().asElement().getSimpleName().toString().equals("Path")) {
                item.getRequest().setUrl(new Url(baseUrl + getValue(annotationMirro, "value")));

                //add paths
                item.getRequest().getUrl().getPath().addAll(List.of(getValue(annotationMirro, "value").split("/")));
            }
        });

    }

    @Override
    public void handleHeaderParameters(List<List<? extends AnnotationMirror>> parameterAnnotations, Item item) {
        parameterAnnotations.forEach(annotationMirror -> annotationMirror.forEach(annotation -> {
            AtomicReference<String> head = new AtomicReference<>("");
            if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals("HeaderParam")) {
                annotationMirror.forEach(val -> {
                    //if ChocoRandom; handle
                    if (val.getAnnotationType().asElement().getSimpleName().toString().equals("ChocoRandom")) {
                        head.set("{{$" + getValue(val, "dynamic") + "}}");
                    }
                    item.getRequest().getHeader().add(new Header(getValue(annotation, "value"), head.get()));
                });
            }
        }));
    }

    @Override
    public void handleQueryParameters(List<List<? extends AnnotationMirror>> parameterAnnotations, Item item) {
        final String[] query = {"?"};
        parameterAnnotations.forEach(annotationMirror -> annotationMirror.forEach(annotation -> {
            {
                final String concatenator = "&";
                AtomicReference<String> param = new AtomicReference<>("");

                if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals("QueryParam")) {

                    annotationMirror.forEach(val -> {
                        //if ChocoRandom; handle
                        if (val.getAnnotationType().asElement().getSimpleName().toString().equals("ChocoRandom")) {
                            param.set("{{$" + getValue(val, "dynamic") + "}}");
                        }
                    });

                    item.getRequest().getUrl().getQuery().add(new Query(getValue(annotation, "value"), param.get()));

                    if (query[0].equals("?")) {
                        query[0] = query[0] + getValue(annotation, "value") + param + "=";
                    } else {
                        query[0] = query[0] + concatenator
                                + getValue(annotation, "value") + param + "=";
                    }
                }
            }
        }));

        //add to url
        item.getRequest().getUrl().setRaw(item.getRequest().getUrl().getRaw() + query[0]);
    }

    @Override
    public void handlePathParameters(List<List<? extends AnnotationMirror>> parameterAnnotations, Item item) {
        parameterAnnotations.forEach(annotationMirror -> annotationMirror.forEach(annotation -> {
                    {
                        if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals("PathParam")) {
                            //if ChocoRandom; handle
                            annotationMirror.forEach(val -> {
                                if (val.getAnnotationType().asElement().getSimpleName().toString().equals("ChocoRandom")) {
                                    List<String> paths = item.getRequest().getUrl().getPath();
                                    //get variable with name from list and replace
                                    for (int i = 0; i < paths.size(); i++) {
                                        if (paths.get(i).contains(getValue(annotation, "value"))) {
                                            paths.set(i, "{{$" + getValue(val, "dynamic") + "}}");
                                        }
                                    }

                                }
                            });
                        }
                    }
                }
        ));
    }
}

