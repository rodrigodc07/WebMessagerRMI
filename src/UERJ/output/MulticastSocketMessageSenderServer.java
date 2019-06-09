package UERJ.output;

import UERJ.Message;
import UERJ.properties.JavaProperties;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static UERJ.properties.JavaProperties.getJavaProperties;

public class MulticastSocketMessageSenderServer implements MessageSenderService{

    private String address;
    private int port;

    public MulticastSocketMessageSenderServer() {
        JavaProperties javaProperties = getJavaProperties();
        port = Integer.parseInt(javaProperties.getProperty("socket.port"));
        address = javaProperties.getProperty("socket.address");
    }

    private void multicast(Message message) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress group = InetAddress.getByName(address);
        byte[] buf = message.toStream();

        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, port);
        socket.send(packet);
        socket.close();
    }    

    @Override
    public void send(Message message) {
        System.out.println("Enviando Menssagem via Socket");
        try {
            multicast(message);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
