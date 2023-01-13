package com.chocotea.core;

import com.chocotea.bean.postman.Collection;
import com.chocotea.bean.postman.Item;
import com.chocotea.core.annotations.JavaxRequest;
import com.chocotea.core.annotations.SpringRequest;
import com.chocotea.tests.TestGenerator;
import com.chocotea.utility.BeanReader;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import static com.chocotea.bean.postman.Auth.auth;
import static com.chocotea.bean.postman.Modes.raw;

public abstract class ControllerReader {

    public Item item;

    private List<Item> testItems;
    public String baseUrl;
    private boolean createTest;
    private String protocol;
    private List<? extends AnnotationMirror> methodAnnotations;
    private List<List<? extends  AnnotationMirror>> parameterAnnotations;
    private SpringRequest springRequest;
    private JavaxRequest javaxRequest;


    public ControllerReader(
            List<? extends AnnotationMirror> methodAnnotations,
            List<List<? extends  AnnotationMirror>> parameterAnnotations,
                            Annotation requestAnnotation,
                            String baseUrl, boolean createTest, String protocol,
                            boolean spring, Item item, List<Item> testItems){
        this.testItems = testItems;
        this.item = item;
       this.methodAnnotations = methodAnnotations;
        this.parameterAnnotations = parameterAnnotations;
        this.baseUrl = baseUrl;
        this.createTest = createTest;
        this.protocol = protocol;
        if(spring) {
            this.springRequest = (SpringRequest)requestAnnotation;
        }else{
            this.springRequest = null;
            this.javaxRequest = (JavaxRequest)requestAnnotation;
        }
    }

    public void read() throws IOException {

        handleMappings(methodAnnotations, item, baseUrl);

        setLanguage();

        //add hosts
        setHost();

        //add protocol
        handleProtocol();

        //generate post body data
        handleBean();

        //path parameters
        handlePathParameters(parameterAnnotations, item);

        //query parameters
        handleQueryParameters(parameterAnnotations, item);

        //header parameters
        handleHeaderParameters(parameterAnnotations, item);

        //if class has createTest annotation add test for the constraint
        if(createTest){
            try {
                handleTests();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        //add auth
        if(springRequest != null) {
            item.getRequest().setAuth(auth(springRequest.auth(), springRequest.authValue()).toString());
        }else {
            item.getRequest().setAuth(auth(javaxRequest.auth(), javaxRequest.authValue()).toString());
        }

    }


    protected void setLanguage(){
        //set language

        if(springRequest != null){
            item.getRequest().getBody().getOptions()
                    .getRaw().setLanguage(
                            springRequest.language().name());
        }else{
            item.getRequest().getBody().getOptions()
                    .getRaw().setLanguage(
                            javaxRequest.language().name());
        }
    }

    protected void setHost(){
        //add hosts
        item.getRequest().getUrl().getHost().addAll(
                List.of(baseUrl.substring(
                        baseUrl.indexOf(protocol+"://")
                                + (protocol+"://").length()
                ).split("\\."))
        );

    }

    public abstract void handleMappings(List<? extends AnnotationMirror> methodAnnotations, Item item, String baseUrl);

    protected void handleProtocol(){
        item.getRequest().getUrl().setProtocol(protocol);
    }

    public abstract void handleHeaderParameters(List<List<? extends AnnotationMirror>> parameterAnnotations, Item item);

    public abstract void handleQueryParameters(List<List<? extends AnnotationMirror>> parameterAnnotations, Item item);

    public abstract void handlePathParameters(List<List<? extends AnnotationMirror>> parameterAnnotations, Item item);

    private void handleTests() throws ClassNotFoundException {
        //call generate tests
        if(springRequest != null) {
            try
            {
                System.out.println("class =" +springRequest.request());
            }
            catch( MirroredTypeException mte ) {
                new TestGenerator(testItems).generateTests(mte.getTypeMirror().toString(), item, "test");
            }

        }else{
            new TestGenerator(testItems).generateTests(javaxRequest.requestBean().getName(), item, "test");
        }
    }

    private void handleBean() {
        //TODO get class using java.lang.model
        if (springRequest != null) {

            try
            {
                System.out.println("class =" +springRequest.request());
            }
            catch( MirroredTypeException mte ) {
                item.getRequest().getBody().setMode(raw.name());
                item.getRequest().getBody().setRaw(BeanReader.generate(mte.getTypeMirror()));
            }

//            //TODO: get request class using java lang
//            if (springRequest.request() != DefaultClass.class) {
////                if(springRequest.language() != json){
////                    //if test generate dummy text?
////
////                }
//                item.getRequest().getBody().setMode(raw.name());
////                item.getRequest().getBody().setRaw(BeanReader.toString(springRequest.request().getName()));
//                item.getRequest().getBody().setRaw(BeanReader.generate(springRequest.request().getName()));
//            }
//        } else {
//            if (javaxRequest.requestBean() != DefaultClass.class) {
//                item.getRequest().getBody().setMode(raw.name());
//                item.getRequest().getBody().setRaw(BeanReader.toString(javaxRequest.requestBean().getName()));
//            }
        }
    }
}
