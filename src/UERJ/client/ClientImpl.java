package UERJ.client;

import UERJ.Message;
import UERJ.RMIUtils.RMIRegistry;
import UERJ.server.ServerInterface;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class ClientImpl implements ClientInterface, Serializable, Runnable {

    private ServerInterface server;

    private ArrayList<Message> bufferedMessages= new ArrayList<Message>();

    private static Scanner scanner = new Scanner(System.in);
    private String username;

    private ClientImpl(int port, String user) {

        ServerInterface server = (ServerInterface) RMIRegistry.getObjectFromRMI(port,"server");
        System.out.println("Connected to UERJ.Server");

        this.server = server;
        this.username = user;

        RMIRegistry.registryInRMI(port,"client_" + user,this);
    }

    public static void main(String[] args) {

        ClientInterface client = new ClientImpl(Integer.parseInt(args[0]),args[1]);
        Thread t1 = new Thread((Runnable) client);
        t1.start();
        try {
            while(true) {
                System.out.print(client.getUsername()+": ");
                String body = getBodyFromConsole();
                clearInputedString();
                Message message = new Message(body,client.getUsername());
                client.sendMessage(message);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("Finished");
    }

    private static String getBodyFromConsole() {
        return scanner.nextLine();
    }

    private static void clearInputedString(){
        System.out.print("\033[1A"); // Move up
        System.out.print("\033[2K"); // Erase line content
    }

    private static void clearUsername(String username){
        String moveBackString = "\b";
        String clearString = "";
        for (Character ignored :username.toCharArray()){
            moveBackString = moveBackString.concat("\b");
            clearString = clearString.concat(" ");
        }
        System.out.print(moveBackString);
        System.out.print(clearString);
        System.out.print(moveBackString);
    }

    public void run(){
        while(true) {
            try {
                Thread.sleep(500);
                if(!bufferedMessages.isEmpty()){
                    Thread.sleep(500);
                    clearUsername(username);
                    bufferedMessages.sort(Comparator.comparing(Message::getDataEnvio).reversed());
                    for (Message message: bufferedMessages){
                        System.out.println(message);
                        System.out.print(username + ": ");
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
    public void pullMessages(Message message) throws RemoteException {
        bufferedMessages.add(message);
    }

    @Override
    public void sendMessage(Message message) throws RemoteException {
        server.sendMessage(message);
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}