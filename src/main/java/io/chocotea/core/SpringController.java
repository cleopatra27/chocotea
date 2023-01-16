package io.chocotea.core;

import io.chocotea.bean.postman.Header;
import io.chocotea.bean.postman.Item;
import io.chocotea.bean.postman.Query;
import io.chocotea.bean.postman.Url;

import javax.lang.model.element.AnnotationMirror;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static io.chocotea.bean.HTTPVerbs.*;

public class SpringController implements Controller {

    @Override
    public void handleMappings(List<? extends AnnotationMirror> methodAnnotations, Item item, String baseUrl) {
        methodAnnotations.forEach(annotationMirror -> {
            if (annotationMirror.getAnnotationType().asElement().getSimpleName().toString().equals("PostMapping")) {
                //set method
                item.getRequest().setMethod(POST);
                item.getRequest().setUrl(new Url(baseUrl + getValue(annotationMirror, "value")
                        .substring(1, getValue(annotationMirror, "value").length() - 1)));

                //add paths
                item.getRequest().getUrl().getPath().addAll(List.of(getValue(annotationMirror, "value")
                        .substring(1, getValue(annotationMirror, "value").length() - 1).split("/")));

            } else if (annotationMirror.getAnnotationType().asElement().getSimpleName().toString().equals("GetMapping")) {
                //set method
                item.getRequest().setMethod(GET);
                item.getRequest().setUrl(new Url(baseUrl + getValue(annotationMirror, "value")
                        .substring(1, getValue(annotationMirror, "value").length() - 1)));

                //add paths
                item.getRequest().getUrl().getPath().addAll(List.of(getValue(annotationMirror, "value")
                        .substring(1, getValue(annotationMirror, "value").length() - 1).split("/")));

            } else if (annotationMirror.getAnnotationType().asElement().getSimpleName().toString().equals("PutMapping")) {
                //set method
                item.getRequest().setMethod(PUT);
                item.getRequest().setUrl(new Url(baseUrl + getValue(annotationMirror, "value")
                        .substring(1, getValue(annotationMirror, "value").length() - 1)));

                //add paths
                item.getRequest().getUrl().getPath().addAll(List.of(getValue(annotationMirror, "value")
                        .substring(1, getValue(annotationMirror, "value").length() - 1).split("/")));

            } else if (annotationMirror.getAnnotationType().asElement().getSimpleName().toString().equals("DeleteMapping")) {
                //set method
                item.getRequest().setMethod(DELETE);
                item.getRequest().setUrl(new Url(baseUrl + getValue(annotationMirror, "value")
                        .substring(1, getValue(annotationMirror, "value").length() - 1)));

                //add paths
                item.getRequest().getUrl().getPath().addAll(List.of(getValue(annotationMirror, "value")
                        .substring(1, getValue(annotationMirror, "value").length() - 1)
                        .split("/")));
            }

        });
    }

    //TODO: handle urlencoded body
    public void handleMultiValueMap() {

    }

    @Override
    public void handleHeaderParameters(List<List<? extends AnnotationMirror>> parameterAnnotations, Item item) {
        parameterAnnotations.forEach(annotationMirror -> annotationMirror.forEach(annotation -> {
            AtomicReference<String> head = new AtomicReference<>("");
            if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals("RequestHeader")) {
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

                if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals("RequestParam")) {

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
                        if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals("PathVariable")) {
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
