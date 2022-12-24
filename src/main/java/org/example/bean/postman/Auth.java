package org.example.bean.postman;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Auth {

    public enum Type{
        apikey,
        awsv4,
        basic,
        bearer,
        digest,
        edgegrid,
        hawk,
        noauth,
        oauth1,
        oauth2,
        ntlm
    }


    public static JSONObject auth(Auth.Type type){
//        Map<String, Object> auth = new HashMap<>();
        JSONObject auth = new JSONObject();
        auth.put("type", type.name());
        List<Data> dt = new ArrayList<>();

        switch (type){
            case bearer ->
                    dt.add(new Data("token"));
            case apikey ->
                    dt.add(new Data("key"));
            case basic -> {
                dt.add(new Data("password"));
                dt.add(new Data("username"));
            }
            case oauth2 ->
                    dt.add(new Data("addTokenTo"));
            case digest -> {
                dt.add(new Data("password"));
                dt.add(new Data("username"));
                dt.add(new Data("algorithm"));
            }
            case awsv4 -> {
                dt.add(new Data("accessKey"));
                dt.add(new Data("secretKey"));
            }
            default -> {}

        }
        auth.put(type.name(), dt);
         return auth;
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

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Data{
        private String type;
        private String key;
        private String value;

        private Data(String key){
            this.key = key;
            this.type = "string";
            this.value = "";
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

}
