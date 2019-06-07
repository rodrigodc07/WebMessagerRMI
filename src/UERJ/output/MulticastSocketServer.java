/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UERJ.output;

import UERJ.Message;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author LCC
 */
public class MulticastSocketServer implements MessageSenderService{
    
    private void multicast(Message message) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress group = InetAddress.getByName("230.0.0.0");
        byte[] buf = message.toStream();

        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
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
