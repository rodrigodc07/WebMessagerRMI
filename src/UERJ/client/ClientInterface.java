package UERJ.client;

import UERJ.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {

    public void pullMessages(Message message) throws RemoteException;

    public void sendMessage(Message message) throws RemoteException;

    public void checkIn() throws RemoteException;

    public String getUsername() throws RemoteException;
}
