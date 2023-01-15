package com.chocotea.core;

import com.chocotea.bean.postman.Collection;
import com.chocotea.bean.postman.Environment;
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
@SupportedSourceVersion(SourceVersion.RELEASE_19)
@AutoService(Processor.class)
public class CollectionProcessor extends AbstractProcessor{
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
    private Set<? extends Element> myAnnotationMethods = new HashSet<>();

    private Environment environment;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

            //loop through annotations
            for (TypeElement annotation : annotations) {
                requestFolder.setItem(new ArrayList<>());
                testFolder.setItem(new ArrayList<>());

                //populate local variables from class annotation
                populateLocalVar(annotation, roundEnv);

                //create new collection with defined collection name
                collection = new com.chocotea.bean.postman.Collection(name);

                setupEnvironment();

                //get method annotations
                getMethodAnnotations(annotation, roundEnv);

                //loop through method annotations and populate request
                for (Element myAnnotationMethod : myAnnotationMethods) {

                    //get the choco request annotation
                    getRequestAnnotation(annotation, myAnnotationMethod);

                    //create request folder
                    createRequestFolder(annotation, myAnnotationMethod);

                    createTestFolder(myAnnotationMethod);

                    //confirm annotation was put on a method
                    if (myAnnotationMethod.getKind() == ElementKind.METHOD) {
                        for (VariableElement variableElement : ((ExecutableElement) myAnnotationMethod).getParameters()) {
                            parameterAnnotations.add(variableElement.getAnnotationMirrors());
                        }
                    } //else?

                    //populate collection with builder
                    com.chocotea.core.Collection.builder(myAnnotationMethod.getAnnotationMirrors(), parameterAnnotations,
                                    requestAnnotation, protocol,  item, testItems)
                            .setLanguage()
                            .setHost()
                            .setProtocol()
                            .setBean()
                            .setAuth()
                            .createTests(test)
                            .build();

                    //if test is enabled, create and populate request test folder
                    populateFolders();

                }
                collection.getItem().add(requestFolder);
                collection.getItem().add(this.testFolder);

                //save item in folder
                writeCollectionToFile();
            }


        return true;
    }

    private void setupEnvironment() {
        //create environment
        environment = new Environment(name);

        //set base url
        environment.getValues().add(new Environment.Values(
                "BaseUrl", baseUrl, "default", true));
    }

    private void createRequestFolder(TypeElement annotation, Element myAnnotationMethod) {

        if (annotation.getSimpleName().contentEquals("SpringCollection")) {
            this.item = new Item(!Objects.equals(((SpringRequest) requestAnnotation).name(),
                    "Sample Request") ?
                    ((SpringRequest) requestAnnotation).name() :
                    myAnnotationMethod.getSimpleName().toString());
        } else if (annotation.getSimpleName().contentEquals("JavaxCollection")) {
            this.item = new Item(!Objects.equals(((JavaxRequest) requestAnnotation).name(),
                    "Sample Request") ?
                    ((JavaxRequest) requestAnnotation).name() :
                    myAnnotationMethod.getSimpleName().toString());
        }else if (annotation.getSimpleName().contentEquals("JakartaCollection")) {
            this.item = new Item(!Objects.equals(((JakartaRequest) requestAnnotation).name(),
                    "Sample Request") ?
                    ((JakartaRequest) requestAnnotation).name() :
                    myAnnotationMethod.getSimpleName().toString());
        }
    }

    private void getRequestAnnotation(TypeElement annotation, Element myAnnotationMethod) {
        if (annotation.getSimpleName().contentEquals("SpringCollection")) {
            requestAnnotation = myAnnotationMethod.getAnnotation(SpringRequest.class);
        } else if (annotation.getSimpleName().contentEquals("JavaxCollection")) {
            requestAnnotation = myAnnotationMethod.getAnnotation(JavaxRequest.class);
        }else if (annotation.getSimpleName().contentEquals("JakartaCollection")) {
            requestAnnotation = myAnnotationMethod.getAnnotation(JakartaRequest.class);
        }
    }

    private void getMethodAnnotations(TypeElement annotation, RoundEnvironment roundEnv) {
        if (annotation.getSimpleName().contentEquals("SpringCollection")) {
            myAnnotationMethods = roundEnv.getElementsAnnotatedWith(SpringRequest.class);
        } else if (annotation.getSimpleName().contentEquals("JavaxCollection")) {
            myAnnotationMethods = roundEnv.getElementsAnnotatedWith(JavaxRequest.class);
        }else if (annotation.getSimpleName().contentEquals("JakartaCollection")) {
            myAnnotationMethods = roundEnv.getElementsAnnotatedWith(JakartaRequest.class);
        }
    }

    private void populateLocalVar(TypeElement annotation, RoundEnvironment roundEnv) {
        if(annotation.getSimpleName().contentEquals("SpringCollection")){
            Set<? extends Element> annotatedClasses = roundEnv.getElementsAnnotatedWith(SpringCollection.class);
            for (Element annotatedClass : annotatedClasses) {
                baseUrl = annotatedClass.getAnnotation(SpringCollection.class).baseUrl();
                name = annotatedClass.getAnnotation(SpringCollection.class).name();
                test = annotatedClass.getAnnotation(SpringCollection.class).createTest();
                protocol = annotatedClass.getAnnotation(SpringCollection.class).protocol();
            }
        }else if (annotation.getSimpleName().contentEquals("JavaxCollection")) {
            Set<? extends Element> annotatedClasses = roundEnv.getElementsAnnotatedWith(JavaxCollection.class);
            for (Element annotatedClass : annotatedClasses) {
                baseUrl = annotatedClass.getAnnotation(JavaxCollection.class).baseUrl();
                name = annotatedClass.getAnnotation(JavaxCollection.class).name();
                test = annotatedClass.getAnnotation(JavaxCollection.class).createTest();
                protocol = annotatedClass.getAnnotation(JavaxCollection.class).protocol();
            }
        }else if (annotation.getSimpleName().contentEquals("JakartaCollection")) {
            Set<? extends Element> annotatedClasses = roundEnv.getElementsAnnotatedWith(JakartaCollection.class);
            for (Element annotatedClass : annotatedClasses) {
                baseUrl = annotatedClass.getAnnotation(JakartaCollection.class).baseUrl();
                name = annotatedClass.getAnnotation(JakartaCollection.class).name();
                test = annotatedClass.getAnnotation(JakartaCollection.class).createTest();
                protocol = annotatedClass.getAnnotation(JakartaCollection.class).protocol();
            }
        }

    }

    private void populateFolders() {
        requestFolder.getItem().add(item);
        if (test) {
            subTestFolder.setItem(testItems);
            testFolder.getItem().add(subTestFolder);
        } else {
            this.testFolder = null;
        }
    }

    private void createTestFolder(Element myAnnotationMethod) {

        if(test){
            this.subTestFolder = new Item(myAnnotationMethod.getSimpleName().toString());
            testItems = new ArrayList<>();
        }
    }

    private void writeCollectionToFile(){
        try {
            Writer writer = processingEnv.getFiler().createResource(
                    StandardLocation.SOURCE_OUTPUT, "",
                    name.replaceAll("\\s+", "_").toLowerCase()+".json").openWriter();
            writer.write(collection.toString());
            writer.close();

             writer = processingEnv.getFiler().createResource(
                    StandardLocation.SOURCE_OUTPUT, "",
                    name.replaceAll("\\s+", "_").toLowerCase()+"_environment.json").openWriter();
            writer.write(environment.toString());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

