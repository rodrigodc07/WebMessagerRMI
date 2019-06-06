package UERJ.server;

import UERJ.Message;
import UERJ.client.ClientInterface;

import java.io.IOException;
import java.io.Serializable;
import java.net.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

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
        Registry reg = null;
        try {
            reg = LocateRegistry.createRegistry(port);
        } catch (Exception e) {
            System.out.println("ERROR: Could not create the registry.");
            e.printStackTrace();
        }
        System.out.println("Waiting...");
        try {
            reg.rebind("server_" + user, (ServerInterface) UnicastRemoteObject.exportObject(this, 0));
        } catch (Exception e) {
            System.out.println("ERROR: Failed to register the server object.");
            e.printStackTrace();
        }
        boolean hasBound = false;
        while (!hasBound) {
            try {
                Registry registry = LocateRegistry.getRegistry("127.0.0.1", port);
                this.client = (ClientInterface) registry.lookup("client_" + user);
                hasBound = true;
                System.out.println("Connected to UERJ.Client");
            }
            catch (NotBoundException ignored) {}
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
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
