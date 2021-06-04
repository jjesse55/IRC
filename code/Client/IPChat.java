package code.Client;

import javax.swing.*;
import java.awt.Color;
import java.awt.*;
import javax.swing.JFrame;
import code.IRC_Packets.IrcPacket;
import code.OpPackets.LeaveRoom;
import code.OpPackets.ListRooms;
import code.OpPackets.GoodBye;
import code.OpPackets.HandShake;
import code.OpPackets.JoinRoom;
import code.OpPackets.ListRoomsResponse;
import code.OpPackets.ListUsers;
import code.OpPackets.ListUsersResponse;
import code.Server.User;
import java.net.ServerSocket;
import java.io.*;

import code.Codes.OpCodes;
import code.ErrorPackets.ErrorPacket;
import java.util.ArrayList;
import java.util.HashMap;

class IPChat extends GuiBase implements Runnable {

    private String username;
    private JFrame nameGetter;
    private ServerAlive keepAliveSocket;

    private final JFrame MENU = new JFrame("Menu");
    private final HashMap<String, ChatRoom> ROOMS_JOINED = new HashMap<>();


    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            IPChat myChat = new IPChat();
            myChat.keepAliveSocket = new ServerAlive();
            myChat.keepAliveSocket.start();
            myChat.run();
        });
    }

    private IPChat() {
        super(null);
    }

    public void run() {
        String username = this.handshakeWithServer();
        super.setUsername(username);
        this.buildMainMenu();
    }

    private String handshakeWithServer() {
        String usernameToTry = null;
        try {
            super.openClientSocket();
            ObjectOutputStream outToServer = new ObjectOutputStream(this.clientSocket.getOutputStream());
            usernameToTry = this.promptForUsername();
            outToServer.writeObject(new HandShake(new User(usernameToTry, this.keepAliveSocket.getPortNumber())));
            ObjectInputStream inFromServer = new ObjectInputStream(this.clientSocket.getInputStream());
            IrcPacket ircPacket = (IrcPacket) inFromServer.readObject();
            super.closeClientSocket();
            super.handleResponseFromServer(ircPacket);
            if (ircPacket.getPacketHeader().getOpCode() == OpCodes.OP_CODE_ERROR)
                this.handshakeWithServer();
        } catch (Exception exception) {
            System.out.println("ERR: Error receiving welcome packet from the server");
            super.serverCrashes();
        }
        return usernameToTry;
    }

    private String promptForUsername() {
        this.nameGetter = new JFrame("userName Response");
        this.username = JOptionPane.showInputDialog(this.nameGetter, "Enter Your Name");
        if(this.username == null)
            System.exit(0);
        return this.username;
    }

    public void buildMainMenu() {
        Color backgroundColor = new Color(47, 79, 79);
        this.MENU.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.MENU.getContentPane().setBackground(backgroundColor);

        JPanel menuBar = new JPanel();
        menuBar.setPreferredSize(new Dimension(200, 500));
        JButton listRoomsMenuButton = this.buildListRoomsMenuButton();
        menuBar.add(listRoomsMenuButton);
        JButton joinRoomMenuButton = this.buildJoinRoomMenuButton();
        menuBar.add(joinRoomMenuButton);
        JButton leaveRoomMenuButton = this.buildLeaveRoomMenuButton();
        menuBar.add(leaveRoomMenuButton);
        JButton displayUsersMenuButton = this.buildDisplayUsersMenuButton();
        menuBar.add(displayUsersMenuButton);
        JButton exitChatMenuButton = this.buildExitChatMenuButton();
        menuBar.add(exitChatMenuButton);

        this.MENU.getContentPane().add(menuBar);
        this.MENU.pack();
        this.MENU.setVisible(true);
    }

    private JButton buildListRoomsMenuButton() {
        JButton listRoomsMenuButton = new JButton("List All Rooms");
        listRoomsMenuButton.setPreferredSize(new Dimension(200, 90));
        listRoomsMenuButton.addActionListener(actionEvent -> {
            ListRooms listRooms1 = new ListRooms();
            System.out.println("LOG: Sending request to server to list all rooms.");
            IrcPacket response = super.sendPacketToServer(listRooms1);
            if (super.isErrorPacket(response))
                super.handleErrorResponseFromServer((ErrorPacket) response);
            else {
                ListRoomsResponse roomResponse = (ListRoomsResponse) response;
                this.displayRooms(roomResponse.getRooms());
                System.out.println("LOG: Successfully retrieved list all rooms. Displaying now...");
            }
        });
        return listRoomsMenuButton;
    }

    private void displayRooms(ArrayList<String> rooms) {
        JFrame listAllRoomsFrame = new JFrame("List All Rooms");
        listAllRoomsFrame.setVisible(true);
        if (rooms == null)
            JOptionPane.showMessageDialog(listAllRoomsFrame, "Empty");
        else
            JOptionPane.showMessageDialog(listAllRoomsFrame, rooms.toString());
    }

    private JButton buildJoinRoomMenuButton() {
        JButton joinRoomMenuButton = new JButton("Add/Join a Room");
        joinRoomMenuButton.setPreferredSize(new Dimension(200, 90));
        joinRoomMenuButton.addActionListener(actionEvent -> {
            this.nameGetter = new JFrame("Add/Join A Room");
            String roomAdd = JOptionPane.showInputDialog(this.nameGetter, "Enter The Name you want to join");
            if (roomAdd == null) {
                System.out.println("Cancel was is pressed");
            } else {
                int port = this.openNewRoomWindow(roomAdd);
                String us = super.getUsername();
                JoinRoom joinRoom = new JoinRoom(roomAdd, us, port);
                System.out.println("LOG: Requesting to join the room: " + roomAdd);
                IrcPacket response = super.sendPacketToServer(joinRoom);
                if (super.isErrorPacket(response))
                    super.handleErrorResponseFromServer((ErrorPacket) response);
                else
                    System.out.println("LOG: Successfully joined the room: " + roomAdd);
            }
        });
        return joinRoomMenuButton;
    }

    private int openNewRoomWindow(String roomName) {
        try {
            ServerSocket roomSocket = new ServerSocket(0);
            ChatRoom newRoom = new ChatRoom(roomName, roomSocket, this.username);
            this.addNewRoom(newRoom);
            Thread roomThread = new Thread(newRoom);
            System.out.println("LOG: Attempting to open a new window for room: " + roomName);
            roomThread.start();
            System.out.println("LOG: Successfully opened a new window for room: " + roomName);
            return newRoom.getListeningSocket().getLocalPort();
        } catch (IOException exception) {
            exception.printStackTrace();
            System.err.println("ERR: Unable to open the new room: : " + roomName + "... Please try again.");
        }
        return 0;
    }

    private void addNewRoom(ChatRoom room) {
        this.ROOMS_JOINED.put(room.getRoomName(), room);
    }

    private JButton buildLeaveRoomMenuButton() {
        JButton leaveRoomMenuButton = new JButton("Leave a Room");
        leaveRoomMenuButton.setPreferredSize(new Dimension(200, 90));
        leaveRoomMenuButton.addActionListener(actionEvent -> {
            this.nameGetter = new JFrame("Leave A Room");
            String roomRemove = JOptionPane.showInputDialog(this.nameGetter, "Enter The Name you want to leave");
            LeaveRoom leaveRoom = new LeaveRoom(roomRemove, getUsername());
            System.out.println("LOG: Requesting to leave the room: " + roomRemove);
            IrcPacket response = super.sendPacketToServer(leaveRoom);
            if (super.isErrorPacket(response))
                super.handleErrorResponseFromServer((ErrorPacket) response);
            else {
                ChatRoom toClose = this.ROOMS_JOINED.get(roomRemove);
                if (toClose == null) {
                    System.err.println("ERR: Cannot exit the room: " + roomRemove + ". Name does not exist");
                    return;
                }
                toClose.closeRoomWindow();
                this.ROOMS_JOINED.remove(roomRemove);
                System.out.println("LOG: Successfully left the room: " + roomRemove);
            }
        });
        return leaveRoomMenuButton;
    }

    private JButton buildDisplayUsersMenuButton() {
        JButton displayUsersMenuButton = new JButton("Display All Users in a Room");
        displayUsersMenuButton.setPreferredSize(new Dimension(200, 90));
        displayUsersMenuButton.addActionListener(actionEvent -> {
            String roomToList = JOptionPane.showInputDialog(this.nameGetter, "Enter The Name of the Room");
            if (roomToList == null) {
                System.out.println("Cancel was is pressed");
            } else {
                ListUsers listUsers = new ListUsers(roomToList);
                System.out.println("LOG: Requesting to list all the users in room: " + roomToList);
                IrcPacket response = super.sendPacketToServer(listUsers);
                if (super.isErrorPacket(response))
                    super.handleErrorResponseFromServer((ErrorPacket) response);
                else {
                    ListUsersResponse listUsersResponse = (ListUsersResponse) response;
                    System.out.println("LOG: Successfully retrieved all users in room: " + roomToList
                            + ". Displaying them now...");
                    this.displayUsers(listUsersResponse.getUsers());
                }
            }
        });
        return displayUsersMenuButton;
    }

    private void displayUsers(ArrayList<String> users) {
        JFrame user = new JFrame("Showing All Users");
        user.setVisible(true);
        if (users == null)
            JOptionPane.showMessageDialog(user, "Empty");
        else
            JOptionPane.showMessageDialog(user, users.toString());
    }

    private JButton buildExitChatMenuButton() {
        JButton exitChatMenuButton = new JButton("Exit IPChat");
        exitChatMenuButton.setPreferredSize(new Dimension(200, 90));
        exitChatMenuButton.addActionListener(actionEvent -> {
            System.out.println("LOG: Informing server of client exiting IPChat application");
            IrcPacket response = super.sendPacketToServer(new GoodBye(getUsername()));
            if (super.isErrorPacket(response)) {
                super.handleErrorResponseFromServer((ErrorPacket) response);
            } else {
                JFrame disconnect = new JFrame("Disconnecting from Server");
                disconnect.setVisible(true);
                int input = JOptionPane.showConfirmDialog(disconnect, "To exit IPChat, click Yes");
                disconnect.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                try {
                    if (input == 0)
                        System.exit(0);
                    else
                        return;
                } catch (Exception exception) {
                    System.exit(5);
                }
            }
            System.out.println("LOG: Exiting IPChat");
        });
        return exitChatMenuButton;
    }
}