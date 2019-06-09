package UERJ.RMIUtils;

import UERJ.properties.JavaProperties;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import static UERJ.properties.JavaProperties.getJavaProperties;

public class RMIStarter {

    private static void createRegistry(){
        try {
            JavaProperties javaProperties = getJavaProperties();
            int port = Integer.parseInt(javaProperties.getProperty("rmi.port"));

            LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        createRegistry();
        while (true);
    }
}
