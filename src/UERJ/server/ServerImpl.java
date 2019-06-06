package UERJ.server;

import UERJ.Message;
import UERJ.client.ClientInterface;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerImpl extends Thread implements ServerInterface, Serializable {

    private ClientInterface client;
    private boolean hasClient;

    protected static MulticastSocket socket = null;
    protected static byte[] buf = new byte[256];


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
    public void sendMessage(Message message) throws RemoteException {
        System.out.println("Enviando Menssagem via Socket");
    }

    public static void main(String[] args) {
        Registry reg = null;
        int port = Integer.parseInt(args[0]);
        String user = args[1];
        try {
            reg = LocateRegistry.createRegistry(port);
        } catch (Exception e) {
            System.out.println("ERROR: Could not create the registry.");
            e.printStackTrace();
        }
        ServerImpl server = new ServerImpl();
        System.out.println("Waiting...");
        try {
            reg.rebind("server_" + user, (ServerInterface) UnicastRemoteObject.exportObject(server, 0));
        } catch (Exception e) {
            System.out.println("ERROR: Failed to register the server object.");
            e.printStackTrace();
        }
        try {
            socket = new MulticastSocket(4446);
            InetAddress group = InetAddress.getByName("230.0.0.0");
            socket.joinGroup(group);
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(
                        packet.getData(), 0, packet.getLength());
                if(server.hasClient())
                    server.client.pullMessages(new Message(received));
                if ("end".equals(received)) {
                    break;
                }
            }
            socket.leaveGroup(group);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
