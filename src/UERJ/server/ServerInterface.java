package UERJ.server;

import UERJ.Message;
import UERJ.client.ClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

   public void sendMessage(Message message) throws RemoteException;

   public void register(ClientInterface client) throws RemoteException;

   public boolean hasClient() throws RemoteException;
}