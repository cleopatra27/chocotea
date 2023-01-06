package com.chocotea.utility;

import com.chocotea.bean.postman.DynamicVariables;

public class RandomGenerator {

    public static String stringGenerator(boolean generateRandom, DynamicVariables variable){
        return generateRandom ? "{{$"+variable+"}}" :"toast";
    }

    public static Object integerGenerator(boolean generateRandom, DynamicVariables variable){
        return generateRandom ? variable :0;
    }
}