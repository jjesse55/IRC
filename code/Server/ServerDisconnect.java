package code.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import code.OpPackets.GoodBye;
import code.OpPackets.KeepAlive;


/**
 * Class to implement both the keep alive messages to all the users in the system
 * AND
 * Notify all the clients upon the server disconnecting for maintenance, upgrades, etc.
 */
public class ServerDisconnect extends Thread {

    //Class fields
    private Socket keepAliveSocket;
    private Socket disconnectSocket;
    private static final String CLIENT_HOSTS = "localhost";

    private ArrayList<User> users;
    private String adminPassword = "adminPassword";


    //Class methods
    public ServerDisconnect(ArrayList<User> users) { this.users = users; }

    /**
     * Method that waits for an admin password to disconnect the server from all clients
     */
    public void run() {
        String passwordAttempt = null;
        while(!this.adminPassword.equals(passwordAttempt)) {
            Scanner scanner = new Scanner(System.in);
            passwordAttempt = scanner.nextLine();
            if(!passwordAttempt.equals(this.adminPassword)) {
                System.out.println("ERR: Incorrrect admin password entered. Please try again.");
                continue;
            }

            for (User user : this.users) {
                try {
                    System.out.println("LOG: Notifying user: " + user.getUsername() + " of server disconnection.");

                    this.openDisconnectSocket(user);
                    ObjectOutputStream outToUser = new ObjectOutputStream(this.getDisconnectSocket().getOutputStream());
    
                    outToUser.writeObject(new GoodBye("server"));
    
                    ObjectInputStream inFromUser = new ObjectInputStream(this.getDisconnectSocket().getInputStream());
    
                    GoodBye resp = (GoodBye) inFromUser.readObject();

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

    /**
     * Method that sends keep alive messages to all clients connected to the server
     * Is invoked in server.java in five second increments
     */
    public ArrayList<User> sendKeepAliveMessages() {
        ArrayList<User> usersToRemove = new ArrayList<>();
        for (User user : this.users) {
            try {
                this.openKeepAliveSocket(user);
                ObjectOutputStream outToUser = new ObjectOutputStream(this.getKeepAliveSocket().getOutputStream());

                outToUser.writeObject(new KeepAlive());

                ObjectInputStream inFromUser = new ObjectInputStream(this.getKeepAliveSocket().getInputStream());

                KeepAlive resp = (KeepAlive) inFromUser.readObject();

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