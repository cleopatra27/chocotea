package com.chocotea.utility;

import com.chocotea.bean.postman.DynamicVariables;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class RandomGenerator {

    public static Object generate(boolean generateRandom, DynamicVariables variable, String type){
        //TODO: if inner class

        if(Objects.equals(type, "java.lang.String")){
            return generateRandom ? "{{$"+variable+"}}" : "toast";
        }else if(Objects.equals(type, "java.lang.Boolean") || Objects.equals(type, "boolean")){
            return generateRandom ? "{{$"+variable+"}}" : false;
        } else if(Objects.equals(type, "int") || Objects.equals(type, "java.lang.Integer")){
            return generateRandom ? variable : new Random().nextInt();
        }else if(Objects.equals(type, "java.lang.Double") || Objects.equals(type, "double")
                || Objects.equals(type, "java.math.BigDecimal") || Objects.equals(type, "float")
                || Objects.equals(type, "java.lang.Float")){
            return generateRandom ? variable : new Random().nextDouble();
        }  else if(Objects.equals(type, "long") || Objects.equals(type, "java.lang.Long")){
            return generateRandom ? variable : new Random().nextLong();
        }
        else{
            return "";
        }
    }

}
