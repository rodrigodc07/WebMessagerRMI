package UERJ;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    private String body;
    private Date dataEnvio;

    public Message(String body) {
        this.body = body;
        this.dataEnvio = new Date();
    }

    public String getBody() {
        return body;
    }

    public Date getDataEnvio() {
        return dataEnvio;
    }

    @Override
    public String toString() {
        return getBody() + " enviado as:" + getDataEnvio();
    }
}
