package org.example.core;

import org.example.bean.postman.Collection;
import org.example.bean.postman.Header;
import org.example.bean.postman.Item;
import org.example.bean.postman.Query;
import org.example.core.annotations.ChocoRandom;
import org.example.core.annotations.JavaxRequest;
import org.example.core.annotations.SpringCollection;
import org.example.core.annotations.SpringRequest;
import org.example.tests.TestGenerator;
import org.example.utility.BeanReader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.example.bean.postman.Auth.auth;
import static org.example.bean.postman.Modes.raw;

public class ControllerClassReader {

    private Item requestFolder = new Item("REQUESTS");
    private Item item;
    private boolean test;
    private Method method;
    private Collection collection;
    private String baseUrl;
    private boolean createTest;
    private String protocol;
    private Class<?> clazz;
    private SpringRequest springRequest;
    private JavaxRequest javaxRequest;


    public ControllerClassReader(Method method, Collection collection, Class<?> clazz){
        this.item = new Item(method.getName());
        this.springRequest = method.getAnnotation(SpringRequest.class);
        this.method = method;
        this.collection = collection;
        this.baseUrl = clazz.getAnnotation(SpringCollection.class).baseUrl();
        this.createTest = clazz.getAnnotation(SpringCollection.class).createTest();
        this.protocol = clazz.getAnnotation(SpringCollection.class).protocol();
    }

    public void read() {

        //set language
        item.getRequest().getBody().getOptions()
                .getRaw().setLanguage(
                        springRequest.language().name());


        if(springRequest != null){
            new SpringControllerClassReader().handleMappings(method, item, baseUrl);
        }else{

        }

        //add hosts
        handleHost();

        //add protocol
        handleProtocol();

        //generate post body data
        handleBean();

        //path parameters
        // handlePathParameters(method);

        //query parameters
        handleQueryParameters();

        //header parameters
        handleHeaderParameters();

        //if class has create test annotation add test for the constraint
        if(createTest){
            try {
                handleTests();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }


        //add auth
        item.getRequest().setAuth(auth(springRequest.auth()).toString());

        List<Item> requestItems = new ArrayList<>();
        requestItems.add(item);

        requestFolder.setItem(requestItems);
        //  requestFolder.getItem().add(item);

        //add item to collection
        collection.getItem().add(requestFolder);
    }

    private void handleHost(){
        //add hosts
        item.getRequest().getUrl().getHost().addAll(
                List.of(baseUrl.substring(
                        baseUrl.indexOf(protocol+"://")
                                + (protocol+"://").length()
                ).split("\\."))
        );

    }

    private void handleProtocol(){
        item.getRequest().getUrl().setProtocol(protocol);
    }

    private void handleHeaderParameters() {
        Arrays.stream(method.getParameterAnnotations()).forEach(n ->
                {
                    Arrays.stream(n).forEach(header -> {
                        AtomicReference<String> head = new AtomicReference<>("");
                        if (header.annotationType().equals(RequestHeader.class)) {

                            //if ChocoRandom; handle
                            Arrays.stream(n).forEach(val -> {
                                if(val.annotationType().equals(ChocoRandom.class)){
                                    head.set("{{$"+((ChocoRandom) val).dynamic() + "}}");
                                }
                            });

                            item.getRequest().getHeader().add(new Header(((RequestHeader) header).value(), head.get()));
                        }
                    });
                }
        );

    }

    private void handleQueryParameters() {
        final String[] query = {"?"};
        Arrays.stream(method.getParameterAnnotations()).forEach(n ->
                {
                    final String concatenator = "&";
                    Arrays.stream(n).forEach(qury -> {
                        AtomicReference<String> param = new AtomicReference<>("");

                        if (qury.annotationType().equals(RequestParam.class)) {

                            //if ChocoRandom; handle
                            Arrays.stream(n).forEach(val -> {
                                if(val.annotationType().equals(ChocoRandom.class)){
                                    param.set("{{$"+((ChocoRandom) val).dynamic() + "}}");
                                }
                            });

                            item.getRequest().getUrl().getQuery().add(new Query(((RequestParam) qury).value(), param.get()));

                            if (query[0].equals("?")) {
                                query[0] = query[0] + ((RequestParam) qury).value() + param + "=";
                            } else {
                                query[0] = query[0] + concatenator
                                        + ((RequestParam) qury).value() + param + "=";
                            }
                        }
                    });
                }
        );

        //add to url
        item.getRequest().getUrl().setRaw(item.getRequest().getUrl().getRaw() + query[0]);


    }

    private void handlePathParameters(Method method) {
        List<String> pathList = new ArrayList<>();
        Arrays.stream(method.getParameterAnnotations()).forEach(n ->
                {
                    Arrays.stream(n).forEach(param -> {
                        if (param.annotationType().equals(PathVariable.class)) {
                            pathList.add(((PathVariable) param).value());

                            //TODO; if has chocogenerate, handle
                        }
                    });
                }
        );

        item.getRequest().getUrl().setPath(pathList);

    }

    private void handleTests() throws ClassNotFoundException {
        //call generate tests
        new TestGenerator(collection).generateTests(springRequest.requestBean().getName(), item, "test");
    }

    private void handleBean(){
        if (springRequest.requestBean() != null) {
            item.getRequest().getBody().setMode(raw.name());
            item.getRequest().getBody().setRaw(BeanReader.toString(springRequest.requestBean().getName()));
        }
    }
}
