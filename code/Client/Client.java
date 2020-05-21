package code.Client;

import java.util.concurrent.TimeUnit;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import code.Codes.OpCodes;
import code.IRC_Packets.IRC_Packet;
import code.OpPackets.HandShake;
import code.OpPackets.OpPackets;
import code.Client.ChatSwing;

public class Client {

    //Fields for connecting to the server
    private Socket clientSocket;
    private static final int SERVER_PORT = 7777; // Port number for the client process
    private static final String SERVER_HOST = "localhost";

    private ChatSwing gui;

    public static void main(String[] notUsed) throws Exception {
        Client client = new Client();

        try {
            ObjectOutputStream outToServer = new ObjectOutputStream(client.getClientSocket().getOutputStream());
            System.out.println("Created the object ouptut stream");

            outToServer.writeObject(new HandShake("my name is the user the best user ever"));
            System.out.println("Sending IRC packet to the server");

            ObjectInputStream inFromServer = new ObjectInputStream(client.getClientSocket().getInputStream());
            System.out.println("Created the object input stream");
            IRC_Packet irc_Packet = (IRC_Packet) inFromServer.readObject();

            System.out.println("Yay we got a response back from the server!!!!!!");
        } catch (SocketTimeoutException exception) {
            System.out.println("ERR: The server has no longer become responsive. Please try connecting again");
            System.exit(0);
        } catch (IOException exception) {

            System.out.println("ERR: The server has no longer become responsive. Please try connecting again");
            System.exit(0);

        }

        client.clientSocket.close();

        while(true) {}
    }

    // Class methods
    public Client() { 
        System.out.println("Connecting to the server...\nHost: " + SERVER_HOST + "\nPort: " + SERVER_PORT);
        try {
            this.clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
        } catch(Exception e) {
            System.err.println("ERR: Server process must be running before trying to connect with the client... System exiting");
            System.exit(1);
        }
        this.gui = new ChatSwing();
    }

    /**
     * Getter for the client socket
     * 
     * @return the clientSocket
     */
    public Socket getClientSocket() {
        return clientSocket;
    }

    /**
     * READ THIS!!! This function determines what type of response packet is sent
     * based on the opCode and then calls the functionality on that object once it
     * is dynamically casted.
     * 
     * @param response
     */
    public void handleResonseFromServer(IRC_Packet response) {
        switch (response.getPacketHeader().getOpCode()) {
            case OP_CODE_ERR:
                // Call the function for this specific object and so on...
                break;
            case OP_CODE_KEEP_ALIVE:
                break;
            case OP_CODE_HELLO:
                // TODO figure out where to create the chat swing! so i can actually have an
                // object
                // String n="";
                // String msg="";
                // IRC_Packet handshake= new HandShake(OpCodes.OP_CODE_HELLO, myChat.UserName,
                // msg);
                break;
            case OP_CODE_LIST_ROOMS_RESPONSE:

                break;
            case OP_CODE_LIST_USERS_RESP:
                break;
            case OP_CODE_JOIN_ROOM_RESP:
                break;
            case OP_CODE_LEAVE_ROOM_RESP:
                break;
        }
    }
}