package com.chocotea;

import com.chocotea.core.annotations.SpringCollection;
import com.chocotea.postman.Collection;
import com.chocotea.utility.JavaxPathGenerator;
import com.chocotea.utility.PathGenerator;

import java.io.IOException;
import java.util.Arrays;


public class Main {
    public static void main(String[] args) throws IOException {

        PathGenerator pathGenerator = new PathGenerator();
//        JavaxPathGenerator pathGenerator = new JavaxPathGenerator();
            Collection collection = new Collection();
            collection.handle(pathGenerator.getClass());
    }

    private void validateInput(){

    }
}