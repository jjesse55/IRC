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
import code.ErrorPackets.InvalidRoomName;
import code.IRC_Packets.IrcPacket;
import code.OpPackets.*;

public class Server extends Thread {

    private final ServerSocket WELCOME_SOCKET;
    private final ServerDisconnect SERVER_DISCONNECT;
    private final ArrayList<User> USERS = new ArrayList<>();
    private final HashMap<String, Room> ROOMS = new HashMap<>();

    private static final int PORT = 7777;
    private final static int PROTOCOL = 0x12345678;


    public static void main(String [] notUsed) throws Exception {
        Server server = new Server();
        server.start();
        server.SERVER_DISCONNECT.start();
        server.pingClientsForTimeout();
    }

    private Server() throws IOException {
        this.WELCOME_SOCKET = new ServerSocket(PORT);
        this.SERVER_DISCONNECT = new ServerDisconnect(this.USERS);
    }

    public void run() {
        while (true) {
            try {
                System.out.println("ServerSocket awaiting connections...");
                Socket newConnection = this.WELCOME_SOCKET.accept();
                ObjectInputStream inFromClient = new ObjectInputStream(newConnection.getInputStream());
                IrcPacket clientPacket = (IrcPacket) inFromClient.readObject();
                ObjectOutputStream outToClient = new ObjectOutputStream(newConnection.getOutputStream());
                outToClient.writeObject(this.handleRequestFromClient(clientPacket));
                newConnection.close();
            } catch (Exception exception) {
                exception.printStackTrace();
                System.err.println("ERR: Unable to exchange info with client");
            }
        }
    }

    private void pingClientsForTimeout() {
        while (true) {
            ArrayList<User> usersToRemove = this.SERVER_DISCONNECT.sendKeepAliveMessages();
            for (User user : usersToRemove) {
                System.out.println("LOG: Timeout received from user: " + user.getUsername()
                        + ". Removing the user from the system now...");
                for (Room room : this.ROOMS.values()) {
                    if (room.containsUser(user.getUsername())) {
                        room.removeUser(user.getUsername());
                        if (room.isEmpty())
                            this.ROOMS.remove(room.getRoomName());
                    }
                }
                this.USERS.remove(user);
            }
            this.sleepFiveSecondsBeforeNextPing();
        }
    }

