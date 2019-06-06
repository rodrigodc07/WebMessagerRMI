package UERJ.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MulticastPublisher {
    private static DatagramSocket socket;
    private static InetAddress group;
    private static byte[] buf;
 
    public static void multicast(
      String multicastMessage) throws IOException {
        socket = new DatagramSocket();
        group = InetAddress.getByName("230.0.0.0");
        buf = multicastMessage.getBytes();
 
        DatagramPacket packet
          = new DatagramPacket(buf, buf.length, group, 4446);
        socket.send(packet);
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        multicast("Hello");
    }
}