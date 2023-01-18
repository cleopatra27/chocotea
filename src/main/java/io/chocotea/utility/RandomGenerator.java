package io.chocotea.utility;

import io.chocotea.bean.postman.DynamicVariables;

import java.util.Objects;
import java.util.Random;

public class RandomGenerator {

    public static Object generate(boolean generatePostmanRandom, DynamicVariables variable, String type){
        //TODO: if inner class

        if(type.contains("String")){
            return generatePostmanRandom ? "{{$"+variable+"}}" : "toast";
        }else if(type.contains( "Boolean") || type.contains( "boolean")){
            return generatePostmanRandom ? "{{$"+variable+"}}" : false;
        } else if(type.contains( "int") || type.contains( "Integer")){
            return generatePostmanRandom ? variable : new Random().nextInt();
        }else if(type.contains( "Double") || type.contains( "double")
                || type.contains( "BigDecimal") || type.contains( "float")
                || type.contains( "Float")){
            return generatePostmanRandom ? variable : new Random().nextDouble();
        }  else if(type.contains( "long") || type.contains( "Long")){
            return generatePostmanRandom ? variable : new Random().nextLong();
        }
        else{
            return "";
        }
    }

}
