package code.Server;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import code.Codes.ErrorCodes;
import code.Codes.OpCodes;
import code.ErrorPackets.IllegalOpcode;
import code.ErrorPackets.IllegalProtocol;
import code.ErrorPackets.NameExists;
import code.ErrorPackets.UnknownError;
import code.IRC_Packets.IRC_Packet;
import code.OpPackets.HandShake;
import code.OpPackets.ListRooms;
import code.OpPackets.ListRoomsResp;

/**
 * Server class for the server side of the IRC
 */
public class Server {

    // Class fields
    private static final int port = 7777; // Port number for the server process
    private ServerSocket welcomeSocket;
    private static int protocol = 0x12345678;

    //Information about clients that the server keeps track of
    ArrayList<String> users = new ArrayList<>();
    HashMap<String, Room> rooms = new HashMap<>();

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
                Socket newConnection = server.getWelcomeSocket().accept();
                System.out.println("Client connected to the server");

                ObjectInputStream inFromClient = new ObjectInputStream(newConnection.getInputStream());
                System.out.println("Created the object input stream");

                IRC_Packet clientPacket = (IRC_Packet) inFromClient.readObject();

                System.out.println("Recieved obj from the client");

                //DON'T DELETE!!!!
             //    TimeUnit.SECONDS.sleep(20); This will be used to show handling crashed gracefully

                ObjectOutputStream outToClient = new ObjectOutputStream(newConnection.getOutputStream());
                System.out.println("Created the object output stream");
                outToClient.writeObject(server.handleRequestFromClient(clientPacket));

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

    /**
     * READ THIS!!! This function determines what type of request packet is sent
     * based on the opCode and then calls the functionality on that object once it
     * is dynamically casted.
     * 
     * @param response
     */
    private IRC_Packet handleRequestFromClient(IRC_Packet request) {
        switch (request.getPacketHeader().getOpCode()) {
            case OP_CODE_HELLO:
                HandShake handShake = (HandShake) request;
                if(!this.verifyProtocol(handShake))
                    return new IllegalProtocol();
                
                if(this.nameExists(handShake))
                    return new NameExists();

                this.users.add(handShake.getUserName());
                return new HandShake("server");
            case OP_CODE_LIST_ROOMS:
                return new ListRoomsResp(this.getRooms());
            case OP_CODE_LIST_USERS:
                break;
            case OP_CODE_JOIN_ROOM:
                break;
            case OP_CODE_LEAVE_ROOM:
                break;
            case OP_CODE_SEND_MESSAGE: // Send msg to a room from client
                break;
            case OP_CODE_SEND_PRIVATE_MESSAGE:
                break;
            default:
                return new IllegalOpcode();
        }

        return new UnknownError();
    }






    //Helper methods
    private boolean nameExists(HandShake handShake) {
        return this.users.contains(handShake.getUserName());
    }

    private boolean verifyProtocol(HandShake handShake) {
        return handShake.getProtocol() == protocol;
    }






    //Getters
    private ServerSocket getWelcomeSocket() {
        return this.welcomeSocket;
    }
    private ArrayList<String> getRooms() {
        return this.rooms.isEmpty() ? null : new ArrayList<String>(this.rooms.keySet());
    }
}