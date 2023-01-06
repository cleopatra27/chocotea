package com.chocotea.bean.postman;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import com.chocotea.bean.HTTPVerbs;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
//@NoArgsConstructor
public class Request {

    private JSONObject auth;
    private String method;
    private List<Header> header = new ArrayList<>();
    private Body body;
    private Url url;

    public Request(){
        this.body = new Body();
    }

    public void setAuth(String data){
            this.auth =new JSONObject(data);

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
