package UERJ.server;

import UERJ.Message;
import UERJ.observer.Observer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ServerImpl implements ServerInterface {

    private ArrayList<Observer> observers = new ArrayList();

    @Override
    public void register(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void sendMessage(Message message) throws RemoteException {
        for (Observer observe :observers)
            observe.notify(message);
    }

    public static void main(String[] args) {
        Registry reg = null;
        int port = Integer.parseInt(args[0]);
        String user = args[1];
        try {
            reg = LocateRegistry.createRegistry(port);
        } catch (Exception e) {
            System.out.println("ERROR: Could not create the registry.");
            e.printStackTrace();
        }
        ServerImpl serverObject = new ServerImpl();
        System.out.println("Waiting...");
        try {
            reg.rebind("server_" + user, (ServerInterface) UnicastRemoteObject.exportObject(serverObject, 0));
        } catch (Exception e) {
            System.out.println("ERROR: Failed to register the server object.");
            e.printStackTrace();
        }
    }

}
