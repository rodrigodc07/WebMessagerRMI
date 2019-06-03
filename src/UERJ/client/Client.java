package UERJ.client;

import UERJ.Message;
import UERJ.server.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private void connectToServer() {
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            ServerInterface server = (ServerInterface) registry.lookup("server");
            System.out.println("Connected to UERJ.Server");

            String text = server.getBody();
            System.out.println(text);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void main(String[] args) {

        String text = "RMI Test Message";
        ServerInterface server = null;

        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            server = (ServerInterface) registry.lookup("server");
            System.out.println("Connected to Server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (server != null) {
            try {
                Message message = new Message("TESTE");
                server.sendMessage(message);
                System.out.println(server.getMessage());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            System.out.println("Finished");
        }
    }
}