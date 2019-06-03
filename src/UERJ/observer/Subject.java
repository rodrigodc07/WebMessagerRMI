package UERJ.observer;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface Subject extends Serializable {

    public void register(Observer observer) throws RemoteException;
}
