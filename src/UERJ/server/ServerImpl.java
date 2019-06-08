package UERJ.server;

import UERJ.Message;
import UERJ.RMIUtils.RMIRegistry;
import UERJ.client.ClientInterface;
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

public class ServerImpl implements ServerInterface, Serializable, Runnable {

    private int port;

    private MessageSenderService messageSenderService;
    
    private MulticastSocket socketListener = null;
    private byte[] buf = new byte[1024];

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
    }

    @Override
    public void run() {
        try {
            socketListener = new MulticastSocket(4446);
            InetAddress group = InetAddress.getByName("230.0.0.0");
            socketListener.joinGroup(group);
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socketListener.receive(packet);
                Message received = new Message(packet.getData());
                received.setDataChegada();
                pushMessage(received);
                if ("end".equals(received.getBody())) {
                    break;
                }
            }
            socketListener.leaveGroup(group);
            socketListener.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
