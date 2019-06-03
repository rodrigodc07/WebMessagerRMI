package UERJ.client;

import UERJ.Message;
import UERJ.observer.Observer;
import UERJ.server.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client implements Observer{

    private static Scanner scanner = new Scanner(System.in);

    private static ServerInterface connectToServer(int port, String user) {
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", port);
            ServerInterface server = (ServerInterface) registry.lookup("server_" + user);
            System.out.println("Connected to UERJ.Server");
            return server;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }

    public static void main(String[] args) {

        ServerInterface server = connectToServer(Integer.parseInt(args[0]),args[1]);

        if (server != null) {
            Client client = new Client();
            try {
                server.register(client);
                while(true) {
                    String body = getBodyFromConsole();
                    Message message = new Message(body);
                    server.sendMessage(message);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            System.out.println("Finished");
        }
    }

    private static String getBodyFromConsole() {
        System.out.print("Enter a string : ");
        return scanner.nextLine();
    }

    @Override
    public void notify(Message message) {
        System.out.println(message);
    }
}