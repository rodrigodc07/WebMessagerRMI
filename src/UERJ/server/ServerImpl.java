package UERJ.server;

import UERJ.Message;
import UERJ.RMIUtils.RMIRegistry;
import UERJ.client.ClientInterface;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.RemoteException;

public class ServerImpl implements ServerInterface, Serializable, Runnable {

    private ClientInterface client;
    private boolean hasClient;

    private MulticastSocket socketListener = null;
    private byte[] buf = new byte[1024];
    private DatagramSocket socket;
    private InetAddress group;

    @Override
    public void register(ClientInterface client) throws RemoteException {
        this.client = client;
        this.hasClient = true;
    }

    @Override
    public boolean hasClient() throws RemoteException {
        return hasClient;
    }

    @Override
    public ClientInterface getClient() throws RemoteException {
        return this.client;
    }

    @Override
    public void sendMessage(Message message) throws RemoteException {
        try {
            System.out.println("Enviando Menssagem via Socket");
            multicast(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void multicast(Message message) throws IOException {
        socket = new DatagramSocket();
        group = InetAddress.getByName("230.0.0.0");
        buf = message.toStream();

        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
        socket.send(packet);
        socket.close();
    }

    public ServerImpl(int port, String user) {
        RMIRegistry.registryInRMI(port,"server_" + user,this);

        this.client = (ClientInterface) RMIRegistry.getObjectFromRMI(port,"client_" + user);
        System.out.println("Connected to UERJ.Client");

    }

    public static void main(String[] args) {
        ServerInterface server = new ServerImpl(Integer.parseInt(args[0]),args[1]);
        Thread t = new Thread((Runnable) server);
        t.start();
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
                if(this.hasClient())
                    this.getClient().pullMessages(received);
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
