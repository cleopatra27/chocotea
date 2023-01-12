package com.chocotea.core;

import com.chocotea.bean.postman.Collection;
import com.chocotea.bean.postman.Item;
import com.chocotea.core.annotations.SpringCollection;
import com.chocotea.core.annotations.SpringRequest;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@SupportedAnnotationTypes({"com.chocotea.core.annotations.SpringCollection",
//        "com.chocotea.core.annotations.JavaxCollection"})
@SupportedAnnotationTypes("com.chocotea.core.annotations.SpringCollection")
@SupportedSourceVersion(SourceVersion.RELEASE_19)
@AutoService(Processor.class)
public class CollectionProcessor extends AbstractProcessor {
    private Collection collection;
    public Item item;
    private Item subTestFolder;
    private Item requestFolder = new Item("REQUESTS");
    private Item testFolder  = new Item("TESTS");
    private List<Item> testItems;

//    public boolean handle(Class<?> clas) throws IOException {
//        requestFolder.setItem(new ArrayList<>());
//        testFolder.setItem(new ArrayList<>());
//        if(clas.isAnnotationPresent(SpringCollection.class)) {
//            collection = new com.chocotea.bean.postman.Collection(clas.getAnnotation(SpringCollection.class).name());
//            for (Method method : clas.getDeclaredMethods()) {
//                if (method.isAnnotationPresent(SpringRequest.class)) {
//                    this.item = new Item(method.getName());
//
//                    if(clas.getAnnotation(SpringCollection.class).createTest()) {
//                        this.subTestFolder = new Item(method.getName());
//                        testItems = new ArrayList<>();
//                    }
//                    new SpringControllerClassReader(
//                            method,
//                            collection,
//                            clas,
//                            this.item,
//                            testItems).read();
//
//                    requestFolder.getItem().add(item);
//
//                    if(clas.getAnnotation(SpringCollection.class).createTest()) {
//                        subTestFolder.setItem(testItems);
//                        testFolder.getItem().add(subTestFolder);
//                    }else{
//                        this.testFolder = null;
//                    }
//                }
//            }
//            collection.getItem().add(requestFolder);
//            collection.getItem().add(this.testFolder);
//            System.out.println(collection.toString());
//
//            //save item in /resources folder
//            writeToFile();
//            return true;
//        }else{
//            collection = new com.chocotea.bean.postman.Collection(clas.getAnnotation(JavaxCollection.class).name());
//            for (Method method : clas.getDeclaredMethods()) {
//                System.out.println(Arrays.stream(method.getDeclaredAnnotations()).toList());
//                if (method.isAnnotationPresent(JavaxRequest.class)) {
//                    new jakartaControllerClassReader(
//                            method,
//                            collection,
//                            clas,
//                            requestFolder,
//                            testItems).read();
//
//                    collection.getItem().add(requestFolder);
//                    System.out.println(collection.toString());
//                }
//            }
//            return true;
//        }
//    }

    private void writeToFile() throws IOException {

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(".").getFile() + "collection.json");
        if (file.createNewFile()) {
            System.out.println("File is created!");
        } else {
            System.out.println("File already exists.");
        }

//
//        Files.write(Paths
//                .get(Objects.requireNonNull(ControllerReader.class.getResource("."))
//                                .getFile()+ "/"+method.getName()+".json"),
//                item.toString().getBytes());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        String baseUrl = null;
        String protocol = null;
        Boolean test = null;
        String name= null;
        Set<? extends Element> annotatedElement = roundEnv.getElementsAnnotatedWith(SpringCollection.class);
        for (Element annosation : annotatedElement) {
            baseUrl=  annosation.getAnnotation(SpringCollection.class).name();
            name =  annosation.getAnnotation(SpringCollection.class).name();
            test = annosation.getAnnotation(SpringCollection.class).createTest();
            protocol = annosation.getAnnotation(SpringCollection.class).protocol();
        }

        Set<List<? extends AnnotationMirror>> methodAnnotations = new HashSet<>();
        Set<List<? extends  AnnotationMirror>> parameterAnnotations = new HashSet<>();
        Annotation requestAnnotation = null;
        for (Element annosation : roundEnv.getElementsAnnotatedWith(SpringRequest.class)) {
            requestAnnotation = annosation.getAnnotation(SpringRequest.class);
            methodAnnotations.add(annosation.getAnnotationMirrors());

            if (annosation.getKind() == ElementKind.METHOD) {
                for (VariableElement variableElement : ((ExecutableElement) annosation).getParameters()){
                    parameterAnnotations.add(variableElement.getAnnotationMirrors());
                }
            }
        }


        System.out.println("baseUrl ->" + baseUrl);
        System.out.println("protocol ->" + protocol);
        System.out.println("test ->" + test);
        System.out.println("name ->" + name);
        System.out.println("methodAnnotations -> " + methodAnnotations);
        System.out.println("parameterAnnotations -> " + parameterAnnotations);
        System.out.println("requestAnnotation -> " + requestAnnotation);


        methodAnnotations.forEach(annotList -> annotList.forEach(annotationMirror ->
                annotationMirror.getElementValues().keySet()
                        .stream().filter(executableElement -> executableElement.getSimpleName()
                                .contentEquals("value"))
                         .map(executableElement -> {
                             System.out.println(executableElement.getDefaultValue().getValue().toString());
                             return null;
                         })));


//        new SpringControllerClassReader(
//                methodAnnotations,
//                parameterAnnotations,
//                collection,
//                requestAnnotation,
//                baseUrl,
//                test,
//                protocol,
//                this.item,
//                testItems).read();


//
//        Set<? extends Element> annotatedElements = null;
//        for (TypeElement annotation : annotations) {
//             annotatedElements = roundEnv.getElementsAnnotatedWith(SpringCollection.class);
//            System.out.println("SpringCollection -> " + annotatedElements);
//        }
//
//        for (Element annosation : annotatedElements) {
//            System.out.println( "SpringCollection name -> " + annosation.getAnnotation(SpringCollection.class).name());
//            System.out.println( "SpringCollection baseUrl -> " + annosation.getAnnotation(SpringCollection.class).baseUrl());
//            System.out.println( "SpringCollection ->  protocol" + annosation.getAnnotation(SpringCollection.class).protocol());
//        }
//


//
//        Set<? extends Element> annotatedMethods = null;
//        for (TypeElement annotation : annotations) {
//             annotatedMethods = roundEnv.getElementsAnnotatedWith(SpringRequest.class);
//
//            System.out.println("SpringRequest -> " + annotatedMethods);
//
//        }
//
//        for (Element annosation : annotatedMethods) {
//            System.out.println(annosation.getAnnotation(SpringRequest.class).name());
//
//            //method annotations
//            // System.out.println(annosation.getAnnotationMirrors());
//
//            Set<List<? extends AnnotationMirror>> methodAnnotations = new HashSet<>();
//            methodAnnotations.add(annosation.getAnnotationMirrors());
//
//            System.out.println(methodAnnotations);
//        }

//        for (Element annosation : annotatedMethods) {
//            System.out.println(annosation.getAnnotation(SpringRequest.class).name());
//
//            //method annotations
//           // System.out.println(annosation.getAnnotationMirrors());
//
//            Set<List<? extends AnnotationMirror>> methodAnnotations = new HashSet<>();
//            methodAnnotations.add(annosation.getAnnotationMirrors());
//
//            System.out.println(methodAnnotations);
//
//            List<TypeMirror> values = new ArrayList<>();
//            methodAnnotations.forEach(annotList -> annotList.forEach(annotationMirror -> {
//                if(annotationMirror.getAnnotationType().asElement().getSimpleName().toString().equals("PostMapping")){
//
//                    System.out.println(annotationMirror.getElementValues().keySet()
//                            .stream().filter(executableElement -> executableElement.getSimpleName()
//                                    .contentEquals("value"))
//                            .map(executableElement -> executableElement.getDefaultValue().getValue().toString()));
//                }
//            }));
//
//            if (annosation.getKind() == ElementKind.METHOD) {
//
//                Set<List<? extends  AnnotationMirror>> parameterAnnotations = new HashSet<>();
//                for (VariableElement variableElement : ((ExecutableElement) annosation).getParameters()){
//                    parameterAnnotations.add(variableElement.getAnnotationMirrors());
//                }
//
////                System.out.println("params" +(((ExecutableElement) annosation).getParameters().get(0))
////                        .getAnnotationMirrors());
//                System.out.println("params" +parameterAnnotations);
//                MethodSpec method = MethodSpec.overriding((ExecutableElement) annosation).build();
//                System.out.println("method ->" + method);
//                System.out.println("method annotations => " + method.annotations);
//                //TypeMirror returnType = ((ExecutableElement) annosation).getParameters();
//                // use returnType for stuff ...
//            }
//        }

        return false;
    }
}
