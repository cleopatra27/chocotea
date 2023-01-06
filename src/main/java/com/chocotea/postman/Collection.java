package com.chocotea.postman;

import com.chocotea.core.JavaxControllerClassReader;
import com.chocotea.core.SpringControllerClassReader;
import com.chocotea.core.annotations.JavaxCollection;
import com.chocotea.core.annotations.JavaxRequest;
import com.chocotea.core.annotations.SpringCollection;
import com.chocotea.core.annotations.SpringRequest;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

@SupportedAnnotationTypes({"com.chocotea.core.annotations.SpringCollection",
        "com.chocotea.core.annotations.JavaxCollection"})
@SupportedSourceVersion(SourceVersion.RELEASE_19)
public class Collection extends AbstractProcessor {
    private com.chocotea.bean.postman.Collection collection;

    public boolean handle(Class<?> clas) throws IOException {
        if(clas.isAnnotationPresent(SpringCollection.class)) {
            collection = new com.chocotea.bean.postman.Collection(clas.getAnnotation(SpringCollection.class).name());
            for (Method method : clas.getDeclaredMethods()) {
                if (method.isAnnotationPresent(SpringRequest.class)) {
                    System.out.println(Arrays.stream(method.getDeclaredAnnotations()).toList());
                    new SpringControllerClassReader(
                            method,
                            collection,
                            clas).read();
                    System.out.println(collection.toString());
                }
            }
            return false;
        }else{
            collection = new com.chocotea.bean.postman.Collection(clas.getAnnotation(JavaxCollection.class).name());
            for (Method method : clas.getDeclaredMethods()) {
                System.out.println(Arrays.stream(method.getDeclaredAnnotations()).toList());
                if (method.isAnnotationPresent(JavaxRequest.class)) {
                    new JavaxControllerClassReader(
                            method,
                            collection,
                            clas).read();
                    System.out.println(collection.toString());
                }
            }
            return false;
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getRootElements()) {
            System.out.printf("%s = %s (%s)\n", element.getSimpleName(), element.asType(), element.asType().getKind());
//        for (TypeElement annotation : annotations) {
//                Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
//
//                //if spring annotation or javax annotation
//            Map<Boolean, List<Element>> annotatedMethods = annotatedElements.stream().collect(
//                    Collectors.partitioningBy(element ->
//                            ((ExecutableType) element.asType()).getParameterTypes().size() == 1
//                                    && element.
//                                    //element.getSimpleName().toString().startsWith("set")
//                    ));
//
//            List<Element> postMappings = annotatedMethods.get(true);
//            List<Element> otherMethods = annotatedMethods.get(false);

            //if spring -> SpringControllerClassReader

            //if javax - > JavaxControllerClassReader
        }
        return false;
    }
}
