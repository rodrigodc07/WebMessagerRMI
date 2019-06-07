package UERJ.RMIUtils;

import UERJ.client.ClientInterface;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIRegistry {


    public static Registry createRegistry(int port){
        try {
            return LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            return null;
        }
    }

    public static Registry getRegistry(int port){
        try {
            return LocateRegistry.getRegistry("127.0.0.1",port);
        } catch (RemoteException e) {
            return null;
        }
    }

    public static Registry getRMIRegistry(int port){
        Registry registry = createRegistry(port);
        if (registry == null)
            registry = getRegistry(port);
        return registry;
    }

    public static void registryInRMI(int port, String name,Object object){
        Registry reg = getRMIRegistry(port);
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
                Registry registry = getRMIRegistry(port);
                object =  registry.lookup(name);
                return object;
            }
            catch (NotBoundException ignored) {}
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
