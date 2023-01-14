package com.chocotea.core;

import com.chocotea.bean.postman.Collection;
import com.chocotea.bean.postman.Item;
import com.chocotea.core.annotations.*;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.*;

@SupportedAnnotationTypes({"com.chocotea.core.annotations.SpringCollection",
        "com.chocotea.core.annotations.JavaxCollection",
        "com.chocotea.core.annotations.JakartaCollection"})
//@SupportedAnnotationTypes("com.chocotea.core.annotations.SpringCollection")
@SupportedSourceVersion(SourceVersion.RELEASE_19)
@AutoService(Processor.class)
public class CollectionProcessor extends AbstractProcessor {
    private Collection collection;
    public Item item;
    private Item subTestFolder;
    private Item requestFolder = new Item("REQUESTS");
    private Item testFolder = new Item("TESTS");
    private List<Item> testItems;

    private String baseUrl;
    private String protocol;
    private boolean test;
    private String name;

    private List<? extends AnnotationMirror> methodAnnotations;
    private List<List<? extends AnnotationMirror>> parameterAnnotations = new ArrayList<>();
    private Annotation requestAnnotation;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            requestFolder.setItem(new ArrayList<>());
            testFolder.setItem(new ArrayList<>());
            Set<? extends Element> myAnnotationMethods = new HashSet<>();

            Set<? extends Element> annotatedElements
                    = roundEnv.getElementsAnnotatedWith(annotation);

            //loop through annotated classes
            for (Element annotatedElem : annotatedElements) {

                if (annotatedElem.getSimpleName().contentEquals("SpringCollection")) {
                    //populate local variables from class annotation
                    Set<? extends Element> annotatedClasses = roundEnv.getElementsAnnotatedWith(SpringCollection.class);
                    for (Element annotatedClass : annotatedClasses) {
                        baseUrl = annotatedClass.getAnnotation(SpringCollection.class).baseUrl();
                        name = annotatedClass.getAnnotation(SpringCollection.class).name();
                        test = annotatedClass.getAnnotation(SpringCollection.class).createTest();
                        protocol = annotatedClass.getAnnotation(SpringCollection.class).protocol();
                    }

                    myAnnotationMethods = roundEnv.getElementsAnnotatedWith(SpringRequest.class);
                    for (Element myAnnotationMethod : myAnnotationMethods) {
                        requestAnnotation = myAnnotationMethod.getAnnotation(SpringRequest.class);

                        //set item name
                        this.item = new Item(!Objects.equals(((SpringRequest) requestAnnotation).name(), "Sample Request") ?
                                ((SpringRequest) requestAnnotation).name() :
                                myAnnotationMethod.getSimpleName().toString());
                    }
                } else if (annotation.getSimpleName().contentEquals("JavaxCollection")) {
                    //populate local variables from class annotation
                    Set<? extends Element> annotatedClasses = roundEnv.getElementsAnnotatedWith(JavaxCollection.class);
                    for (Element annotatedClass : annotatedClasses) {
                        baseUrl = annotatedClass.getAnnotation(JavaxCollection.class).baseUrl();
                        name = annotatedClass.getAnnotation(JavaxCollection.class).name();
                        test = annotatedClass.getAnnotation(JavaxCollection.class).createTest();
                        protocol = annotatedClass.getAnnotation(JavaxCollection.class).protocol();
                    }

                    myAnnotationMethods = roundEnv.getElementsAnnotatedWith(JavaxRequest.class);
                    for (Element myAnnotationMethod : myAnnotationMethods) {
                        requestAnnotation = myAnnotationMethod.getAnnotation(JavaxRequest.class);

                        //set item name
                        this.item = new Item(!Objects.equals(((JavaxRequest) requestAnnotation).name(), "Sample Request") ?
                                ((JavaxRequest) requestAnnotation).name() :
                                myAnnotationMethod.getSimpleName().toString());
                    }
                } else if (annotation.getSimpleName().contentEquals("JakartaCollection")) {
                    //populate local variables from class annotation
                    Set<? extends Element> annotatedClasses = roundEnv.getElementsAnnotatedWith(JakartaCollection.class);
                    for (Element annotatedClass : annotatedClasses) {
                        baseUrl = annotatedClass.getAnnotation(JakartaCollection.class).baseUrl();
                        name = annotatedClass.getAnnotation(JakartaCollection.class).name();
                        test = annotatedClass.getAnnotation(JakartaCollection.class).createTest();
                        protocol = annotatedClass.getAnnotation(JakartaCollection.class).protocol();
                    }

                    myAnnotationMethods = roundEnv.getElementsAnnotatedWith(JakartaRequest.class);
                    for (Element myAnnotationMethod : myAnnotationMethods) {
                        requestAnnotation = myAnnotationMethod.getAnnotation(JakartaRequest.class);

                        //set item name
                        this.item = new Item(!Objects.equals(((JakartaRequest) requestAnnotation).name(), "Sample Request") ?
                                ((JakartaRequest) requestAnnotation).name() :
                                myAnnotationMethod.getSimpleName().toString());
                    }
                }
            }

            collection = new com.chocotea.bean.postman.Collection(name);

            for (Element myAnnotationMethod : myAnnotationMethods) {
                methodAnnotations = myAnnotationMethod.getAnnotationMirrors();

                if (myAnnotationMethod.getKind() == ElementKind.METHOD) {
                    for (VariableElement variableElement : ((ExecutableElement) myAnnotationMethod).getParameters()) {
                        parameterAnnotations.add(variableElement.getAnnotationMirrors());
                    }
                }

                if (test) {
                    this.subTestFolder = new Item(myAnnotationMethod.getSimpleName().toString());
                    this.testItems = new ArrayList<>();
                    subTestFolder.setItem(testItems);
                    testFolder.getItem().add(subTestFolder);
                } else {
                    this.testFolder = null;
                }

                //build Collection with builder
                com.chocotea.core.Collection.builder(methodAnnotations, parameterAnnotations, requestAnnotation,
                                baseUrl, protocol, true, item, testItems)
                        .setLanguage()
                        .setHost()
                        .setProtocol()
                        .setBean()
                        .setAuth()
                        .createTests(test)
                        .build();

                requestFolder.getItem().add(item);
            }
            collection.getItem().add(requestFolder);
            collection.getItem().add(this.testFolder);
        }

        //save item in /resources folder
        writeCollectionToFile();
        return true;
    }

    private void writeCollectionToFile(){
        try {
            Writer writer = processingEnv.getFiler().createResource(
                    StandardLocation.SOURCE_OUTPUT, "", name+".json").openWriter();
            writer.write(collection.toString());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

