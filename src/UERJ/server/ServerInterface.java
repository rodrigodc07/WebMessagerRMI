package UERJ.server;

import UERJ.Message;
import UERJ.observer.Subject;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface ServerInterface extends Remote, Subject {

   public void sendMessage(Message message) throws RemoteException;
}