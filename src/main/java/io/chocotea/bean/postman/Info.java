package io.chocotea.bean.postman;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.chocotea.core.annotations.ChocoRandom;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Info {

    @NotNull
    private String _postman_id;
    @NotBlank
    private String name;

    private final String schema = "https://schema.getpostman.com/json/collection/v2.1.0/collection.json";

    @ChocoRandom(dynamic = DynamicVariables.randomFirstName)
    private String reference;

    public Info(String name){
      this._postman_id = UUID.randomUUID().toString();
      this.name = name;
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
