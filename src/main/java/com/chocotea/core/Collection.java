package com.chocotea.core;

import com.chocotea.bean.postman.Item;
import com.chocotea.core.annotations.JakartaRequest;
import com.chocotea.core.annotations.JavaxRequest;
import com.chocotea.core.annotations.SpringRequest;
import com.chocotea.tests.TestGenerator;
import com.chocotea.utility.BeanReader;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.MirroredTypeException;
import java.lang.annotation.Annotation;
import java.util.List;
import static com.chocotea.bean.postman.Auth.auth;
import static com.chocotea.bean.postman.Modes.raw;

public final class Collection {

    public static final class Builder {

        public Item item;
        private List<Item> testItems;
        public String baseUrl;
        private String protocol;
        private List<? extends AnnotationMirror> methodAnnotations;
        private List<List<? extends  AnnotationMirror>> parameterAnnotations;
        private Annotation requestAnnotation;

        public Builder(List<? extends AnnotationMirror> methodAnnotations, List<List<? extends AnnotationMirror>> parameterAnnotations,
                               Annotation requestAnnotation, String baseUrl, String protocol, boolean spring,
                               Item item, List<Item> testItems){
            Factory.getOperation(requestAnnotation).get().handleMappings(methodAnnotations,item, baseUrl);
            this.testItems = testItems;
            this.item = item;
            this.methodAnnotations = methodAnnotations;
            this.parameterAnnotations = parameterAnnotations;
            this.baseUrl = baseUrl;
            this.protocol = protocol;
            this.requestAnnotation = requestAnnotation;
        }

        public Builder setLanguage(){
            //TODO: prevent this method from growing
            item.getRequest().getBody().getOptions()
                    .getRaw().setLanguage(requestAnnotation.annotationType().getSimpleName().contains("SpringRequest")
                    ? ((SpringRequest)requestAnnotation).language().name()
                            : ((JavaxRequest)requestAnnotation).language().name()
                   // : ((Ja)requestAnnotation).language().name()
        );
            return this;
        }

        public Builder setHost(){
            item.getRequest().getUrl().getHost().addAll(
                    List.of(baseUrl.substring(
                            baseUrl.indexOf(protocol+"://")
                                    + (protocol+"://").length()
                    ).split("\\."))
            );
            return this;
        }

        public Builder setProtocol(){
            item.getRequest().getUrl().setProtocol(protocol);
            return this;
        }

        public Builder setBean() {
            //TODO: prevent this method from growing
            if (requestAnnotation != null) {
                try {
                    if(requestAnnotation.annotationType().getSimpleName().contentEquals("SpringRequest")){
                        ((SpringRequest)requestAnnotation).request();
                    }else if(requestAnnotation.annotationType().getSimpleName().contentEquals("JavaxRequest")){
                        ((JavaxRequest)requestAnnotation).request();
                    }else if(requestAnnotation.annotationType().getSimpleName().contentEquals("JakartaRequest")){
                        ((JakartaRequest)requestAnnotation).request();
                    }
                } catch( MirroredTypeException mte ) {
                    item.getRequest().getBody().setMode(raw.name());
                    item.getRequest().getBody().setRaw(BeanReader.generate(mte.getTypeMirror()));
                }
            }
            return this;
        }

        public Builder setAuth(){
            //TODO: prevent this method from growing
            if(requestAnnotation.annotationType().getSimpleName().contentEquals("SpringRequest")){
                item.getRequest().setAuth(auth(((SpringRequest)requestAnnotation).auth(),
                        ((SpringRequest)requestAnnotation).authValue()));
            }else if(requestAnnotation.annotationType().getSimpleName().contentEquals("JavaxRequest")){
                item.getRequest().setAuth(auth(((JavaxRequest)requestAnnotation).auth(),
                        ((JavaxRequest)requestAnnotation).authValue()));
            }else if(requestAnnotation.annotationType().getSimpleName().contentEquals("JakartaRequest")){
                item.getRequest().setAuth(auth(((JakartaRequest)requestAnnotation).auth(),
                        ((JakartaRequest)requestAnnotation).authValue()));
            }
            return this;
        }

        public Builder createTests(boolean test)  {
            //TODO: prevent this method from growing
            if(test) {
                try {
                    if(requestAnnotation.annotationType().getSimpleName().contentEquals("SpringRequest")){
                        ((SpringRequest)requestAnnotation).request();
                    }else if(requestAnnotation.annotationType().getSimpleName().contentEquals("JavaxRequest")){
                        ((JavaxRequest)requestAnnotation).request();
                    }else if(requestAnnotation.annotationType().getSimpleName().contentEquals("JakartaRequest")){
                        ((JakartaRequest)requestAnnotation).request();
                    }
                } catch (MirroredTypeException mte) {
                    new TestGenerator(testItems)
                            .generate(mte.getTypeMirror(), item);
                }
            }
                return this;
        }

        public void build(){
            Factory.getOperation(requestAnnotation).get().handleHeaderParameters(parameterAnnotations,item);
            Factory.getOperation(requestAnnotation).get().handleQueryParameters(parameterAnnotations,item);
            Factory.getOperation(requestAnnotation).get().handlePathParameters(parameterAnnotations,item);
        }

    }
    public static Builder builder (List<? extends AnnotationMirror> methodAnnotations,
                                   List<List<? extends AnnotationMirror>> parameterAnnotations,
                            Annotation requestAnnotation, String baseUrl, String protocol, boolean spring,
                            Item item, List<Item> testItems){
        return new Builder(methodAnnotations, parameterAnnotations, requestAnnotation, baseUrl, protocol,
                spring, item, testItems);
    }



}

