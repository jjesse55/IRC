package code.Server;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;

import code.Codes.OpCodes;
import code.IRC_Packets.IRC_Packet;
import code.OpPackets.HandShake;

/**
 * Server class for the server side of the IRC
 */
public class Server {

    // Class fields
    private static final int port = 7777; // Port number for the server process
    private ServerSocket welcomeSocket;

    /**
     * Main program that runs the IRC server
     * 
     * @param notUsed
     */
    public static void main(String[] notUsed) throws Exception {
        Server server = new Server();

        while (true) {
            System.out.println("ServerSocket awaiting connections...");
            Socket newConnection = server.welcomeSocket.accept();
            System.out.println("Client connected to the server");

            ObjectInputStream inFromClient = new ObjectInputStream(newConnection.getInputStream());
            System.out.println("Created the object input stream");

            IRC_Packet clientPacket = (IRC_Packet) inFromClient.readObject();

            System.out.println("Recieved obj from the client");

            ObjectOutputStream outToClient = new ObjectOutputStream(newConnection.getOutputStream());
            System.out.println("Created the object output stream");
            outToClient.writeObject(new HandShake(OpCodes.OP_CODE_HELLO, "username", "hello"));

            newConnection.close();
            server.welcomeSocket.close();

            System.out.println("Closing the server sockets.");
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