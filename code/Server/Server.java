package code.Server;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import code.ErrorPackets.IllegalOpcode;
import code.ErrorPackets.IllegalProtocol;
import code.ErrorPackets.NameExists;
import code.ErrorPackets.UnknownError;
import code.ErrorPackets.InvalidRoomName;
import code.IRC_Packets.IrcPacket;
import code.OpPackets.GoodBye;
import code.OpPackets.HandShake;
import code.OpPackets.JoinRoom;
import code.OpPackets.JoinRoomResp;
import code.OpPackets.LeaveRoom;
import code.OpPackets.LeaveRoomResp;
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

    // Information about clients that the server keeps track of
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
        server.serverDisconnect.start();
        while (true) {
            ArrayList<User> usersToRemove = server.serverDisconnect.sendKeepAliveMessages();
            for (User user : usersToRemove) {
                System.out.println("LOG: Timeout recieved from user: " + user.getUsername()
                        + ". Removing the user from the system now...");
                for (Room room : server.rooms.values()) {
                    if (room.containsUser(user.getUsername())) {
                        room.removeUser(user.getUsername());
                        if (room.isEmpty())
                            server.rooms.remove(room.getRoomName());
                    }
                }
                server.users.remove(user);
            }
            Thread.sleep(5000);
        }
    }

    /**
     * thread for the welcome socket of the server accepts incoming connections
     */
    public void run() {
        while (true) {
            try {
                System.out.println("ServerSocket awaiting connections...");
                Socket newConnection = this.getWelcomeSocket().accept();

                ObjectInputStream inFromClient = new ObjectInputStream(newConnection.getInputStream());

                IrcPacket clientPacket = (IrcPacket) inFromClient.readObject();

                ObjectOutputStream outToClient = new ObjectOutputStream(newConnection.getOutputStream());
                outToClient.writeObject(this.handleRequestFromClient(clientPacket));

                newConnection.close();
            } catch (ClassNotFoundException exception) {
                System.err.println("ERR: Class Not Found");
            } catch (Exception exception) {
                exception.printStackTrace();
                System.err.println("ERR: Unable to exchange info with client");
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
        this.serverDisconnect = new ServerDisconnect(this.users);
    }

    /**
     * READ THIS!!! This function determines what type of request packet is sent
     * based on the opCode and then calls the functionality on that object once it
     * is dynamically casted.
     * 
     */
    private IrcPacket handleRequestFromClient(IrcPacket request) {
        Room room = null;
        switch (request.getPacketHeader().getOpCode()) {
            case OP_CODE_HELLO:
                HandShake handShake = (HandShake) request;
                System.out.println("LOG: Recieved handshake request from client: " + handShake.getUser().getUsername());
                if (!this.verifyProtocol(handShake))
                    return new IllegalProtocol();
                if (this.nameExists(handShake))
                    return new NameExists();
                this.users.add(handShake.getUser());
                System.out.println("LOG: Sending handshake to response back to client.");
                return new HandShake(null);

            case OP_CODE_LIST_ROOMS:
                System.out.println("LOG: Recieved request to list all rooms from client.");
                System.out.println("LOG: Sneding list all rooms response back to client.");
                return new ListRoomsResp(this.getRooms());

            case OP_CODE_LIST_USERS:
                ListUsers listUsersPacket = (ListUsers) request;
                System.out.println(
                        "LOG: Recieved list users request from client: for room " + listUsersPacket.getRoomName());
                room = this.getRoom(listUsersPacket.getRoomName());
                System.out.println(
                        "LOG: Sending list of users in room: " + listUsersPacket.getRoomName() + " back to client.");
                return room == null ? new ListUsersResponse(null) : new ListUsersResponse(room.getUsers());

            case OP_CODE_JOIN_ROOM:
                JoinRoom joinRoom = (JoinRoom) request;
                System.out.println("LOG: Recieved request from client: " + joinRoom.getUsername() + " to join room: "
                        + joinRoom.getRoomName());
                if (!this.doesRoomExist(joinRoom.getRoomName())) {
                    System.out.println("LOG: Name of room does not exist. Creating room now...");
                    this.addRoom(joinRoom.getRoomName());
                }
                room = this.getRoom(joinRoom.getRoomName());
                room.addUser(new User(joinRoom.getUsername(), joinRoom.getPortNumber()));
                System.out.println("Adding client to room: " + joinRoom.getRoomName());
                return new JoinRoomResp();

            case OP_CODE_LEAVE_ROOM:
                LeaveRoom rqst = (LeaveRoom) request;
                System.out.println("LOG: Recieved request from client: " + rqst.getUsername() + " to leave room: "
                        + rqst.getRoomName());
                room = this.rooms.get(rqst.getRoomName());
                if (room == null) {
                    System.out.println("ERR: Name in remove room request invalid... Sending error packet response.");
                    return new InvalidRoomName();
                }
                String usrExiting = rqst.getUsername();
                if (room.containsUser(usrExiting)) {
                    room.removeUser(usrExiting);
                    if (room.isEmpty())
                        this.rooms.remove(rqst.getRoomName());
                }
                System.out.println("LOG: Successfull removed client from the room. Sending response packet...");
                return new LeaveRoomResp();

            case OP_CODE_SEND_MESSAGE: // Send msg to a room from client
                SendMessage msg = (SendMessage) request;
                System.out.println("LOG: Send message request from user: " + msg.getUserName() + " to room: "
                        + msg.getRoomName() + ". Message: " + msg.getMessage());
                room = this.getRoom(msg.getRoomName());
                room.setMessageToForward(msg);
                if (room.getState() == Thread.State.NEW)
                    room.start();
                else
                    room.run();
                System.out.println("LOG: Sent message to all users in the room, sending response back to sender...");
                return new SendMessageResp();

            case OP_CODE_SEND_PRIVATE_MESSAGE:
                // Not yet implemented (E.C.)
                break;

            case OP_CODE_GOODBYE:
                GoodBye goodBye = (GoodBye) request;
                System.out.println("LOG: Revieced goodbye request from exiting client: " + goodBye.getUsername());
                String userExiting = goodBye.getUsername();
                System.out.println("Removing the user from the system.");
                if (this.users.contains(userExiting))
                    this.users.remove(userExiting);
                System.out.println("Removing the user from all subscribed rooms.");
                while (true) {
                    try {
                        for (String roomName : this.rooms.keySet()) {
                            room = this.rooms.get(roomName);
                            if (room.containsUser(userExiting)) {
                                room.removeUser(userExiting);
                                if (room.isEmpty())
                                    this.rooms.remove(roomName);
                            }
                        }
                        break;
                    } catch (ConcurrentModificationException e) {
                        continue;
                    }
                }
                System.out.println("LOG: Sending goodbye response back to client: " + goodBye.getUsername());
                return new GoodBye("server");

            default:
                return new IllegalOpcode();
        }
        return new UnknownError();
    }

    // Helper methods
    private boolean nameExists(HandShake handShake) {
        for (User user : this.users)
            if (user.getUsername().equals(handShake.getUser().getUsername()))
                return true;

        return false;
    }

    private boolean verifyProtocol(HandShake handShake) {
        return handShake.getProtocol() == protocol;
    }

    // Getters
    private ServerSocket getWelcomeSocket() {
        return this.welcomeSocket;
    }

    private ArrayList<String> getRooms() {
        return this.rooms.isEmpty() ? null : new ArrayList<String>(this.rooms.keySet());
    }

    /**
     * Method not yet used because sending private messages not yet implemented.
     */
    private ArrayList<String> getUsernames() {
        if (this.users.isEmpty())
            return null;

        ArrayList<String> usernames = new ArrayList<>();
        for (User user : this.users)
            usernames.add(user.getUsername());

        return usernames;
    }

    private Room getRoom(String roomName) {
        return this.rooms.get(roomName);
    }

    private boolean doesRoomExist(String name) {
        return this.rooms.keySet().contains(name);
    }

    private void addRoom(String name) {
        this.rooms.put(name, new Room(name));
    }
}