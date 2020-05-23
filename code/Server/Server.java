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
import code.OpPackets.JoinRoom;
import code.OpPackets.JoinRoomResp;
import code.OpPackets.ListRooms;
import code.OpPackets.ListRoomsResp;
import code.OpPackets.ListUsers;
import code.OpPackets.ListUsersResponse;
import code.OpPackets.SendMessage;
import code.OpPackets.SendMessageResp;

/**
 * Server class for the server side of the IRC
 */
public class Server extends Thread {

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
        server.start();
    }

    public void run() {
        try {
            while (true) {

                System.out.println("ServerSocket awaiting connections...");
                Socket newConnection = this.getWelcomeSocket().accept();

                ObjectInputStream inFromClient = new ObjectInputStream(newConnection.getInputStream());

                IRC_Packet clientPacket = (IRC_Packet) inFromClient.readObject();

                //DON'T DELETE!!!!
             //    TimeUnit.SECONDS.sleep(20); This will be used to show handling crashed gracefully

                ObjectOutputStream outToClient = new ObjectOutputStream(newConnection.getOutputStream());
                outToClient.writeObject(this.handleRequestFromClient(clientPacket));

                newConnection.close();
            }
        } catch (Exception exception) {
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
        Room room = null;
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
                ListUsers listUsersPacket = (ListUsers) request;
                return new ListUsersResponse(this.getRoom(listUsersPacket.getRoomName()).getUsers());
            case OP_CODE_JOIN_ROOM:
                JoinRoom joinRoom = (JoinRoom) request;
                if(!this.doesRoomExist(joinRoom.getRoomName())) {
                    this.addRoom(joinRoom.getRoomName());
                }
                room = this.getRoom(joinRoom.getRoomName());
                room.addUser(new User(joinRoom.getUsername(), joinRoom.getPortNumber()));
                System.out.println("Adding client to room: " + joinRoom.getRoomName() + " with port: " + joinRoom.getPortNumber());
                return new JoinRoomResp();
            case OP_CODE_LEAVE_ROOM:
                break;
            case OP_CODE_SEND_MESSAGE: // Send msg to a room from client
                SendMessage msg = (SendMessage) request;
                System.out.println("line 125");
                room = this.getRoom(msg.getRoomName());
                System.out.println("127");
                room.setMessageToForward(msg);
                System.out.println("129");
                room.start();
                return new SendMessageResp();
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
    private ArrayList<String> getUsers() {
        return this.users.isEmpty() ? null : this.users;
    }
    private Room getRoom(String roomName) { return this.rooms.get(roomName); }
    private boolean doesRoomExist(String name) { return this.rooms.keySet().contains(name); }
    private void addRoom(String name) { this.rooms.put(name, new Room(name)); }
}