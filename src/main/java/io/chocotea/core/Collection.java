package io.chocotea.core;

import io.chocotea.bean.postman.Item;
import io.chocotea.core.annotations.JakartaRequest;
import io.chocotea.core.annotations.JavaxRequest;
import io.chocotea.core.annotations.SpringRequest;
import io.chocotea.core.tests.TestGenerator;
import io.chocotea.utility.BeanReader;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.MirroredTypeException;
import java.lang.annotation.Annotation;
import java.util.List;
import static io.chocotea.bean.postman.Auth.auth;
import static io.chocotea.bean.postman.Modes.raw;

/**
 * Handles building the postman collection from method
 */
public final class Collection {

    public static final class Builder {

        public Item item;
        private List<Item> testItems;
        public String baseUrl = "{{BaseUrl}}";
        private String protocol;
        private List<? extends AnnotationMirror> methodAnnotations;
        private List<List<? extends  AnnotationMirror>> parameterAnnotations;
        private Annotation requestAnnotation;

        public Builder(List<? extends AnnotationMirror> methodAnnotations, List<List<? extends AnnotationMirror>> parameterAnnotations,
                               Annotation requestAnnotation, String protocol,
                               Item item, List<Item> testItems){
            Factory.getOperation(requestAnnotation).get().handleMappings(methodAnnotations,item, baseUrl);
            this.testItems = testItems;
            this.item = item;
            this.methodAnnotations = methodAnnotations;
            this.parameterAnnotations = parameterAnnotations;
            this.protocol = protocol;
            this.requestAnnotation = requestAnnotation;
        }

        /**
         * Sets the language to user defined
         * @return Builder
         */
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

        /**
         * sets the host to user defined
         * @return Builder
         */
        public Builder setHost(){
//            item.getRequest().getUrl().getHost().addAll(
//                    List.of(baseUrl.substring(
//                            baseUrl.indexOf(protocol+"://")
//                                    + (protocol+"://").length()
//                    ).split("\\."))
//            );
            item.getRequest().getUrl().getHost().add(baseUrl);
            return this;
        }

        @Deprecated
        public Builder setProtocol(){
            item.getRequest().getUrl().setProtocol(protocol);
            return this;
        }

        /**
         * generates a body if needed
         * @return Builder
         */
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

        /**
         * adds auth to request
         * @return
         */
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

        /**
         * creates tests for header, auth, path, body and adds to request
         * @param test
         * @return
         */
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
                } catch (MirroredTypeException requestMirror) {
                    try {
                        if(requestAnnotation.annotationType().getSimpleName().contentEquals("SpringRequest")){
                            ((SpringRequest)requestAnnotation).response();
                        }else if(requestAnnotation.annotationType().getSimpleName().contentEquals("JavaxRequest")){
                            ((JavaxRequest)requestAnnotation).response();
                        }else if(requestAnnotation.annotationType().getSimpleName().contentEquals("JakartaRequest")){
                            ((JakartaRequest)requestAnnotation).response();
                        }
                    } catch (MirroredTypeException responseMirror) {
                        new TestGenerator(testItems)
                                .generate(requestMirror.getTypeMirror(), responseMirror.getTypeMirror(), item);
                    }

                }
            }
                return this;
        }

        /**
         * builds the header parameters, query parameters and path parameters.
         */
        public void build(){
            Factory.getOperation(requestAnnotation).get().handleHeaderParameters(parameterAnnotations,item);
            Factory.getOperation(requestAnnotation).get().handleQueryParameters(parameterAnnotations,item);
            Factory.getOperation(requestAnnotation).get().handlePathParameters(parameterAnnotations,item);
        }

    }

    /**
     * builds postman requests, events and tests for the collection
     * @param methodAnnotations all annotations on method with choco request
     * @param parameterAnnotations all parameter annotations from methods
     * @param requestAnnotation the choc request annotation
     * @param protocol protocol http or https for the url
     * @param item the requests folder to be added to
     * @param testItems the test folder to be added to
     * @return Builder
     */
    public static Builder builder (List<? extends AnnotationMirror> methodAnnotations,
                                   List<List<? extends AnnotationMirror>> parameterAnnotations,
                            Annotation requestAnnotation,  String protocol,
                            Item item, List<Item> testItems){
        return new Builder(methodAnnotations, parameterAnnotations, requestAnnotation,  protocol,
                 item, testItems);
    }



}

