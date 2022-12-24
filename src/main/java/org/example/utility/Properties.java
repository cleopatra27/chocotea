package org.example.utility;

import java.io.IOException;
import java.io.InputStream;

public class Properties {

        public static java.util.Properties getProperties(){
            java.util.Properties prop = new java.util.Properties();
            ClassLoader loader = Properties.class.getClassLoader();
            try (InputStream input = loader.getResourceAsStream("base.properties")) {
                // load a properties file
                prop.load(input);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return prop;
        }

        public static  String getProperty(String key) {
            return getProperties().getProperty(key);
        }

}
