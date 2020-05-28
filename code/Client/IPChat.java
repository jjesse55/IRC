package code.Client;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.*;
import javax.swing.JFrame;
import java.lang.Object;
import code.IRC_Packets.IRC_Packet;
import code.OpPackets.LeaveRoom;
import code.OpPackets.LeaveRoomResp;
import code.OpPackets.ListRooms;
import code.OpPackets.GoodBye;
import code.OpPackets.HandShake;
import code.OpPackets.JoinRoom;
import code.OpPackets.JoinRoomResp;
import code.OpPackets.ListRoomsResp;
import code.OpPackets.ListUsers;
import code.OpPackets.ListUsersResponse;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.io.*;
import code.Client.GuiBase;
import code.Codes.OpCodes;
import code.ErrorPackets.ErrorPacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;




//the warning is ok to keep since we will be serializing but not yet!
class IPChat extends GuiBase implements ActionListener, Runnable
{

    private HashMap<String, CRoom> roomsJoined = new HashMap<>();


/*
    JFrame frame;
    JFrame NameGetter;
    JLabel label;
    JTextArea chatbubble;
    JTextField textbox1;
    JButton button; 
    JFrame menu;
    JComboBox roomMenu;
    JLabel labelRoom;
   // no longer need them String rooms[]= {"General", "Private Chat-Joseph Jesse", "Private Chat -Katie Rosas", "Games"};
    JLabel allRoomsList;
    JComboBox allRoomMenu;
    // no longer need them String allRooms[]={"Barney", "is", "a ", "dinosaur", "who", "lives", "General", "Private Chat-Joseph Jesse", "Private Chat -Katie Rosas", "Games"};
    String UserName; 
    String RespName; 
    String message;
*/

    JFrame menu;
    String UserName; 

    JFrame NameGetter;
    public IPChat()
    {
        super(null);
        /*
        Color bgColor = new Color(47,79,79);
        //the screen 
        frame= new JFrame("IRC Chat");
        frame.getContentPane().setBackground(bgColor);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
       
        frame.setLayout(new GridBagLayout()); // no need to use a layout manager
        GridBagConstraints c= new GridBagConstraints();
        c.insets= new Insets(2,0,0,0);



        //label for window
        c.gridx=0;
        c.gridy=0;
        c.ipadx=0;
        c.ipady=10;
        label = new JLabel("IRC Chatty Chat");
        label.setFont(new Font("", Font.BOLD,40));
        label.setForeground(Color.WHITE);

        frame.add(label,c);
      
        
        //This is for the actual chat text 
        c.gridx=0;
        c.gridy=4;
        c.ipadx=320;
        c.ipady=400;
        chatbubble = new JTextArea();
        chatbubble.setEditable(false);
        chatbubble.setBackground(Color.lightGray);
        frame.add(chatbubble,c);



        c.gridx=0;
        c.gridy=1;
        c.ipadx=0;
        c.ipady=0;
        labelRoom = new JLabel("Current Room");
        labelRoom.setFont(new Font("", Font.PLAIN,15));
        labelRoom.setForeground(Color.white);
        frame.add(labelRoom,c);

        c.gridx=0;
        c.gridy=2;
        c.ipadx=0;
        c.ipady=0;
        roomMenu= new JComboBox<>();
        frame.add(roomMenu, c);
        //roomMenu.addItem(makeObj("katie"));

         c.gridx=1;
         c.gridy=1;
         c.ipadx=10;
         c.ipady=0;
         allRoomsList= new JLabel ("All Rooms Available");
         allRoomsList.setFont(new Font("", Font.PLAIN,15));
         allRoomsList.setForeground(Color.white);
         frame.add(allRoomsList, c);

         c.gridx=1;
         c.gridy=2;
         c.ipadx=10;
         c.ipady=0;
         allRoomMenu= new JComboBox <> ();
         frame.add(allRoomMenu, c);


        //Adding a textbox for the chatting 
        c.gridx=0;
        c.gridy=5;
        c.ipadx=320;
        c.ipady=20;
        textbox1= new JTextField();
        textbox1.setBackground(Color.lightGray);
        frame.add(textbox1,c);

        
        
        c.gridx=1;
        c.gridy=5;
        c.ipadx=10;
        c.ipady=10;
        button= new JButton("Click to Send Message");
        frame.add(button,c);

        
        button.addActionListener( this);
        button.setActionCommand("SendMessage");



        //frame controls
        frame.pack();

        frame.setLayout(null);
        frame.setSize(600,600);
        frame.setVisible(true);

         USED TO TEST DISPLAY ROOMS 
        ArrayList<String> cars = new ArrayList<String>();
        cars.add("Volvo");
        cars.add("BMW");
        cars.add("Ford");
        cars.add("Mazda");
         displayRooms(cars);


            //userName();
          //  menuOptionMethods();
          // roomMenu.addItem("Hello");
           //roomMenu.addItem("Hi");
           //roomMenu.addItem("yo");

           //roomMenu.addItem("dude");
           //roomMenu.removeItemAt(0);

            // USED FOR TESTING: displayRooms();

            */
    } 



