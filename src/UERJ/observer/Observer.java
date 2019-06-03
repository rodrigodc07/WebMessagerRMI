package UERJ.observer;

import UERJ.Message;

import java.io.Serializable;

public interface Observer extends Serializable {

    public void notify(Message message);
}
