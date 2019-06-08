package UERJ.server;

import UERJ.Message;
import UERJ.client.ClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

   public void sendMessage(Message message) throws RemoteException;

   public void pushMessage(Message message) throws RemoteException;

   public void checkIn(ClientInterface clientInterface) throws RemoteException;

}