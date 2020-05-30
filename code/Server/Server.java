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
import code.ErrorPackets.InvalidRoomName;
import code.IRC_Packets.IRC_Packet;
import code.OpPackets.GoodBye;
import code.OpPackets.HandShake;
import code.OpPackets.JoinRoom;
import code.OpPackets.JoinRoomResp;
import code.OpPackets.LeaveRoom;
import code.OpPackets.LeaveRoomResp;
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

    private ServerDisconnect serverDisconnect;

    //Information about clients that the server keeps track of
    ArrayList<User> users = new ArrayList<>();
    HashMap<String, Room> rooms = new HashMap<>();

    /**
     * Main program that runs the IRC server
     * 
     * @param notUsed
     */
    public static void main(String[] notUsed) throws Exception {
        Server server = new Server();
        server.start();
        while(true) {
            for(User user : server.serverDisconnect.sendKeepAliveMessages(server.users)) {
                server.users.remove(user);
                for(String roomName : server.rooms.keySet()) {
                    if(server.rooms.get(roomName).containsUser(user.getUsername()))
                        server.rooms.remove(user.getUsername());
                }
            }
            Thread.sleep(5000);
        }
    }

    public void run() {
        while (true) {
            try {
                System.out.println("ServerSocket awaiting connections...");
                Socket newConnection = this.getWelcomeSocket().accept();

                ObjectInputStream inFromClient = new ObjectInputStream(newConnection.getInputStream());

                IRC_Packet clientPacket = (IRC_Packet) inFromClient.readObject();

                //DON'T DELETE!!!!
             //    TimeUnit.SECONDS.sleep(20); This will be used to show handling crashed gracefully

                ObjectOutputStream outToClient = new ObjectOutputStream(newConnection.getOutputStream());

                System.out.println("Right before sending the response back to the client");
                outToClient.writeObject(this.handleRequestFromClient(clientPacket));

                newConnection.close();
            } catch(IOException ex){
                //TODO no clue here
                System.out.println("Err: IO Exception");
                System.exit(0);
            } catch(ClassNotFoundException exception){
                //TODO ERROR and try again?
                System.out.println("ERR: Class Not Found");

            } catch(Exception exception){
                //TODO error and try again 
                exception.printStackTrace();
               System.out.println("ERR: exception");
                //System.exit(0);
            }
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
        this.serverDisconnect = new ServerDisconnect();
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

                this.users.add(handShake.getUser());
                return new HandShake(null);
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
                LeaveRoom rqst = (LeaveRoom) request;
                room = this.rooms.get(rqst.getRoomName());
                if(room == null)
                    return new InvalidRoomName();
                String usrExiting = rqst.getUsername();
                if(room.containsUser(usrExiting)) {
                    room.removeUser(usrExiting);
                    if(room.isEmpty())
                        this.rooms.remove(rqst.getRoomName());
                }
                return new LeaveRoomResp();
            case OP_CODE_SEND_MESSAGE: // Send msg to a room from client
                SendMessage msg = (SendMessage) request;
                System.out.println("line 125");
                room = this.getRoom(msg.getRoomName());
                System.out.println("127");
                room.setMessageToForward(msg);
                System.out.println("129");
                if(room.getState() == Thread.State.NEW)
                    room.start();
                else
                    room.run();
                return new SendMessageResp();
            case OP_CODE_SEND_PRIVATE_MESSAGE:
                break;
            case OP_CODE_GOODBYE:
                GoodBye goodBye = (GoodBye) request;
                String userExiting = goodBye.getUsername();
                if(this.users.contains(userExiting))
                    this.users.remove(userExiting);
                for(String roomName : this.rooms.keySet()) {
                    room = this.rooms.get(roomName);
                    if(room.containsUser(userExiting)) {
                        room.removeUser(userExiting);
                        if(room.isEmpty())
                            this.rooms.remove(roomName);
                    }
                }
                return new GoodBye("server");
            default:
                return new IllegalOpcode();
        }

        return new UnknownError();
    }






    //Helper methods
    private boolean nameExists(HandShake handShake) {
        for(User user : this.users)
            if(user.getUsername() == handShake.getUser().getUsername())
                return true;
        
        return false;
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
    private ArrayList<String> getUsernames() {
        if(this.users.isEmpty()) return null;

        ArrayList<String> usernames = new ArrayList<>();
        for(User user : this.users)
            usernames.add(user.getUsername());

        return usernames;
    }
    private Room getRoom(String roomName) { return this.rooms.get(roomName); }
    private boolean doesRoomExist(String name) { return this.rooms.keySet().contains(name); }
    private void addRoom(String name) { this.rooms.put(name, new Room(name)); }
}