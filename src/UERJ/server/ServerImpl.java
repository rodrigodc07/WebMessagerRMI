package UERJ.server;

import UERJ.Message;
import UERJ.RMIUtils.RMIRegistry;
import UERJ.client.ClientInterface;
import UERJ.input.MessageReceiverService;
import UERJ.input.MulticastSocketMessageReceiverService;
import UERJ.output.MessageSenderService;
import UERJ.output.MulticastSocketMessageSenderServer;
import UERJ.properties.JavaProperties;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

import static UERJ.properties.JavaProperties.getJavaProperties;

public class ServerImpl implements ServerInterface, Serializable {

    private int port;

    private MessageSenderService messageSenderService;
    private MessageReceiverService messageReceiverService;

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

    public ServerImpl() {
        JavaProperties javaProperties = getJavaProperties();
        this.port = Integer.parseInt(javaProperties.getProperty("rmi.port"));

        RMIRegistry.registryInRMI(port,"server",this);
        this.messageSenderService = new MulticastSocketMessageSenderServer();
        this.messageReceiverService = new MulticastSocketMessageReceiverService(s -> {
            try {
                pushMessage(s);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        Thread t = new Thread((Runnable) this.messageReceiverService);
        t.start();
    }

}
