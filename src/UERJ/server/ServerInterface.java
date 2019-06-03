package UERJ.server;

import UERJ.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface ServerInterface extends Remote {

   public String getBody() throws RemoteException;

   public Date getDate() throws RemoteException;

   public Message getMessage() throws RemoteException;

   public void sendMessage(Message message) throws RemoteException;
}