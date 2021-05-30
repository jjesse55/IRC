package code.Client;

import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.*;
import javax.swing.JFrame;
import code.IRC_Packets.IrcPacket;
import code.OpPackets.LeaveRoom;
import code.OpPackets.ListRooms;
import code.OpPackets.GoodBye;
import code.OpPackets.HandShake;
import code.OpPackets.JoinRoom;
import code.OpPackets.ListRoomsResp;
import code.OpPackets.ListUsers;
import code.OpPackets.ListUsersResponse;
import code.Server.User;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.io.*;

import code.Codes.OpCodes;
import code.ErrorPackets.ErrorPacket;
import java.util.ArrayList;
import java.util.HashMap;

class IPChat extends GuiBase implements ActionListener, Runnable {
    private KeepAlive keepAliveSocket;
    private final HashMap<String, ChatRoom> ROOMS_JOINED = new HashMap<>();

    private final JFrame MENU = new JFrame("Menu");
    private String username;

    private JFrame nameGetter;

    public IPChat() {
        super(null);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            IPChat myChat = new IPChat();
            myChat.keepAliveSocket = new KeepAlive();
            myChat.keepAliveSocket.start();
            myChat.run();
        });
    }

    public void run() {
        this.handshakeAndUsername();
        this.MENUOptionMethods();
    }


    public void MENUOptionMethods() {

        Color backgroundColor = new Color(47, 79, 79);
        this.MENU.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.MENU.getContentPane().setBackground(backgroundColor);
        JPanel menuBar = new JPanel();
        menuBar.setPreferredSize(new Dimension(200, 500));

        JButton listRooms = new JButton("List All Rooms");
        listRooms.setPreferredSize(new Dimension(200, 90));
        menuBar.add(listRooms);

        listRooms.addActionListener(actionEvent -> {

            ListRooms listRooms1 = new ListRooms();

                System.out.println("LOG: Sending request to server to list all rooms.");
                IrcPacket response = sendPacketToWelcomeServer(listRooms1);

            if (isErrPacket(response))
                handleErrorResponseFromServer((ErrorPacket) response);
            else {
                ListRoomsResp roomresponse = (ListRoomsResp) response;
                displayRooms(roomresponse.getRooms());
                System.out.println("LOG: Successfully retrieved list all rooms. Displaying now...");
            }
        });

        JButton addRoom = new JButton("Add/Join a Room");
        addRoom.setPreferredSize(new Dimension(200, 90));
        menuBar.add(addRoom);

        addRoom.addActionListener(actionEvent -> {
                this.nameGetter = new JFrame("Add/Join A Room");
                String roomAdd = JOptionPane.showInputDialog(this.nameGetter, "Enter The Name you want to join");

            if(roomAdd == null){
                System.out.println("Cancel was is pressed");
             }
             else{
            int p = openNewRoomWindow(roomAdd);
            String us = getUsername();

            JoinRoom roomJoin = new JoinRoom(roomAdd, us, p);

            System.out.println("LOG: Requesting to join the room: " + roomAdd);
            IrcPacket response = sendPacketToWelcomeServer(roomJoin);

            if (isErrPacket(response))
                handleErrorResponseFromServer((ErrorPacket) response);
            else
                System.out.println("LOG: Successfully joined the room: " + roomAdd);

             }
            });

        JButton removeRoom = new JButton("Leave a Room");
        removeRoom.setPreferredSize(new Dimension(200, 90));
        menuBar.add(removeRoom);

        removeRoom.addActionListener(actionEvent -> {
            this.nameGetter = new JFrame("Leave A Room");
            String roomRemove = JOptionPane.showInputDialog(this.nameGetter, "Enter The Name you want to leave");

            LeaveRoom leaveRoom = new LeaveRoom(roomRemove, getUsername());

            System.out.println("LOG: Requesting to leave the room: " + roomRemove);
            IrcPacket response = sendPacketToWelcomeServer(leaveRoom);

            if (isErrPacket(response))
                handleErrorResponseFromServer((ErrorPacket) response);
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

        JButton displayUsers = new JButton("Display All Users in a Room");
        displayUsers.setPreferredSize(new Dimension(200, 90));
        menuBar.add(displayUsers);
        displayUsers.addActionListener(actionEvent -> {
            String roomToList = JOptionPane.showInputDialog(this.nameGetter, "Enter The Name of the Room");
            if(roomToList == null){
                System.out.println("Cancel was is pressed");
             }
             else{
            ListUsers listUsers = new ListUsers(roomToList);

            System.out.println("LOG: Requesting to list all the users in room: " + roomToList);
            IrcPacket response = sendPacketToWelcomeServer(listUsers);


            if (isErrPacket(response))
                handleErrorResponseFromServer((ErrorPacket) response);
            else {
                ListUsersResponse listUsersResponse = (ListUsersResponse) response;
                System.out.println("LOG: Successfully retrieved all users in room: " + roomToList
                + ". Displaying them now...");
                displayUser(listUsersResponse.getUsers());
            }
          }
        });

        JButton serverDisconnect = new JButton("Exit IPChat");
        serverDisconnect.setPreferredSize(new Dimension(200, 90));
        menuBar.add(serverDisconnect);

        serverDisconnect.addActionListener(actionEvent -> {

            System.out.println("LOG: Informing server of client exiting IPChat application");
            IrcPacket response = sendPacketToWelcomeServer(new GoodBye(getUsername()));

            if (isErrPacket(response)) {
                handleErrorResponseFromServer((ErrorPacket) response);
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
                } catch (Exception a) {
                    a.printStackTrace();
                    System.exit(5);
                }
            }

            System.out.println("LOG: Exiting IPChat");
        });

        this.MENU.getContentPane().add(menuBar);
        this.MENU.pack();
        this.MENU.setVisible(true);
    }

    public String userName() {
        this.nameGetter = new JFrame("userName Response");
        this.username = JOptionPane.showInputDialog(this.nameGetter, "Enter Your Name");

        if(this.username == null)
            System.exit(0);

        return this.username;
    }

    public void serverCrashes() {
        JFrame servCrash = new JFrame("Server Has Stopped Responding");
        servCrash.setVisible(true);
        JOptionPane.showMessageDialog(servCrash, "The Server has stopped responding, IP Chat Exiting");
        servCrash.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.exit(1);
    }

    public void actionPerformed(ActionEvent e) {}

    public void displayRooms(ArrayList<String> rooms) {
        JFrame listAllRoomsFrame = new JFrame("List All Rooms");
        listAllRoomsFrame.setVisible(true);
        if (rooms == null)
             JOptionPane.showMessageDialog(listAllRoomsFrame, "Empty");
        else
            JOptionPane.showMessageDialog(listAllRoomsFrame, rooms.toString());
    }

    public void displayUser(ArrayList<String> users) {
        JFrame user = new JFrame("Showing All Users");
        user.setVisible(true);
        if (users == null)
            JOptionPane.showMessageDialog(user, "Empty");
        else
            JOptionPane.showMessageDialog(user, users.toString());
    }


    private boolean handshakeAndUsername() {
        String usernameToTry = null;

        try {
            super.openClientSocket();

            ObjectOutputStream outToServer = new ObjectOutputStream(this.clientSocket.getOutputStream());

            usernameToTry = this.userName();

            System.out.println("LOG: Attempting to register as username: " + usernameToTry);
            outToServer.writeObject(new HandShake(new User(usernameToTry, this.keepAliveSocket.getPortNumber())));

            ObjectInputStream inFromServer = new ObjectInputStream(this.clientSocket.getInputStream());
            IrcPacket ircPacket = (IrcPacket) inFromServer.readObject();

            super.closeClientSocket();
            this.handleResponseFromServer(ircPacket);

            if (ircPacket.getPacketHeader().getOpCode() == OpCodes.OP_CODE_ERROR)
                return this.handshakeAndUsername();

            System.out.println("LOG: Successfully registered with username: " + usernameToTry);

        } catch (SocketTimeoutException exception) {
            System.out.println("ERR: The server has no longer become responsive. Please try connecting again");
            this.serverCrashes();
        } catch (IOException exception) {
            System.out.println("ERR: Error recieving welcome packet from the server");
            this.serverCrashes();
        } catch (ClassNotFoundException exception) {
            System.out.println("ERR: The operation specified is unsuported and not available... System exiting");
            System.exit(1);
        }

        super.setUsername(usernameToTry);
        return true;
    }

    public int openNewRoomWindow(String roomName) {
        try {
            ServerSocket roomSocket = new ServerSocket(0);
            ChatRoom newRoom = new ChatRoom(roomName, roomSocket, this.username);
            this.addNewRoom(newRoom);
            Thread roomThread = new Thread(newRoom);

            System.out.println("LOG: Attempting to open a new window for room: " + roomName);
            roomThread.start();

            System.out.println("LOG: Successfully opened a new window for room: " + roomName);
            return newRoom.getListeningSocket().getLocalPort();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERR: Unable to open the new room: : " + roomName + "... Please try again.");
        }

        return 0;
    }

    private void addNewRoom(ChatRoom room) {
        this.ROOMS_JOINED.put(room.getRoomName(), room);
    }
}