package UERJ.client;

import UERJ.Message;
import UERJ.RMIUtils.RMIRegistry;
import UERJ.properties.JavaProperties;
import UERJ.server.ServerInterface;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Comparator;

import static UERJ.properties.JavaProperties.getJavaProperties;

public class ClientImpl implements ClientInterface, Serializable, Runnable {

    private int port;
    private String username;
    private ServerInterface server;
    private ArrayList<Message> messageBuffer = new ArrayList<Message>();
    private ArrayList<Message> messageHistory = new ArrayList<Message>();

    public ClientImpl(String user) {

        JavaProperties javaProperties = getJavaProperties();
        this.port = Integer.parseInt(javaProperties.getProperty("rmi.port"));

        ServerInterface server = (ServerInterface) RMIRegistry.getObjectFromRMI(port,"server");
        System.out.println("Connected to UERJ.Server");

        this.server = server;
        this.username = user;

        try {
            checkIn();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        RMIRegistry.registryInRMI(port,"client_" + user,this);
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
            monitorMessageBuffer();
        }
    }

    private void monitorMessageBuffer() {
        try {
            Thread.sleep(500);
            if(!messageBuffer.isEmpty()){
                Thread.sleep(500);
                clearUsername(username);
                messageBuffer.sort(Comparator.comparing(Message::getDataEnvio).reversed());
                for (Message message: messageBuffer){
                    System.out.println(message);
                    System.out.print(username + ": ");
                    messageHistory.add(message);
                }
                messageBuffer.clear();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pullMessages(Message message) throws RemoteException {
        messageBuffer.add(message);
    }

    @Override
    public void sendMessage(Message message) throws RemoteException {
        server.sendMessage(message);
    }

    @Override
    public void checkIn() throws RemoteException {
        sendMessage(new Message("Estou Entrando",username));
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}