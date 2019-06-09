package UERJ.server;

import UERJ.Message;
import UERJ.RMIUtils.RMIRegistry;
import UERJ.client.ClientInterface;
import UERJ.input.MessageReceiverService;
import UERJ.input.MulticastSocketReciver;
import UERJ.output.MessageSenderService;
import UERJ.output.MulticastSocketServer;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;

public class ServerImpl implements ServerInterface, Serializable {

    private int port;

    private MessageSenderService messageSenderService;
    private MessageReceiverService messageReceiverService;

    @Override
    public void sendMessage(Message message) throws RemoteException {
        messageSenderService.send(message);
    }

    @Override
    public void pushMessage(Message message) throws RemoteException {
        List<String> clients_name = RMIRegistry.listObjects(port);
        for (String client_name:clients_name){
            ClientInterface client = (ClientInterface) RMIRegistry.getObjectFromRMI(port, client_name);
            client.pullMessages(message);
        }
    }

    public ServerImpl(int port) {
        this.port = port;
        RMIRegistry.registryInRMI(port,"server",this);
        this.messageSenderService = new MulticastSocketServer();
        this.messageReceiverService = new MulticastSocketReciver( s -> {
            try {
                pushMessage(s);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        Thread t = new Thread((Runnable) this.messageReceiverService);
        t.start();
    }

}