    /**
     * This will be triggered by button to send message! and show up on chat
     * bubble 
     * @param string being sent in to be added to chat 
     *
    public void addMessageToChatBubble(String event )
    {
        chatbubble.append(event);
        //TODO call tellmessage!
    }


    public String getRoomCurrentlySelected(){
        String roomVal= roomMenu.getSelectedItem().toString();
        return roomVal;
    }
*/
    public void  menuOptionMethods(){

        Color bgColor = new Color(47,79,79);
        menu= new JFrame("Menu");
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        menu.getContentPane().setBackground(bgColor);
        JPanel menuBar= new JPanel();
        menuBar.setPreferredSize(new Dimension(200,500));



        JButton listRooms= new JButton("List All Rooms");
        listRooms.setPreferredSize(new Dimension(200, 90));
        menuBar.add(listRooms);

        listRooms.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e){

                ListRooms listRooms = new ListRooms();
                IRC_Packet resp = sendPacketToWelcomeServer(listRooms);
    
                if(isErrPacket(resp)) {
                    handleErrorResponseFromServer( (ErrorPacket) resp);
                } else {
                    ListRoomsResp roomResp = (ListRoomsResp) resp;
                     displayRooms(roomResp.getRooms());
                }

            }
        });

        JButton addRoom= new JButton("Add a Room");
        addRoom.setPreferredSize(new Dimension(200, 90));
        menuBar.add(addRoom);

        addRoom.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e){
                NameGetter= new JFrame("Add A Room");
                String roomAdd=JOptionPane.showInputDialog(NameGetter, "Enter The Name you want to Add");

                System.out.println("this is about to call the openNewRoom function");

                int p= openNewRoomWindow(roomAdd);
                String us= getUsername();

                System.out.println("After the open new room function :)");
                System.out.println("Port number: "+ p);

               JoinRoom roomJoin = new JoinRoom( roomAdd, us, p );
               IRC_Packet resp = sendPacketToWelcomeServer(roomJoin);
   
                System.out.println("sent packet to the server :)");
               if(isErrPacket(resp)) {
                   handleErrorResponseFromServer( (ErrorPacket) resp);
               } else {
                   JoinRoomResp join= (JoinRoomResp) resp;
                
                   System.out.println("response from server recieved");
               }
                     
            }
           
        });


        JButton removeRoom= new JButton("Remove a Room");
        removeRoom.setPreferredSize(new Dimension(200, 90));
        menuBar.add(removeRoom);

        removeRoom.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e){
                NameGetter= new JFrame("Remove A Room");
                String roomRemov=JOptionPane.showInputDialog(NameGetter, "Enter The Name you want to remove");


                LeaveRoom roomRev = new LeaveRoom(roomRemov,getUsername());
                IRC_Packet resp = sendPacketToWelcomeServer(roomRev);

                if(isErrPacket(resp)) {
                    handleErrorResponseFromServer( (ErrorPacket) resp);
                } else {
                   // LeaveRoomResp leaveRoomResponse = (LeaveRoomResp) resp;
                   CRoom toClose = roomsJoined.get(roomRemov);
                   if(toClose == null)
                        System.err.println("Cannot exit the room: " + roomRemov + ". Name does not exist");
                   toClose.closeRoomWindow();
                   roomsJoined.remove(roomRemov);
                }
                      
                }
           
        });

        JButton displayUsers= new JButton("Display All Users");
        displayUsers.setPreferredSize(new Dimension(200, 90));
        menuBar.add(displayUsers);
        displayUsers.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e){
            String roomtoList=JOptionPane.showInputDialog(NameGetter, "Enter The Name of the Room");

            ListUsers listUsers = new ListUsers(roomtoList);
            IRC_Packet resp = sendPacketToWelcomeServer(listUsers);

            if(isErrPacket(resp)) {
                handleErrorResponseFromServer( (ErrorPacket) resp);
            } else {
                ListUsersResponse listUsersResponse = (ListUsersResponse) resp;
                displayUser(listUsersResponse.getUsers());
            }
                  
            }
            
        });


        JButton serverDisconnect= new JButton("Exit IPChat");
        serverDisconnect.setPreferredSize(new Dimension(200, 90));
        menuBar.add(serverDisconnect);

        serverDisconnect.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e){
                
            IRC_Packet resp = sendPacketToWelcomeServer(new GoodBye(getUsername()));

            if(isErrPacket(resp)) {
                handleErrorResponseFromServer( (ErrorPacket) resp);
            } else {
               JFrame disconnect= new JFrame("Disconnecting from Server");
               disconnect.setVisible(true);
               int input = JOptionPane.showConfirmDialog(disconnect, "To exit IPChat, click Yes");
               if(input == 0){
                    disconnect.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               }
               try{
                    System.exit(0);
               }catch(Exception a){
                    a.printStackTrace();
                    System.exit(5);
               }
            }
            }
            
        });

         //menu.setLayout(null);
        menu.getContentPane().add(menuBar);
        menu.pack();
        menu.setVisible(true);
    }


    /**
    * This should be updated with join/create room response packet
    * @param Room
    
    public void addRoomToSubscribed(String Room)
    {
        //TODO call list room! 
        roomMenu.addItem(Room);
    }

    public void addAvailableRoom(String Room)
    {
        allRoomMenu.addItem(Room);

    }

    public void removeRoomFromSubcribed( String room){
        int count= allRoomMenu.getItemCount();
        for(int i=0; i < count; i++){
            String val= allRoomMenu.getItemAt(i).toString();
            if(room == val){
                allRoomMenu.removeItemAt(i);
            }            
        }

        int count2= roomMenu.getItemCount();
        for(int i=0; i < count2; i++){
            String val= roomMenu.getItemAt(i).toString();
            if(room == val){
                roomMenu.removeItemAt(i);
            }            
        }

    }
    */

    public String userName(){
        NameGetter= new JFrame("UserName Response");
        UserName=JOptionPane.showInputDialog(NameGetter, "Enter Your Name");
        while(UserName == null || UserName== ""){

          NameGetter= new JFrame("UserName Response");
          UserName=JOptionPane.showInputDialog(NameGetter, "Enter Your Name");

        }

        return UserName;

        //UserName=set this field to the popup box if joseph jesse responds 
    }
    

    @Override
    public void actionPerformed(ActionEvent e)
    {

            //yelling it needed this :) 
    }

