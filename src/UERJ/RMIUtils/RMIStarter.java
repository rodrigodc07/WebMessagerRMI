package UERJ.RMIUtils;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIStarter {

    private static Registry createRegistry(int port){
        try {
            return LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            return null;
        }
    }
    public static void main(String[] args){
        createRegistry(Integer.parseInt(args[0]));
        while (true);
    }
}
