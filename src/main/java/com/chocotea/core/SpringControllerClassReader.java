//package com.chocotea.core;
//
//import com.chocotea.bean.postman.*;
//import com.chocotea.core.annotations.ChocoRandom;
//import org.springframework.web.bind.annotation.*;
//
//import javax.lang.model.element.AnnotationMirror;
//import java.lang.annotation.Annotation;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.atomic.AtomicReference;
//
//import static com.chocotea.bean.HTTPVerbs.*;
//
//public class SpringControllerClassReader extends ControllerReader{
//
//    public SpringControllerClassReader(
//            Set<List<? extends AnnotationMirror>> methodAnnotations,
//            Set<List<? extends  AnnotationMirror>> parameterAnnotations,
//            Collection collection, Annotation requestAnnotation, String baseUrl,
//            boolean createTest, String protocol, Item item, List<Item> testItems) {
//
//        super(methodAnnotations, parameterAnnotations, collection, requestAnnotation, baseUrl, createTest,
//                protocol, true, item, testItems);
//
//    }
//
//    public String getValue(AnnotationMirror annotationMirror){
//        return annotationMirror.getElementValues().keySet()
//                .stream().filter(executableElement -> executableElement.getSimpleName()
//                        .contentEquals("value"))
//                 .map(executableElement -> executableElement.getDefaultValue().getValue().toString()).toString();
//    }
//    @Override
//    public void handleMappings(Set<List<? extends AnnotationMirror>> methodAnnotations, Item item, String baseUrl) {
//        methodAnnotations.forEach(annotList -> annotList.forEach(annotationMirror -> {
//            if(annotationMirror.getAnnotationType().asElement().getSimpleName().toString().equals("PostMapping")){
//                //set method
//                item.getRequest().setMethod(POST);
//
//            } else if(annotationMirror.getAnnotationType().asElement().getSimpleName().toString().equals("GetMapping")){
//                //set method
//                item.getRequest().setMethod(GET);
//
//            } else if(annotationMirror.getAnnotationType().asElement().getSimpleName().toString().equals("PutMapping")){
//                //set method
//                item.getRequest().setMethod(PUT);
//
//            } else if(annotationMirror.getAnnotationType().asElement().getSimpleName().toString().equals("DeleteMapping")){
//                //set method
//                item.getRequest().setMethod(DELETE);
//            }
//
//            item.getRequest().setUrl(new Url(baseUrl + getValue(annotationMirror)));
//
//            //add paths
//            item.getRequest().getUrl().getPath().addAll(List.of(getValue(annotationMirror).split("/")));
//        }));
//    }
//
//
////    @Override
////    public void handleMappings(Method method, Item item, String baseUrl) {
////        if (method.isAnnotationPresent(PostMapping.class)) {
////
////            //set method
////            item.getRequest().setMethod(POST);
////            item.getRequest().setUrl(new Url(baseUrl + method.getAnnotation(PostMapping.class).value()[0]));
////
////            //add paths
////            item.getRequest().getUrl().getPath().addAll(
////                        List.of(method.getAnnotation(PostMapping.class).value()[0].split("/"))
////                );
////
////        } else if (method.isAnnotationPresent(GetMapping.class)) {
////            //set method
////            item.getRequest().setMethod(GET);
////            item.getRequest().setUrl(new Url(baseUrl + method.getAnnotation(GetMapping.class).value()[0]));
////
////            //add paths
////            item.getRequest().getUrl().getPath().addAll(
////                    List.of(method.getAnnotation(GetMapping.class).value()[0].split("/"))
////            );
////        } else if (method.isAnnotationPresent(PutMapping.class)) {
////            //set method
////            item.getRequest().setMethod(PUT);
////            item.getRequest().setUrl(new Url(baseUrl + method.getAnnotation(PutMapping.class).value()[0]));
////
////            //add paths
////            item.getRequest().getUrl().getPath().addAll(
////                    List.of(method.getAnnotation(PutMapping.class).value()[0].split("/"))
////            );
////        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
////            //set method
////            item.getRequest().setMethod(DELETE);
////            item.getRequest().setUrl(new Url(baseUrl + method.getAnnotation(DeleteMapping.class).value()[0]));
////
////            //add paths
////            item.getRequest().getUrl().getPath().addAll(
////                    List.of(method.getAnnotation(DeleteMapping.class).value()[0].split("/"))
////            );
////        }
////    }
//
//    //TODO: handle urlencoded body
//    public void handleMultiValueMap(){
//
//    }
//    @Override
//    public void handleHeaderParameters(Set<List<? extends AnnotationMirror>> parameterAnnotations, Item item) {
//
//        parameterAnnotations.forEach(annotList -> annotList.forEach(annotationMirror -> {
//            if(annotationMirror.getAnnotationType().asElement().getSimpleName().toString().equals("RequestHeader")){
//                       // if (header.annotationType().equals(RequestHeader.class)) {
//
//                            //if ChocoRandom; handle
////                            Arrays.stream(n).forEach(val -> {
//                                if(annotationMirror.getAnnotationType().asElement()
//                                        .getSimpleName().toString().equals("ChocoRandom")){
//                                    head.set("{{$"+((ChocoRandom) val).dynamic() + "}}");
//                                }
//                           // });
//
//                            item.getRequest().getHeader().add(new Header(((RequestHeader) header).value(), head.get()));
//                        }
//                    });
//                }
//        );
//    }
//
//    @Override
//    public void handleQueryParameters(Set<List<? extends AnnotationMirror>> parameterAnnotations, Item item) {
//        final String[] query = {"?"};
//        Arrays.stream(method.getParameterAnnotations()).forEach(n ->
//                {
//                    final String concatenator = "&";
//                    Arrays.stream(n).forEach(qury -> {
//                        AtomicReference<String> param = new AtomicReference<>("");
//
//                        if (qury.annotationType().equals(RequestParam.class)) {
//
//                            //if ChocoRandom; handle
//                            Arrays.stream(n).forEach(val -> {
//                                if(val.annotationType().equals(ChocoRandom.class)){
//                                    param.set("{{$"+((ChocoRandom) val).dynamic() + "}}");
//                                }
//                            });
//
//                            item.getRequest().getUrl().getQuery().add(new Query(((RequestParam) qury).value(), param.get()));
//
//                            if (query[0].equals("?")) {
//                                query[0] = query[0] + ((RequestParam) qury).value() + param + "=";
//                            } else {
//                                query[0] = query[0] + concatenator
//                                        + ((RequestParam) qury).value() + param + "=";
//                            }
//                        }
//                    });
//                }
//        );
//
//        //add to url
//        item.getRequest().getUrl().setRaw(item.getRequest().getUrl().getRaw() + query[0]);
//    }
//
//    @Override
//    public void handlePathParameters(Set<List<? extends AnnotationMirror>> parameterAnnotations, Item item) {
//        Arrays.stream(method.getParameterAnnotations()).forEach(n ->
//                {
//                    Arrays.stream(n).forEach(param -> {
//                        if (param.annotationType().equals(PathVariable.class)) {
//                            //if ChocoRandom; handle
//                            Arrays.stream(n).forEach(val -> {
//                                if(val.annotationType().equals(ChocoRandom.class)){
//
//                                    List<String> paths = item.getRequest().getUrl().getPath();
//                                    //get variable with name from list and replace
//                                    for (int i = 0; i < paths.size() ; i++) {
//                                        if(paths.get(i).contains(((PathVariable) param).value())){
//                                            paths.set(i, "{{$"+((ChocoRandom) val).dynamic() + "}}");
//                                        }
//                                    }
//
//                                }
//                            });
//                        }
//                    });
//                }
//        );
//    }
//}
