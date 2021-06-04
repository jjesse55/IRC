package code.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import code.OpPackets.GoodBye;
import code.OpPackets.KeepAlive;

public class ServerDisconnect extends Thread {

    private Socket keepAliveSocket;
    private Socket disconnectSocket;

    private final ArrayList<User> USERS;
    private final String ADMIN_PASSWORD = "adminPassword";

    private static final String CLIENT_HOSTS = "localhost";

    public ServerDisconnect(ArrayList<User> users) { this.USERS = users; }

    public void run() {
        String passwordAttempt;
        Scanner scanner = new Scanner(System.in);
        do {
            passwordAttempt = scanner.nextLine();
        } while (!this.ADMIN_PASSWORD.equals(passwordAttempt));
        for (User user : this.USERS) {
            try {
                System.out.println("LOG: Notifying user: " + user.getUsername() + " of server disconnection.");
                this.openDisconnectSocket(user);
                ObjectOutputStream outToUser = new ObjectOutputStream(this.disconnectSocket.getOutputStream());
                outToUser.writeObject(new GoodBye("server"));
                ObjectInputStream inFromUser = new ObjectInputStream(this.disconnectSocket.getInputStream());
                inFromUser.readObject();
                System.out.println("LOG: Confirmed server disconnection from user: " + user.getUsername());
                inFromUser.close();
                this.closeDisconnectSocket();
            } catch (Exception e) {
                System.out.println("ERR: Could not successfully send notice of server disconnect to user: "
                + user.getUsername());
            }
        }
        System.out.println("LOG: Server disconnected from all users, now terminating...");
        System.exit(0);
    }

    private void openDisconnectSocket(User user) throws Exception {
        this.disconnectSocket = new Socket(CLIENT_HOSTS, user.getPortNumber());
    }

    private void closeDisconnectSocket() throws Exception {
        this.disconnectSocket.close();
    }

    public ArrayList<User> sendKeepAliveMessages() {
        ArrayList<User> usersToRemove = new ArrayList<>();
        for (User user : this.USERS) {
            try {
                this.openKeepAliveSocket(user);
                ObjectOutputStream outToUser = new ObjectOutputStream(this.keepAliveSocket.getOutputStream());
                outToUser.writeObject(new KeepAlive());
                ObjectInputStream inFromUser = new ObjectInputStream(this.keepAliveSocket.getInputStream());
                inFromUser.readObject();
                inFromUser.close();
                this.closeKeepAliveSocket();
            } catch (Exception e) {
                usersToRemove.add(user);
            }
        }
        return usersToRemove;
    }

    private void openKeepAliveSocket(User user) throws Exception {
        this.keepAliveSocket = new Socket(CLIENT_HOSTS, user.getPortNumber());
    }

    private void closeKeepAliveSocket() throws Exception {
        this.keepAliveSocket.close();
    }
}