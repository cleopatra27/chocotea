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
            Set<? extends Element> annotatedElement = roundEnv.getElementsAnnotatedWith(SpringCollection.class);
            for (Element annosation : annotatedElement) {
                baseUrl = annosation.getAnnotation(SpringCollection.class).name();
                name = annosation.getAnnotation(SpringCollection.class).name();
                test = annosation.getAnnotation(SpringCollection.class).createTest();
                protocol = annosation.getAnnotation(SpringCollection.class).protocol();
            }

            collection = new com.chocotea.bean.postman.Collection(name);

            List<? extends AnnotationMirror> methodAnnotations;
            List<List<? extends AnnotationMirror>> parameterAnnotations = new ArrayList<>();
            Annotation requestAnnotation;
            for (Element annosation : roundEnv.getElementsAnnotatedWith(SpringRequest.class)) {
                requestAnnotation = annosation.getAnnotation(SpringRequest.class);

                this.item = new Item(((SpringRequest) requestAnnotation).name());

//            try
//            {
//                System.out.println("class =" +((SpringRequest) requestAnnotation).request());
//            }
//            catch( MirroredTypeException mte ){
//                System.out.println("class =" +mte.getTypeMirror());
//
//                if (mte.getTypeMirror() instanceof DeclaredType) {
//                    if (((DeclaredType) mte.getTypeMirror()).asElement() instanceof TypeElement) {
//                        System.out.println(((TypeElement) ((DeclaredType) mte.getTypeMirror())
//                                .asElement()).getEnclosedElements());
//
//                        (((DeclaredType) mte.getTypeMirror()).asElement()).getEnclosedElements().forEach(element -> {
////                            System.out.println("type -" + element.asType());
////                            System.out.println("is prim -" + element.asType().getKind().isPrimitive());
////                            System.out.println("type annots -" + element.getAnnotationMirrors());
////                            System.out.println("expect -?? " + Arrays.toString(element.getAnnotationsByType(ChocoRandom.class)));
//                            if(element.getAnnotation(ChocoRandom.class) != null){
//                                System.out.println("expect -?? " + element.getAnnotation(ChocoRandom.class));
//                                System.out.println("expect -?? " + element.getAnnotation(ChocoRandom.class).dynamic());
//                            }
//                        });
//                    }
//                }
//            }


                methodAnnotations = annosation.getAnnotationMirrors();

                if (annosation.getKind() == ElementKind.METHOD) {
                    for (VariableElement variableElement : ((ExecutableElement) annosation).getParameters()) {
                        parameterAnnotations.add(variableElement.getAnnotationMirrors());
                    }
                }


                if (test) {
                    this.subTestFolder = new Item(name);
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
            System.out.println(collection.toString());

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

