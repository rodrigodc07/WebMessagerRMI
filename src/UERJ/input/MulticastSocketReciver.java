package UERJ.input;

import UERJ.Message;
import UERJ.properties.JavaProperties;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import static UERJ.properties.JavaProperties.getJavaProperties;

public class MulticastSocketReciver implements MessageReceiverService, Runnable{

    private final int port;
    private final String address;
    private MulticastSocket socketListener = null;
    private byte[] buf = new byte[1024];
    private Lambda lambda;

    public MulticastSocketReciver(Lambda lambda) {
        this.lambda = lambda;
        JavaProperties javaProperties = getJavaProperties();
        port = Integer.parseInt(javaProperties.getProperty("socket.port"));
        address = javaProperties.getProperty("socket.address");
    }

    @Override
    public void run() {
        try {
            socketListener = new MulticastSocket(port);
            InetAddress group = InetAddress.getByName(address);
            socketListener.joinGroup(group);
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socketListener.receive(packet);
                Message received = new Message(packet.getData());
                received.setDataChegada();
                lambda.execute(received);
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
