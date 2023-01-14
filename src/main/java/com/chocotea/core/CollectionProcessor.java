package com.chocotea.core;

import com.chocotea.bean.postman.Collection;
import com.chocotea.bean.postman.Item;
import com.chocotea.core.annotations.SpringCollection;
import com.chocotea.core.annotations.SpringRequest;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private Item testFolder= new Item("TESTS");
    private List<Item> testItems;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            requestFolder.setItem(new ArrayList<>());
            testFolder.setItem(new ArrayList<>());
            String baseUrl = null;
            String protocol = null;
            boolean test = false;
            String name = null;
            Set<? extends Element> annotatedClasses = roundEnv.getElementsAnnotatedWith(SpringCollection.class);
            for (Element annotatedClass : annotatedClasses) {
                baseUrl = annotatedClass.getAnnotation(SpringCollection.class).name();
                name = annotatedClass.getAnnotation(SpringCollection.class).name();
                test = annotatedClass.getAnnotation(SpringCollection.class).createTest();
                protocol = annotatedClass.getAnnotation(SpringCollection.class).protocol();
            }

            collection = new com.chocotea.bean.postman.Collection(name);

            List<? extends AnnotationMirror> methodAnnotations;
            List<List<? extends AnnotationMirror>> parameterAnnotations = new ArrayList<>();
            Annotation requestAnnotation;
            for (Element myAnnotationMethod : roundEnv.getElementsAnnotatedWith(SpringRequest.class)) {
                requestAnnotation = myAnnotationMethod.getAnnotation(SpringRequest.class);

                this.item = new Item(!Objects.equals(((SpringRequest) requestAnnotation).name(), "Sample Request") ?
                        ((SpringRequest) requestAnnotation).name() :
                        myAnnotationMethod.getSimpleName().toString());

                methodAnnotations = myAnnotationMethod.getAnnotationMirrors();

                if (myAnnotationMethod.getKind() == ElementKind.METHOD) {
                    for (VariableElement variableElement : ((ExecutableElement) myAnnotationMethod).getParameters()) {
                        parameterAnnotations.add(variableElement.getAnnotationMirrors());
                    }
                }


                if (test) {
                    this.subTestFolder = new Item(myAnnotationMethod.getSimpleName().toString());
                    testItems = new ArrayList<>();
                }

                try {
                    new SpringControllerClassReader(
                            methodAnnotations,
                            parameterAnnotations,
                            requestAnnotation,
                            baseUrl,
                            test,
                            protocol,
                            this.item,
                            testItems).read();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                requestFolder.getItem().add(item);

                if (test) {
                    subTestFolder.setItem(testItems);
                     testFolder.getItem().add(subTestFolder);
                } else {
                    this.testFolder = null;
                }
            }
            collection.getItem().add(requestFolder);
        collection.getItem().add(this.testFolder);

            //save item in /resources folder
            try {
                Writer writer = processingEnv.getFiler().createResource(
                        StandardLocation.SOURCE_OUTPUT, "", "sample.json").openWriter();
                writer.write(collection.toString());
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return true;
        }
        return true;
    }
}

