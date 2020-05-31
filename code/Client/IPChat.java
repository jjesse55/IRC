package code.Client;

import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.*;
import javax.swing.JFrame;
import code.IRC_Packets.IRC_Packet;
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
import code.Client.GuiBase;
import code.Codes.OpCodes;
import code.ErrorPackets.ErrorPacket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main client part of the application. 1. Initiates the client application 2.
 * Asks the user for the username they would like to use 3. Displays a menu of
 * options that the user can perform in the application
 */
class IPChat extends GuiBase implements ActionListener, Runnable {
    // Class fields
    private AliveS keepAliveSocket;
    private HashMap<String, CRoom> roomsJoined = new HashMap<>();

    // Fields for the GUI
    JFrame menu;
    String UserName;

    // class methods
    JFrame NameGetter;

    public IPChat() {
        super(null);
    }


    //Main method
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                IPChat myChat = new IPChat();
                myChat.keepAliveSocket = new AliveS();
                myChat.keepAliveSocket.start();
                myChat.run();
            }
        });
    }


    /**
     * Run method for the main client thread of IPChat
     */
    public void run() {
        this.handshakeAndUsername();
        this.menuOptionMethods();
    }


    public void menuOptionMethods() {

        Color bgColor = new Color(47, 79, 79);
        menu = new JFrame("Menu");
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menu.getContentPane().setBackground(bgColor);
        JPanel menuBar = new JPanel();
        menuBar.setPreferredSize(new Dimension(200, 500));

        JButton listRooms = new JButton("List All Rooms");
        listRooms.setPreferredSize(new Dimension(200, 90));
        menuBar.add(listRooms);

        listRooms.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                ListRooms listRooms = new ListRooms();

                System.out.println("LOG: Sending request to server to list all rooms.");
                IRC_Packet resp = sendPacketToWelcomeServer(listRooms);

                if (isErrPacket(resp))
                    handleErrorResponseFromServer((ErrorPacket) resp);
                else {
                    ListRoomsResp roomResp = (ListRoomsResp) resp;
                    displayRooms(roomResp.getRooms());
                    System.out.println("LOG: Successfully retrieved list all rooms. Displaying now...");
                }
            }
        });

        JButton addRoom = new JButton("Add/Join a Room");
        addRoom.setPreferredSize(new Dimension(200, 90));
        menuBar.add(addRoom);

        addRoom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NameGetter = new JFrame("Add/Join A Room");
                String roomAdd = JOptionPane.showInputDialog(NameGetter, "Enter The Name you want to join");

                if(roomAdd == null){
                    System.out.println("Cancel was is pressed");
                 }
                 else{
                int p = openNewRoomWindow(roomAdd);
                String us = getUsername();

                JoinRoom roomJoin = new JoinRoom(roomAdd, us, p);

                System.out.println("LOG: Requesting to join the room: " + roomAdd);
                IRC_Packet resp = sendPacketToWelcomeServer(roomJoin);

                if (isErrPacket(resp)) 
                    handleErrorResponseFromServer((ErrorPacket) resp);
                else
                    System.out.println("LOG: Successfully joined the room: " + roomAdd);

                 }
                }


        
        });

        JButton removeRoom = new JButton("Leave a Room");
        removeRoom.setPreferredSize(new Dimension(200, 90));
        menuBar.add(removeRoom);

        removeRoom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NameGetter = new JFrame("Leave A Room");
                String roomRemov = JOptionPane.showInputDialog(NameGetter, "Enter The Name you want to leave");

                LeaveRoom roomRev = new LeaveRoom(roomRemov, getUsername());

                System.out.println("LOG: Requesting to leave the room: " + roomRemov);
                IRC_Packet resp = sendPacketToWelcomeServer(roomRev);

                if (isErrPacket(resp))
                    handleErrorResponseFromServer((ErrorPacket) resp);
                else {
                    CRoom toClose = roomsJoined.get(roomRemov);
                    if (toClose == null)
                        System.err.println("ERR: Cannot exit the room: " + roomRemov + ". Name does not exist");
                    toClose.closeRoomWindow();
                    roomsJoined.remove(roomRemov);

                    System.out.println("LOG: Successfully left the room: " + roomRemov);
                }
            }
        });

        JButton displayUsers = new JButton("Display All Users in a Room");
        displayUsers.setPreferredSize(new Dimension(200, 90));
        menuBar.add(displayUsers);
        displayUsers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String roomtoList = JOptionPane.showInputDialog(NameGetter, "Enter The Name of the Room");
                if(roomtoList == null){
                    System.out.println("Cancel was is pressed");
                 }
                 else{
                ListUsers listUsers = new ListUsers(roomtoList);

                System.out.println("LOG: Requesting to list all the users in room: " + roomtoList);
                IRC_Packet resp = sendPacketToWelcomeServer(listUsers);
                 

                if (isErrPacket(resp))
                    handleErrorResponseFromServer((ErrorPacket) resp);
                else {
                    ListUsersResponse listUsersResponse = (ListUsersResponse) resp;
                    System.out.println("LOG: Successfully retrieved all users in room: " + roomtoList
                    + ". Displaying them now...");
                    displayUser(listUsersResponse.getUsers());
                }
              }
            }
        });

        JButton serverDisconnect = new JButton("Exit IPChat");
        serverDisconnect.setPreferredSize(new Dimension(200, 90));
        menuBar.add(serverDisconnect);

        serverDisconnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                System.out.println("LOG: Informing server of client exiting IPChat application");
                IRC_Packet resp = sendPacketToWelcomeServer(new GoodBye(getUsername()));

                if (isErrPacket(resp)) {
                    handleErrorResponseFromServer((ErrorPacket) resp);
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
            }
        });

        // menu.setLayout(null);
        menu.getContentPane().add(menuBar);
        menu.pack();
        menu.setVisible(true);
    }

    public String userName() {
        NameGetter = new JFrame("UserName Response");
        UserName = JOptionPane.showInputDialog(NameGetter, "Enter Your Name");

        if(UserName == null)
            System.exit(0);

        return UserName;
    }

    public void serverCrashes() {
        JFrame servCrash = new JFrame("Server Has Stopped Responding");
        servCrash.setVisible(true);
        JOptionPane.showMessageDialog(servCrash, "The Server has stopped responding, IP Chat Exiting");
        servCrash.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.exit(1);
    }


    /**
     * Must override this method
     */
    public void actionPerformed(ActionEvent e) {}

    public void displayRooms(ArrayList<String> rooms) {
        JFrame Rooms = new JFrame("List All Rooms");
        Rooms.setVisible(true);
        if (rooms == null)
             JOptionPane.showMessageDialog(Rooms, "Empty");
        else
            JOptionPane.showMessageDialog(Rooms, rooms.toString());
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
            IRC_Packet irc_Packet = (IRC_Packet) inFromServer.readObject();

            super.closeClientSocket();
            this.handleResponseFromServer(irc_Packet);

            if (irc_Packet.getPacketHeader().getOpCode() == OpCodes.OP_CODE_ERR)
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

    /**
     * This method: 1. creates a new CRoom (client room) object 2. Opens up the
     * listening socket for the room 3. Runs the new room in parallel
     */
    public int openNewRoomWindow(String roomName) {
        try {
            ServerSocket roomSocket = new ServerSocket(0);
            CRoom newRoom = new CRoom(roomName, roomSocket, username);
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

    /**
     * Add a new room to the Array kept by the clinet of rooms joined
     */
    private void addNewRoom(CRoom room) {
        this.roomsJoined.put(room.getRoomName(), room);
    }
}