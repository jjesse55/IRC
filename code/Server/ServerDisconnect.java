package code.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.lang.Object;
import code.IRC_Packets.IRC_Packet;
import code.OpPackets.KeepAlive;


public class ServerDisconnect {

    private Socket keepAliveSocket;
    protected static final String CLIENT_HOSTS = "localhost";

    public ArrayList<User> sendKeepAliveMessages(ArrayList<User> users) {
        ArrayList<User> usersToRemove = new ArrayList<>();
        for (User user : users) {
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

    private void openKeepAliveSocket(User user) {
        try {
            this.keepAliveSocket = new Socket(CLIENT_HOSTS, user.getPortNumber());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unable to open the client socket");
        }
    }

    private void closeKeepAliveSocket() {
        try {
            this.keepAliveSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Unable to close the server socket");
        }
    }

    private Socket getKeepAliveSocket() { return this.keepAliveSocket; }
}