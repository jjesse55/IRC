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

public class Server extends Thread {

    private static final int PORT = 7777;
    private final ServerSocket WELCOME_SOCKET;
    private final static int PROTOCOL = 0x12345678;

    private final ServerDisconnect SERVER_DISCONNECT;

    private final ArrayList<User> USERS = new ArrayList<>();
    private final HashMap<String, Room> ROOMS = new HashMap<>();

    public static void main(String[] notUsed) throws Exception {
        Server server = new Server();
        server.start();
        server.SERVER_DISCONNECT.start();
        while (true) {
            ArrayList<User> usersToRemove = server.SERVER_DISCONNECT.sendKeepAliveMessages();
            for (User user : usersToRemove) {
                System.out.println("LOG: Timeout recieved from user: " + user.getUsername()
                        + ". Removing the user from the system now...");
                for (Room room : server.ROOMS.values()) {
                    if (room.containsUser(user.getUsername())) {
                        room.removeUser(user.getUsername());
                        if (room.isEmpty())
                            server.ROOMS.remove(room.getRoomName());
                    }
                }
                server.USERS.remove(user);
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

    private Server() throws IOException {
        this.WELCOME_SOCKET = new ServerSocket(PORT);
        this.SERVER_DISCONNECT = new ServerDisconnect(this.USERS);
    }

    private IrcPacket handleRequestFromClient(IrcPacket request) {
        Room room;
        switch (request.getPacketHeader().getOpCode()) {
            case OP_CODE_HELLO:
                HandShake handShake = (HandShake) request;
                System.out.println("LOG: Recieved handshake request from client: " + handShake.getUser().getUsername());
                if (!this.verifyProtocol(handShake))
                    return new IllegalProtocol();
                if (this.nameExists(handShake))
                    return new NameExists();
                this.USERS.add(handShake.getUser());
                System.out.println("LOG: Sending handshake to response back to client.");
                return new HandShake(null);

            case OP_CODE_LIST_ROOMS:
                System.out.println("LOG: Recieved request to list all rooms from client.");
                System.out.println("LOG: Sneding list all rooms response back to client.");
                return new ListRoomsResponse(this.getRooms());

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
                return new JoinRoomResponse();

            case OP_CODE_LEAVE_ROOM:
                LeaveRoom request = (LeaveRoom) request;
                System.out.println("LOG: Recieved request from client: " + request.getUsername() + " to leave room: "
                        + request.getRoomName());
                room = this.ROOMS.get(request.getRoomName());
                if (room == null) {
                    System.out.println("ERR: Name in remove room request invalid... Sending error packet response.");
                    return new InvalidRoomName();
                }
                String usrExiting = request.getUsername();
                if (room.containsUser(usrExiting)) {
                    room.removeUser(usrExiting);
                    if (room.isEmpty())
                        this.ROOMS.remove(request.getRoomName());
                }
                System.out.println("LOG: Successfull removed client from the room. Sending response packet...");
                return new LeaveRoomResponse();

            case OP_CODE_SEND_MESSAGE:
                SendMessage message = (SendMessage) request;
                System.out.println("LOG: Send message request from user: " + message.getUserName() + " to room: "
                        + message.getRoomName() + ". Message: " + message.getMessage());
                room = this.getRoom(message.getRoomName());
                room.setMessageToForward(message);
                if (room.getState() == Thread.State.NEW)
                    room.start();
                else
                    room.run();
                System.out.println("LOG: Sent message to all users in the room, sending response back to sender...");
                return new SendMessageResponse();

            case OP_CODE_SEND_PRIVATE_MESSAGE:
                // TODO
                break;

            case OP_CODE_GOODBYE:
                GoodBye goodBye = (GoodBye) request;
                System.out.println("LOG: Revieced goodbye request from exiting client: " + goodBye.getUsername());
                String userExiting = goodBye.getUsername();
                System.out.println("Removing the user from the system.");
                if (this.USERS.contains(userExiting))
                    this.USERS.remove(userExiting);
                System.out.println("Removing the user from all subscribed rooms.");
                while (true) {
                    try {
                        for (String roomName : this.ROOMS.keySet()) {
                            room = this.ROOMS.get(roomName);
                            if (room.containsUser(userExiting)) {
                                room.removeUser(userExiting);
                                if (room.isEmpty())
                                    this.ROOMS.remove(roomName);
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

    private boolean nameExists(HandShake handShake) {
        for (User user : this.USERS)
            if (user.getUsername().equals(handShake.getUser().getUsername()))
                return true;

        return false;
    }

    private boolean verifyProtocol(HandShake handShake) {
        return handShake.getProtocol() == PROTOCOL;
    }

    private ServerSocket getWelcomeSocket() {
        return this.WELCOME_SOCKET;
    }

    private ArrayList<String> getRooms() {
        return this.ROOMS.isEmpty() ? null : new ArrayList<String>(this.ROOMS.keySet());
    }

    private Room getRoom(String roomName) {
        return this.ROOMS.get(roomName);
    }

    private boolean doesRoomExist(String name) {
        return this.ROOMS.keySet().contains(name);
    }

    private void addRoom(String name) {
        this.ROOMS.put(name, new Room(name));
    }
}