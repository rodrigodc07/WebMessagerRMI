package UERJ.RMIUtils;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RMIRegistry {

    public static Registry getRegistry(int port){
        try {
            return LocateRegistry.getRegistry("127.0.0.1",port);
        } catch (RemoteException e) {
            return null;
        }
    }

    public static void registryInRMI(int port, String name,Object object){
        Registry reg = getRegistry(port);
        System.out.println("Waiting...");
        try {
            reg.rebind( name, UnicastRemoteObject.exportObject((Remote) object, 0));
        } catch (Exception e) {
            System.out.println("ERROR: Failed to register the server object.");
            e.printStackTrace();
        }
    }

    public static Object getObjectFromRMI(int port, String name) {
        Object object;
        while (true) {
            try {
                Registry registry = getRegistry(port);
                object =  registry.lookup(name);
                return object;
            }
            catch (NotBoundException ignored) {}
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    public static List<String> listObjects(int port) {
        try {
            String[] aux = getRegistry(port).list();
            List<String> array = Arrays.asList(aux);
            return array.stream().filter(p -> p.contains("client")).collect(Collectors.toList());
        } catch (RemoteException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
