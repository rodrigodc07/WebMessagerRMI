package UERJ.server;

public class ServerMain {

    public static void main(String[] args) {
        ServerInterface server = new ServerImpl(Integer.parseInt(args[0]));
        Thread t = new Thread((Runnable) server);
        t.start();
    }

}
