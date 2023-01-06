package com.chocotea.core;

import com.chocotea.bean.postman.Collection;
import com.chocotea.bean.postman.Item;
import com.chocotea.core.annotations.JavaxCollection;
import com.chocotea.core.annotations.JavaxRequest;
import com.chocotea.core.annotations.SpringCollection;
import com.chocotea.core.annotations.SpringRequest;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes({"com.chocotea.core.annotations.SpringCollection",
        "com.chocotea.core.annotations.JavaxCollection"})
@SupportedSourceVersion(SourceVersion.RELEASE_19)
//@AutoService(Processor.class)
public class CollectionProcessor extends AbstractProcessor {
    private Collection collection;
    public Item item;
    private Item subTestFolder;
    private Item requestFolder = new Item("REQUESTS");
    private Item testFolder  = new Item("TESTS");
    private List<Item> testItems;

    public boolean handle(Class<?> clas) throws IOException {
        requestFolder.setItem(new ArrayList<>());
        testFolder.setItem(new ArrayList<>());
        if(clas.isAnnotationPresent(SpringCollection.class)) {
            collection = new com.chocotea.bean.postman.Collection(clas.getAnnotation(SpringCollection.class).name());
            for (Method method : clas.getDeclaredMethods()) {
                if (method.isAnnotationPresent(SpringRequest.class)) {
                    this.item = new Item(method.getName());

                    if(clas.getAnnotation(SpringCollection.class).createTest()) {
                        this.subTestFolder = new Item(method.getName());
                        testItems = new ArrayList<>();
                    }
                    new SpringControllerClassReader(
                            method,
                            collection,
                            clas,
                            this.item,
                            testItems).read();

                    requestFolder.getItem().add(item);

                    if(clas.getAnnotation(SpringCollection.class).createTest()) {
                        subTestFolder.setItem(testItems);
                        testFolder.getItem().add(subTestFolder);
                    }else{
                        this.testFolder = null;
                    }
                }
            }
            collection.getItem().add(requestFolder);
            collection.getItem().add(this.testFolder);
            System.out.println(collection.toString());
            return true;
        }else{
            collection = new com.chocotea.bean.postman.Collection(clas.getAnnotation(JavaxCollection.class).name());
            for (Method method : clas.getDeclaredMethods()) {
                System.out.println(Arrays.stream(method.getDeclaredAnnotations()).toList());
                if (method.isAnnotationPresent(JavaxRequest.class)) {
                    new jakartaControllerClassReader(
                            method,
                            collection,
                            clas,
                            requestFolder,
                            testItems).read();

                    collection.getItem().add(requestFolder);
                    System.out.println(collection.toString());
                }
            }
            return true;
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
            for (Element element : roundEnv.getRootElements()) {

            //processingEnv.getMessager().printError("hello sir");
//            System.out.println("hello");
            System.out.printf("data %s = %s (%s)\n", element.getSimpleName(), element.asType(), element.asType().getKind());
//                try {
//                    handle(Class.forName(element.getSimpleName().toString()));
//                } catch (IOException | ClassNotFoundException e) {
//                    throw new RuntimeException(e);
//                }

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
