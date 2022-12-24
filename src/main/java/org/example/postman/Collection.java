package org.example.postman;

import org.example.core.ControllerClassReader;
import org.example.core.SpringControllerClassReader;
import org.example.core.annotations.SpringCollection;
import org.example.core.annotations.SpringRequest;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

@SupportedAnnotationTypes({"org.example.core.annotations.SpringCollection",
        "org.example.core.annotations.JavaxCollection"})
@SupportedSourceVersion(SourceVersion.RELEASE_19)
public class Collection extends AbstractProcessor {
    private org.example.bean.postman.Collection collection;

    private String create(){
        return null;
    }

    private String update(){
        return null;
    }

    public boolean handle(Method[] methods, Class<?> clas){
        collection = new org.example.bean.postman.Collection(clas.getAnnotation(SpringCollection.class).name());
        for (Method method: clas.getDeclaredMethods()) {
            if(method.isAnnotationPresent(SpringRequest.class)){
                System.out.println(Arrays.stream(method.getDeclaredAnnotations()).toList());
                new ControllerClassReader(
                        method,
                        collection,
                        clas).read();

                System.out.println(collection.toString());
            }
        }
        return false;
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
