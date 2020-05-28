package code.Client;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
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
import code.OpPackets.SendMessage;
import code.OpPackets.SendMessageResp;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.io.*;
import code.Client.GuiBase;
import code.Codes.OpCodes;
import code.ErrorPackets.ErrorPacket;

import java.util.Arrays;



public class CRoom extends GuiBase implements ActionListener, Runnable
{

    public void run() {
        while(true) {
            try {
                Socket newConnection = this.listeningSocket.accept();

                ObjectInputStream inFromClient = new ObjectInputStream(newConnection.getInputStream());

                SendMessage msg = (SendMessage) inFromClient.readObject();
                System.out.println("Recieved msg from server for room");
                displayMessage(msg.getUserName(), msg.getMessage());

                ObjectOutputStream outToClient = new ObjectOutputStream(newConnection.getOutputStream());
                outToClient.writeObject(new SendMessageResp());

                newConnection.close();
            } catch(Exception e) {
                e.printStackTrace();
                System.err.println("ERR: Exception raised receiving message from the server.");
            }
        }
    }



    ArrayList <String> users = new ArrayList<>(); //change this
    String roomName;
    ServerSocket listeningSocket;

    JFrame frame;
    JFrame NameGetter;
    JLabel label;
    JTextArea chatbubble;
    JTextField textbox1;
    JButton button; 
    JFrame menu;
    JLabel labelRoom;
    JLabel allRoomsList;
    String RespName; 
    String message;

    public CRoom(String name, ServerSocket listeningSocket, String username) {
        super(username);
        System.out.println("Constructing CRoom object");
        this.roomName=name;
        this.listeningSocket = listeningSocket;

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
        label = new JLabel("IP Chat");
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
        labelRoom = new JLabel("Current Room:  " + roomName );
        labelRoom.setFont(new Font("", Font.PLAIN,15));
        labelRoom.setForeground(Color.white);
        frame.add(labelRoom,c);


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
    
    }


   /**
     * This will be triggered by button to send message! and show up on chat
     * bubble 
     * @param string being sent in to be added to chat 
     */
    
     public void addMessageToChatBubble(String event )
    {
        chatbubble.append(event);
    }



    public String userName(){
        NameGetter= new JFrame("UserName Response");
        username=JOptionPane.showInputDialog(NameGetter, "Enter Your Name");
        while(username == null || username== ""){

          NameGetter= new JFrame("UserName Response");
          username=JOptionPane.showInputDialog(NameGetter, "Enter Your Name");

        }

        return username;

        //UserName=set this field to the popup box if joseph jesse responds 
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String action= e.getActionCommand();
        if(action.equals("SendMessage")){

            message= textbox1.getText();
            textbox1.setText(null);
            String useName= username;

            SendMessage msgToSend = new SendMessage(message, useName, roomName);
            System.out.println("Room NAME "+ roomName);
            
            System.out.println("CROOM before packet from server recieved");

            IRC_Packet resp = sendPacketToWelcomeServer(msgToSend);

            System.out.println("CROOM after packet from server recieved");

            if(isErrPacket(resp)) {
                handleErrorResponseFromServer( (ErrorPacket) resp);
            } else {
                SendMessageResp roomResp = (SendMessageResp) resp;
            }
        }


    }

    public String getMessage(){
        return message;
    }


     /**
     * This is to be used for Tell msg 
     * @param username()
     * @param message
     */
    public void displayMessage(String username, String message)
    {
            chatbubble.append(username+ ": " + message + "\n");

    }
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

    public void closeRoomWindow(){

        frame.setVisible(false);
        frame.dispose();
    }



    public static void main(String [] args){

    }

    public ServerSocket getListeningSocket() { return this.listeningSocket; }

    public String getRoomName() { return this.roomName; }
}