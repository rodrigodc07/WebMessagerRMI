package UERJ.properties;

import UERJ.output.MulticastSocketMessageSenderServer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JavaProperties {

    private static JavaProperties javaProperties;

    private Properties properties;

    private JavaProperties(){
        try (InputStream input = MulticastSocketMessageSenderServer.class.getClassLoader().getResourceAsStream("application.properties")) {

            this.properties = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find application.properties");
                return;
            }
            this.properties.load(input);


        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getProperty(String key){
        return this.properties.getProperty(key);
    }

    public static JavaProperties getJavaProperties(){
        if (JavaProperties.javaProperties == null)
            javaProperties = new JavaProperties();
        return javaProperties;
    }

}
