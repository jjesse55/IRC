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
    private static final String CLIENT_HOSTS = "localhost";

    private final ArrayList<User> USERS;
    private final String ADMIN_PASSWORD = "adminPassword";

    public ServerDisconnect(ArrayList<User> users) { this.USERS = users; }

    public void run() {
        String passwordAttempt = null;
        while(!this.ADMIN_PASSWORD.equals(passwordAttempt)) {
            Scanner scanner = new Scanner(System.in);
            passwordAttempt = scanner.nextLine();
            if(!passwordAttempt.equals(this.ADMIN_PASSWORD)) {
                System.out.println("ERR: Incorrrect admin password entered. Please try again.");
                continue;
            }

            for (User user : this.USERS) {
                try {
                    System.out.println("LOG: Notifying user: " + user.getUsername() + " of server disconnection.");

                    this.openDisconnectSocket(user);
                    ObjectOutputStream outToUser = new ObjectOutputStream(this.getDisconnectSocket().getOutputStream());
    
                    outToUser.writeObject(new GoodBye("server"));
    
                    ObjectInputStream inFromUser = new ObjectInputStream(this.getDisconnectSocket().getInputStream());
    
                    inFromUser.readObject();

                    System.out.println("LOG: Confirmed server disconncetion from user: " + user.getUsername());
    
                    inFromUser.close();
                    closeDisconnectSocket();
    
                } catch (Exception e) {
                    System.out.println("ERR: Could not successfully send notice of server disconnect to user: "
                    + user.getUsername());
                }
            }

            System.out.println("LOG: Server disconnected from all users, now terminating...");
            System.exit(0);
        }
    }

    public ArrayList<User> sendKeepAliveMessages() {
        ArrayList<User> usersToRemove = new ArrayList<>();
        for (User user : this.USERS) {
            try {
                this.openKeepAliveSocket(user);
                ObjectOutputStream outToUser = new ObjectOutputStream(this.getKeepAliveSocket().getOutputStream());

                outToUser.writeObject(new KeepAlive());

                ObjectInputStream inFromUser = new ObjectInputStream(this.getKeepAliveSocket().getInputStream());

                inFromUser.readObject();

                inFromUser.close();
                closeKeepAliveSocket();

            } catch (Exception e) {
                usersToRemove.add(user);
            }
        }

        return usersToRemove;
    }

    private void openKeepAliveSocket(User user) throws Exception {
        this.keepAliveSocket = new Socket(CLIENT_HOSTS, user.getPortNumber());
    }

    private void openDisconnectSocket(User user) throws Exception {
        this.disconnectSocket = new Socket(CLIENT_HOSTS, user.getPortNumber());
    }

    private void closeKeepAliveSocket() throws Exception {
        this.keepAliveSocket.close();
    }

    private void closeDisconnectSocket() throws Exception {
        this.disconnectSocket.close();
    }

    private Socket getKeepAliveSocket() { return this.keepAliveSocket; }
    private Socket getDisconnectSocket() { return this.disconnectSocket; }
}