package io.chocotea.bean.postman;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Item {

    private String name;
    private Request request;
    private List<Event> event;
    private List<String> response = new ArrayList<>();
    private List<Item> item;


    public Item copy(){
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(this), Item.class);
    }

    public Item(String name){
        this.name = name;
        this.request = new Request();
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