/*
    public String getName(){
        return UserName;
    }

    public String getMessage(){
        return message;
    }
*/
    /**
     * This is to be used for Tell msg 
     * @param UserName
     * @param message
     * @param room
     
    public void displayMessage(String UserName, String message, String room)
    {
        if(getRoomCurrentlySelected()== room){
            chatbubble.append(UserName+ ": " + message + "\n");
        }

    }

    public void displayMessageToRoom(String UserName, String message, String[] rooms){
            for(int i=0; i< rooms.length; i++){
                displayMessage(UserName, message, rooms[i]);
            }

    }
    */
    public void displayRooms(ArrayList <String> rooms){
          JFrame Rooms= new JFrame("List All Rooms");
          Rooms.setVisible(true);
          if(rooms == null){

            JOptionPane.showMessageDialog(Rooms, "Empty");
          }
          else{

          JOptionPane.showMessageDialog(Rooms, "hi" + rooms.toString());
          }
    }
   
    public void displayUser(ArrayList <String> users){
        JFrame user= new JFrame("Showing All Users");
        user.setVisible(true);
        if(users == null){

           JOptionPane.showMessageDialog(user, "Empty");
        }
        else{
        JOptionPane.showMessageDialog(user, users.toString());
        }
    }
    

    public static void main(String [] args)
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
        public void run(){
            IPChat myChat= new IPChat();
            myChat.run();
        }
    });
 }

    public void run() {
        this.handshakeAndUsername();
        this.menuOptionMethods();
    }

    private boolean handshakeAndUsername() {
        String usernameToTry = null;

        try {
            super.openClientSocket();

            ObjectOutputStream outToServer = new ObjectOutputStream(this.clientSocket.getOutputStream());
            System.out.println("Created the object ouptut stream");

            usernameToTry = this.userName();
            outToServer.writeObject(new HandShake(usernameToTry));
            System.out.println("Sending IRC packet to the server");

            ObjectInputStream inFromServer = new ObjectInputStream(this.clientSocket.getInputStream());
            System.out.println("Created the object input stream");
            IRC_Packet irc_Packet = (IRC_Packet) inFromServer.readObject();

            super.closeClientSocket();
            this.handleResponseFromServer(irc_Packet);

            if(irc_Packet.getPacketHeader().getOpCode() == OpCodes.OP_CODE_ERR)
                return this.handshakeAndUsername();

        } catch (SocketTimeoutException exception) {
            System.out.println("ERR: The server has no longer become responsive. Please try connecting again");
            System.exit(1);
        } catch (IOException exception) {
            System.out.println("ERR: IO exception");
            System.exit(1);
        }
        catch(ClassNotFoundException exception){
            System.out.println("ERR: The operation is not available..");
            System.exit(1);
        }

        super.setUsername(usernameToTry);
        return true;
    }


    /**
     * This method:
     * 1. creates a new CRoom (client room) object
     * 2. Opens up the listening socket for the room
     * 3. Runs the new room in parallel
     */
    public int openNewRoomWindow(String roomName) {
        try {
            System.out.println("Line 544 IPChat");
            ServerSocket roomSocket = new ServerSocket(0);
            CRoom newRoom = new CRoom(roomName, roomSocket, username);
            this.addNewRoom(newRoom);
            Thread roomThread = new Thread(newRoom);
            System.out.println("right before attempting to run in parallel");
            roomThread.start();
            System.out.println("right after attempting to run in parallel");
            return newRoom.getListeningSocket().getLocalPort();
        } catch(IOException e) {
            e.printStackTrace();
            System.err.println("ERR: Not able to open the socket for the room.");
        }

        return 0;
    }
    
    /**
     * Add a new room to the Array kept by the clinet of rooms joined
     */
    private void addNewRoom(CRoom room) { this.roomsJoined.put(room.getRoomName(), room); }
}