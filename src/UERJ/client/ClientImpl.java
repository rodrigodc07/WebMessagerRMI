package UERJ.client;

import UERJ.Message;
import UERJ.server.ServerInterface;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientImpl implements ClientInterface, Serializable, Runnable {

    private ServerInterface server;

    private boolean hasServer = false;

    private ArrayList<Message> bufferedMessages= new ArrayList<Message>();

    private static Scanner scanner = new Scanner(System.in);
    private String username;

    private ClientImpl(int port, String user) {
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", port);
            ServerInterface server = (ServerInterface) registry.lookup("server_" + user);
            System.out.println("Connected to UERJ.Server");
            this.server = server;
            this.username = user;

            Registry reg = LocateRegistry.getRegistry("127.0.0.1",port);
            System.out.println("Waiting...");
            try {
                reg.rebind("client_" + user, (ClientInterface) UnicastRemoteObject.exportObject(this, 0));
            } catch (Exception e) {
                System.out.println("ERROR: Failed to register the server object.");
                e.printStackTrace();
            }

            server.register(this);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void main(String[] args) {

        ClientInterface client = new ClientImpl(Integer.parseInt(args[0]),args[1]);
        Thread t1 = new Thread((Runnable) client);
        t1.start();
        try {
            while(true) {
                String body = getBodyFromConsole();
                Message message = new Message(body,client.getUsername());
                client.getServer().sendMessage(message);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("Finished");
    }

    private static String getBodyFromConsole() {
        return scanner.nextLine();
    }

    public void run(){
        while(true) {
            try {
                Thread.sleep(500);
                if(!bufferedMessages.isEmpty()){
                    Thread.sleep(5000);
                    for (Message message: bufferedMessages){
                        System.out.println(message);
                    }
                    bufferedMessages.clear();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
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

    @Override
    public ServerInterface getServer() throws RemoteException {
        return this.server;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}