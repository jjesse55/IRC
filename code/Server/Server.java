package code.Server;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import code.IRC_Packets.IRC_Packet;

/**
 * Server class for the server side of the IRC
 */
public class Server {

    // Class fields
    private static final int port = 194; // Port number for the server process
    private ServerSocket welcomeSocket;

    /**
     * Main program that runs the IRC server
     * 
     * @param notUsed
     */
    public static void main(String[] notUsed) throws Exception {
        Server server = new Server();

        while (true) {
            Socket newConnection = server.welcomeSocket.accept();

            ObjectInputStream inFromClient = new ObjectInputStream(newConnection.getInputStream());
            ObjectOutputStream outToClient = new ObjectOutputStream(newConnection.getOutputStream());

            IRC_Packet clientPacket = (IRC_Packet) inFromClient.readObject();
        }
    }
/**
 * 
 * TODO: Add a dictionary that holds both names of user and IP adress to decipher what
 * user is sending the msg
 */


    //Class methods

    /**
     * Private constructor to keep from outside use.
     * 
     * @throws IOException
     */
    private Server() throws IOException {
        this.welcomeSocket = new ServerSocket(port);
    }
}