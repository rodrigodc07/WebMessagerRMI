package UERJ;

import java.io.*;
import java.util.Date;

public class Message implements Serializable {

    private String body;
    private Date dataEnvio;
    private String from;

    public Message(String body, String username) {
        this.body = body;
        this.dataEnvio = new Date();
        this.from = username;
    }

    public Message(byte[] stream) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(stream);
             ObjectInputStream ois = new ObjectInputStream(bais);) {
            Message m = (Message) ois.readObject();
            this.body = m.getBody();
            this.dataEnvio = new Date();
            this.from = m.getFrom();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getBody() {
        return body;
    }

    public Date getDataEnvio() {
        return dataEnvio;
    }

    public String getFrom() {
        return from;
    }

    public byte[] toStream() {
        byte[] stream = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos);) {
            oos.writeObject(this);
            stream = baos.toByteArray();
        } catch (IOException e) {
            // Error in serialization
            e.printStackTrace();
        }
        return stream;
    }

    @Override
    public String toString() {
        return getFrom() + " " + getBody() + " enviado as:" + getDataEnvio();
    }
}
