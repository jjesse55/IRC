package code.Server;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketException;

import java.util.concurrent.TimeUnit;

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
    private static int protocol = 0x12345678;

    /**
     * Main program that runs the IRC server
     * 
     * @param notUsed
     */
    public static void main(String[] notUsed) throws Exception {
        Server server = new Server();
        try {
            while (true) {

                System.out.println("ServerSocket awaiting connections...");
                Socket newConnection = server.welcomeSocket.accept();
                System.out.println("Client connected to the server");

                ObjectInputStream inFromClient = new ObjectInputStream(newConnection.getInputStream());
                System.out.println("Created the object input stream");

                IRC_Packet clientPacket = (IRC_Packet) inFromClient.readObject();

                System.out.println("Recieved obj from the client");

             //    TimeUnit.SECONDS.sleep(20);

                ObjectOutputStream outToClient = new ObjectOutputStream(newConnection.getOutputStream());
                System.out.println("Created the object output stream");
                outToClient.writeObject(new HandShake(OpCodes.OP_CODE_HELLO, "username"));

                newConnection.close();
            }
        } catch (SocketException exception) {
            System.out.println("ERR: The client has no longer become responsive. Proceeding as normal");
            
        }
    }

    // Class methods

    /**
     * Private constructor to keep from outside use.
     * 
     * @throws IOException
     */
    private Server() throws IOException {
        this.welcomeSocket = new ServerSocket(port);
    }

    public boolean verifyProtocol(HandShake handShake) {
        return handShake.getProtocol() == protocol;
    }
}