    private void sleepFiveSecondsBeforeNextPing() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {}
    }

    private IrcPacket handleRequestFromClient(IrcPacket request) {
        switch (request.getPacketHeader().getOpCode()) {
            case OP_CODE_HELLO:
                assert request instanceof HandShake;
                HandShake handShakeRequest = (HandShake) request;
                return this.handleHandshakeRequest(handShakeRequest);
            case OP_CODE_LIST_ROOMS:
                return this.handleListRoomsRequest();
            case OP_CODE_LIST_USERS:
                assert request instanceof ListUsers;
                ListUsers listUsersRequest = (ListUsers) request;
                return this.handleListUsersRequest(listUsersRequest);
            case OP_CODE_JOIN_ROOM:
                assert request instanceof JoinRoom;
                JoinRoom joinRoomRequest = (JoinRoom) request;
                return this.handleJoinRoomRequest(joinRoomRequest);
            case OP_CODE_LEAVE_ROOM:
                assert request instanceof LeaveRoom;
                LeaveRoom leaveRoomRequest = (LeaveRoom) request;
                return this.handleLeaveRoomRequest(leaveRoomRequest);
            case OP_CODE_SEND_MESSAGE:
                assert request instanceof SendMessage;
                SendMessage sendMessageRequest = (SendMessage) request;
                return this.handleSendMessageRequest(sendMessageRequest);
            case OP_CODE_GOODBYE:
                assert request instanceof GoodBye;
                GoodBye goodByeRequest = (GoodBye) request;
                return this.handleGoodbyeRequest(goodByeRequest);
            default:
                return new IllegalOpcode();
        }
    }

    private IrcPacket handleHandshakeRequest(HandShake handShakeRequest) {
        System.out.println("LOG: Received handshake request from client: " + handShakeRequest.getUser().getUsername());
        boolean invalidProtocolUsed = !this.verifyProtocol(handShakeRequest);
        if (invalidProtocolUsed)
            return new IllegalProtocol();
        boolean usernameAlreadyExists = this.nameExists(handShakeRequest);
        if (usernameAlreadyExists)
            return new NameExists();
        this.USERS.add(handShakeRequest.getUser());
        System.out.println("LOG: Sending handshake to response back to client.");
        return new HandShake(null);
    }

    private boolean verifyProtocol(HandShake handShake) {
        return handShake.getProtocol() == PROTOCOL;
    }

    private boolean nameExists(HandShake handShake) {
        for (User user : this.USERS)
            if (user.getUsername().equals(handShake.getUser().getUsername()))
                return true;
        return false;
    }

    private IrcPacket handleListRoomsRequest() {
        System.out.println("LOG: Received request to list all rooms from client.\n " +
                "LOG: Sending list all rooms response back to client.");
        return new ListRoomsResponse(this.getRooms());
    }

    private ArrayList<String> getRooms() {
        return this.ROOMS.isEmpty() ? null : new ArrayList<String>(this.ROOMS.keySet());
    }

    private IrcPacket handleListUsersRequest(ListUsers listUsersRequest) {
        System.out.println(
                "LOG: Received list users request from client: for room " + listUsersRequest.getRoomName());
        Room roomToListUsers = this.getRoom(listUsersRequest.getRoomName());
        System.out.println(
                "LOG: Sending list of users in room: " + listUsersRequest.getRoomName() + " back to client.");
        boolean roomExists = roomToListUsers != null;
        ArrayList<String> usersInRoom = roomExists ? roomToListUsers.getUsers() : null;
        return new ListUsersResponse(usersInRoom);
    }

    private Room getRoom(String roomName) {
        return this.ROOMS.get(roomName);
    }

    private IrcPacket handleJoinRoomRequest(JoinRoom joinRoomRequest) {
        System.out.println("LOG: Received request from client: " + joinRoomRequest.getUsername() + " to join room: "
                + joinRoomRequest.getRoomName());
        boolean roomDoesNotExist = !this.doesRoomExist(joinRoomRequest.getRoomName());
        if (roomDoesNotExist) {
            System.out.println("LOG: Room does not exist. Creating room now...");
            this.addRoom(joinRoomRequest.getRoomName());
        }
        Room roomToJoin = this.getRoom(joinRoomRequest.getRoomName());
        roomToJoin.addUser(new User(joinRoomRequest.getUsername(), joinRoomRequest.getPortNumber()));
        System.out.println("Adding client to room: " + joinRoomRequest.getRoomName());
        return new JoinRoomResponse();
    }

    private boolean doesRoomExist(String name) {
        return this.ROOMS.containsKey(name);
    }

    private void addRoom(String name) {
        this.ROOMS.put(name, new Room(name));
    }

    private IrcPacket handleLeaveRoomRequest(LeaveRoom leaveRoomRequest) {
        System.out.println("LOG: Received request from client: " + leaveRoomRequest.getUsername() + " to leave room: "
                + leaveRoomRequest.getRoomName());
        Room roomToLeave = this.ROOMS.get(leaveRoomRequest.getRoomName());
        if (roomToLeave == null) {
            System.out.println("ERR: Name in remove room request invalid... Sending error packet response.");
            return new InvalidRoomName();
        }
        String usrExiting = leaveRoomRequest.getUsername();
        if (roomToLeave.containsUser(usrExiting)) {
            roomToLeave.removeUser(usrExiting);
            if (roomToLeave.isEmpty())
                this.ROOMS.remove(leaveRoomRequest.getRoomName());
        }
        System.out.println("LOG: Successful removed client from the room. Sending response packet...");
        return new LeaveRoomResponse();
    }

    private IrcPacket handleSendMessageRequest(SendMessage sendMessageRequest) {
        System.out.println("LOG: Send message request from user: " + sendMessageRequest.getUserName() + " to room: "
                + sendMessageRequest.getRoomName() + ". Message: " + sendMessageRequest.getMessage());
        Room roomToSendMessage = this.getRoom(sendMessageRequest.getRoomName());
        roomToSendMessage.setMessageToForward(sendMessageRequest);
        if (roomToSendMessage.getState() == Thread.State.NEW)
            roomToSendMessage.start();
        else
            roomToSendMessage.run();
        System.out.println("LOG: Sent message to all users in the room, sending response back to sender...");
        return new SendMessageResponse();
    }

    private IrcPacket handleGoodbyeRequest(GoodBye goodByeRequest) {
        System.out.println("LOG: Received goodbye request from exiting client: " + goodByeRequest.getUsername());
        String userExiting = goodByeRequest.getUsername();
        System.out.println("Removing the user from all subscribed rooms.");
        try {
            for (String roomName : this.ROOMS.keySet()) {
                Room roomToRemoveUserFrom = this.ROOMS.get(roomName);
                if (roomToRemoveUserFrom.containsUser(userExiting)) {
                    roomToRemoveUserFrom.removeUser(userExiting);
                    if (roomToRemoveUserFrom.isEmpty())
                        this.ROOMS.remove(roomName);
                }
            }
        } catch (ConcurrentModificationException ignored) {
            this.handleGoodbyeRequest(goodByeRequest);
        }
        System.out.println("LOG: Sending goodbye response back to client: " + goodByeRequest.getUsername());
        return new GoodBye("server");
    }
}