package org.example.postman;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bean.postman.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Setter
@NoArgsConstructor
@Deprecated
public class Environment {
    //To be utilized later
    private String environmentName;
    private Map<String, Object> variables;

    private Item item;

    public Environment(Map<String, Object> variables, Item item){
        this.variables = variables;
        this.item = item;
    }

    public void create(){
        AtomicReference<String> environmentPre = new AtomicReference<>();
        //sets environment variables
        variables.forEach((k, v) -> {
            environmentPre.set(environmentPre
                    + "pm.environment.set('" + k + "', '" + v + "');");
        });

        //add to item
        List<Item> requestItems = new ArrayList<>();
        requestItems.add(item);
        item.setItem(requestItems);

    }

    public void getVariables(String environmentName){

    }

    public void getVariable(String environmentName){
        //pm.environment.get('name', 'John Doe');
    }

    public void addVariable(String key, Object value){
        this.variables.put(key, value);
    }
}
