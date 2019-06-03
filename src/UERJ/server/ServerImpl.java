package UERJ.server;

import UERJ.Message;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Stack;

public class ServerImpl implements ServerInterface{

    private Stack<Message> messageList = new Stack<>();
    private Message top;

    @Override
    public String getBody() throws RemoteException {
        if(messageList.empty())
            return null;
        return top.getBody();
    }

    @Override
    public Date getDate() throws RemoteException {
        if(messageList.empty())
            return null;
        return top.getDataEnvio();
    }

    @Override
    public Message getMessage() throws RemoteException {
        if(messageList.empty())
            return null;
        top =  messageList.pop();
        return top;
    }

    @Override
    public void sendMessage(Message message) throws RemoteException {
        messageList.push(message);
    }

    public static void main(String[] args) {
        Registry reg = null;
        try {
            reg = LocateRegistry.createRegistry(1099);
        } catch (Exception e) {
            System.out.println("ERROR: Could not create the registry.");
            e.printStackTrace();
        }
        ServerImpl serverObject = new ServerImpl();
        System.out.println("Waiting...");
        try {
            reg.rebind("server", (ServerInterface) UnicastRemoteObject.exportObject(serverObject, 0));
        } catch (Exception e) {
            System.out.println("ERROR: Failed to register the server object.");
            e.printStackTrace();
        }
    }
}
