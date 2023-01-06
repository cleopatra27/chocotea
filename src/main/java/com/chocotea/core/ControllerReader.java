package com.chocotea.core;

import com.chocotea.bean.postman.Collection;
import com.chocotea.bean.postman.Item;
import com.chocotea.core.annotations.JavaxCollection;
import com.chocotea.core.annotations.JavaxRequest;
import com.chocotea.core.annotations.SpringCollection;
import com.chocotea.core.annotations.SpringRequest;
import com.chocotea.tests.TestGenerator;
import com.chocotea.utility.BeanReader;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.chocotea.bean.postman.Auth.auth;
import static com.chocotea.bean.postman.Modes.raw;

public abstract class ControllerReader {

    private Item requestFolder = new Item("REQUESTS");
    public Item item;
    private Method method;
    private Collection collection;
    public String baseUrl;
    private boolean createTest;
    private String protocol;
    private SpringRequest springRequest;
    private JavaxRequest javaxRequest;


    public ControllerReader(Method method, Collection collection, Class<?> clazz, boolean spring){
        this.item = new Item(method.getName());
        this.method = method;
        this.collection = collection;
        if(spring) {
            this.baseUrl = clazz.getAnnotation(SpringCollection.class).baseUrl();
            this.createTest = clazz.getAnnotation(SpringCollection.class).createTest();
            this.protocol = clazz.getAnnotation(SpringCollection.class).protocol();
            this.springRequest = method.getAnnotation(SpringRequest.class);
        }else{
            this.springRequest = null;
            this.baseUrl = clazz.getAnnotation(JavaxCollection.class).baseUrl();
            this.createTest = clazz.getAnnotation(JavaxCollection.class).createTest();
            this.protocol = clazz.getAnnotation(JavaxCollection.class).protocol();
            this.javaxRequest = method.getAnnotation(JavaxRequest.class);
        }
    }

    public void read() throws IOException {

        handleMappings(method, item, baseUrl);

        setLanguage();

        //add hosts
        setHost();

        //add protocol
        handleProtocol();

        //generate post body data
        handleBean();

        //path parameters
        handlePathParameters(method, item);

        //query parameters
        handleQueryParameters(method, item);

        //header parameters
        handleHeaderParameters(method, item);

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

        List<Item> requestItems = new ArrayList<>();
        requestItems.add(item);

        requestFolder.setItem(requestItems);

        //add item to collection
        collection.getItem().add(requestFolder);

        //save item in /resources folder
        writeToFile();


    }

    private void writeToFile() throws IOException {
        Files.write(Paths.get(Objects.requireNonNull(ControllerReader.class.getResource(".")).getFile()+ "/"+method.getName()+".json"), item.toString().getBytes());
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

    public abstract void handleMappings(Method method, Item item, String baseUrl);

    protected void handleProtocol(){
        item.getRequest().getUrl().setProtocol(protocol);
    }

    public abstract void handleHeaderParameters(Method method, Item item);

    public abstract void handleQueryParameters(Method method, Item item);

    public abstract void handlePathParameters(Method method, Item item);

    private void handleTests() throws ClassNotFoundException {
        //call generate tests
        if(springRequest != null) {
            new TestGenerator(collection).generateTests(springRequest.requestBean().getName(), item, "test");
        }else{
            new TestGenerator(collection).generateTests(javaxRequest.requestBean().getName(), item, "test");
        }
    }

    private void handleBean() {
        if (springRequest != null) {
            if (springRequest.requestBean() != null) {
                item.getRequest().getBody().setMode(raw.name());
                item.getRequest().getBody().setRaw(BeanReader.toString(springRequest.requestBean().getName()));
            }
        } else {
            if (javaxRequest.requestBean() != null) {
                item.getRequest().getBody().setMode(raw.name());
                item.getRequest().getBody().setRaw(BeanReader.toString(javaxRequest.requestBean().getName()));
            }
        }
    }
}
