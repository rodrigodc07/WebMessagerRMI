package UERJ.client;

import UERJ.Message;
import UERJ.server.ServerInterface;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientImpl implements ClientInterface, Serializable, Runnable {

    private ServerInterface server;

    private boolean hasServer = false;

    private ArrayList<Message> bufferedMessages= new ArrayList<Message>();

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
            ClientInterface client = new ClientImpl();
            Thread t1 = new Thread((Runnable) client);
            t1.start();
            try {
                client.registryServer(server);
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

    public void run(){
        while(true) {
            System.out.println(bufferedMessages.isEmpty());
            if(!bufferedMessages.isEmpty()){
                try {
                    Thread.sleep(500);
                    for (Message message: bufferedMessages){
                        System.out.println(message);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void registryServer(ServerInterface server) throws RemoteException {
        this.server = server;
        this.hasServer = true;
    }

    @Override
    public boolean hasServer() throws RemoteException {
        return hasServer;
    }

    @Override
    public void pullMessages(Message message) throws RemoteException {
        bufferedMessages.add(message);
    }
}