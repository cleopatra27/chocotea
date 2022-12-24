package org.example;

import org.example.core.annotations.SpringCollection;
import org.example.postman.Collection;
import org.example.utility.PathGenerator;


public class Main {
    public static void main(String[] args) {

        PathGenerator pathGenerator = new PathGenerator();
        if(pathGenerator.getClass().isAnnotationPresent(SpringCollection.class)){
            System.out.println("yup");
            Collection collection = new Collection();
            collection.handle(pathGenerator.getClass().getDeclaredMethods(), pathGenerator.getClass());
        }else {
            System.out.println("nop");
        }
    }

    private void validateInput(){

    }
}