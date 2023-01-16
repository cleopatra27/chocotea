package io.chocotea.bean.postman;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


      public static Map<String, Object> auth(Auth.Type type, String[] values){
        Map<String, Object> auth = new HashMap<>();
        auth.put("type", type.name());
        List<Data> dt = new ArrayList<>();

        switch (type){
            case bearer ->
                    dt.add(new Data("token", values[0]));
            case apikey ->
                    dt.add(new Data("key", values[0]));
            case basic -> {
                dt.add(new Data("password", values[1]));
                dt.add(new Data("username", values[0]));
            }
            case oauth2 ->
                    dt.add(new Data("addTokenTo", values[0]));
            case digest -> {
                dt.add(new Data("password", values[1]));
                dt.add(new Data("username", values[0]));
                dt.add(new Data("algorithm", values[2]));
            }
            case awsv4 -> {
                dt.add(new Data("accessKey", values[0]));
                dt.add(new Data("secretKey", values[1]));
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

        private Data(String key, String value){
            this.key = key;
            this.type = "string";
            this.value = value;
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
