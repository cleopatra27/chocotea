package io.chocotea.bean.postman;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import io.chocotea.bean.HTTPVerbs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Request {

   private Map<String, Object> auth;
    private String method;
    private List<Header> header = new ArrayList<>();
    private Body body;
    private Url url;

    public Request(){
        this.body = new Body();
    }

    public void setMethod(HTTPVerbs verb){
        this.method = verb.name();
    }

    @Override
    public String toString() {

        String value = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            value = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return value;

    }
}
