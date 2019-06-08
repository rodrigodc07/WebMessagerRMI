package UERJ.client;

import UERJ.Message;

import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientMain {

    private static Scanner scanner = new Scanner(System.in);

    private static String getBodyFromConsole() {
        return scanner.nextLine();
    }

    private static void clearInputedString(){
        System.out.print("\033[1A"); // Move up
        System.out.print("\033[2K"); // Erase line content
    }

    public static void main(String[] args) {

        ClientInterface client = new ClientImpl(Integer.parseInt(args[0]),args[1]);
        Thread t1 = new Thread((Runnable) client);
        t1.start();
        try {
            while(true) {
                System.out.print(client.getUsername()+": ");
                String body = getBodyFromConsole();
                clearInputedString();
                Message message = new Message(body,client.getUsername());
                client.sendMessage(message);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("Finished");
    }

}
