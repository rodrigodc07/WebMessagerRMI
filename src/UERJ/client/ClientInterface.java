package UERJ.client;

import UERJ.Message;
import UERJ.server.ServerInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {

    public void registryServer(ServerInterface server) throws RemoteException;

    public boolean hasServer() throws RemoteException;

    public void printMessages(Message message) throws RemoteException;
}
