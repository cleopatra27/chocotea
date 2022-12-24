package org.example.bean.postman;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.core.annotations.ChocoRandom;
import org.example.utility.Properties;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Info {

    @NotBlank
    private String _postman_id;

    private String name;
    private String schema;

    @ChocoRandom(dynamic = DynamicVariables.randomFirstName)
    private String reference;

    public Info(String name){
      this._postman_id = UUID.randomUUID().toString();
      this.name = name;
      this.schema = Properties.getProperty("SCHEMA");
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
