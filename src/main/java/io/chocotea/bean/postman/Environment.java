package io.chocotea.bean.postman;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Environment {

    public String id;
    public String name;
    public List<Values> values;
    private String _postman_variable_scope;
    private String _postman_exported_at;
    private String _postman_exported_using;

    public Environment(String name){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        values = new ArrayList<>();
        this._postman_variable_scope = "environment";
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Values{
        private String key;
          private String value;
          private String type;
          private boolean enabled;

          public Values(String key, String value, String type, boolean enabled){
              this.key = key;
              this.value = value;
              this.type = type;
              this.enabled = enabled;
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
