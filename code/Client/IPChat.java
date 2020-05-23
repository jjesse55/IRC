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
import code.OpPackets.JoinRoom;
import code.OpPackets.JoinRoomResp;
import code.OpPackets.ListRoomsResp;
import code.OpPackets.ListUsers;
import code.OpPackets.ListUsersResponse;
import java.net.Socket;
import java.io.*;
import code.Client.GuiBase;
import java.util.ArrayList;
import java.util.Arrays;




//the warning is ok to keep since we will be serializing but not yet!
class IPChat extends GuiBase implements ActionListener
{

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

    IPChat()
    {
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
/*
         USED TO TEST DISPLAY ROOMS 
        ArrayList<String> cars = new ArrayList<String>();
        cars.add("Volvo");
        cars.add("BMW");
        cars.add("Ford");
        cars.add("Mazda");
         displayRooms(cars);
*/

            //userName();
          //  menuOptionMethods();
          // roomMenu.addItem("Hello");
           //roomMenu.addItem("Hi");
           //roomMenu.addItem("yo");

           //roomMenu.addItem("dude");
           //roomMenu.removeItemAt(0);

            // USED FOR TESTING: displayRooms();

    } 



    /**
     * This will be triggered by button to send message! and show up on chat
     * bubble 
     * @param string being sent in to be added to chat 
     */
    public void addMessageToChatBubble(String event )
    {
        chatbubble.append(event);
        //TODO call tellmessage!
    }


    public String getRoomCurrentlySelected(){
        String roomVal= roomMenu.getSelectedItem().toString();
        return roomVal;
    }

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

                try{
                openClientSocket();

                ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                System.out.println("Created the object ouptut stream");

                outToServer.writeObject(new ListRooms());
                System.out.println("Sending IRC packet to list rooms");       

                ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
                System.out.println("GOT the List of rooms from server! ");

                 IRC_Packet irc_Packet = (IRC_Packet) inFromServer.readObject(); 

                ListRoomsResp roomresp = (ListRoomsResp) irc_Packet;

                ArrayList ro = roomresp.getRooms();
                System.out.println(ro);
                      
                 displayRooms(ro);

                inFromServer.close();
                closeClientSocket();
                  
                }
                catch(IOException ex){
                    //TODO no clue here
                    System.out.println("Err: IO");

                }
                catch(ClassNotFoundException exception){
                    //TODO ERROR and try again?
                    System.out.println("Err: Class Not Found");

                }
                catch(Exception exception){
                    //TODO error and try again 
                    System.out.println("Err:  Exception");
                    System.exit(0);

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
                addRoomToSubscribed(roomAdd);
           
           
                try{
                    openClientSocket();
                   ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                    System.out.println("Created the object ouptut stream");
    
                    outToServer.writeObject(new JoinRoom(roomAdd));
                    System.out.println("Sending IRC packet to join/ add room ");       
    
                    ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
                    System.out.println("GOT the room/joined ");
    
                     IRC_Packet irc_Packet = (IRC_Packet) inFromServer.readObject(); 
                    closeClientSocket();


                inFromServer.close();

                    }
                    catch(IOException ex){
                        //TODO no clue here
                        System.out.println("eroor IO");
                        System.exit(0);
    
                    }
                    catch(ClassNotFoundException exception){
                        //TODO ERROR and try again?
                        System.out.println("eroor class not found");
    
                    }
                    catch(Exception exception){
                        //TODO error and try again 
                        System.out.println("eroor exception");
                        //System.exit(0);
    
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
                removeRoomFromSubcribed(roomRemov);

                try{
                    openClientSocket();
                    ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                    System.out.println("Created the object ouptut stream");
    
                    outToServer.writeObject(new LeaveRoom(roomRemov));
                    System.out.println("Sending IRC packet to leave room ");       
    
                    ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
                    System.out.println("Room Removed Packet from Server");
    
                     IRC_Packet irc_Packet = (IRC_Packet) inFromServer.readObject(); 
                inFromServer.close();
                    closeClientSocket();
                  
    
                      
                    }
                    catch(IOException ex){
                        //TODO no clue here
                        System.out.println("eroor IO");
                        System.exit(0);
    
                    }
                    catch(ClassNotFoundException exception){
                        //TODO ERROR and try again?
                        System.out.println("eroor class not found");
    
                    }
                    catch(Exception exception){
                        //TODO error and try again 
                        System.out.println("eroor exception");
                        //System.exit(0);
    
                    }
                }
           
        });

        JButton displayUsers= new JButton("Display All Users");
        displayUsers.setPreferredSize(new Dimension(200, 90));
        menuBar.add(displayUsers);

        displayUsers.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e){
                
                try{
                    openClientSocket();
                   ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                    System.out.println("Created the object ouptut stream");
    
                    outToServer.writeObject(new ListUsers());
                    System.out.println("Sending IRC packet to list rooms");       
    
                    ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
                    System.out.println("GOT the List of users from server! ");
    
                     IRC_Packet irc_Packet = (IRC_Packet) inFromServer.readObject(); 
    
                    ListUsersResponse roomresp = (ListUsersResponse) irc_Packet;
    
                    ArrayList<String> ro = roomresp.getUsers();
                    System.out.println(ro);
                          
                     displayUser(ro);
                    inFromServer.close();
                    closeClientSocket();
                      
                    }
                    catch(IOException ex){
                        //TODO no clue here
                        System.out.println("Err: IO Exception");
                        System.exit(0);
    
                    }
                    catch(ClassNotFoundException exception){
                        //TODO ERROR and try again?
                        System.out.println("ERR: Class Not Found");
    
                    }
                    catch(Exception exception){
                        //TODO error and try again 
                       System.out.println("ERR: exception");
                        //System.exit(0);
    
                    }
                }
            
        });






        JButton serverDisconnect= new JButton("Disconnect from Server");
        serverDisconnect.setPreferredSize(new Dimension(200, 90));
        menuBar.add(serverDisconnect);

        serverDisconnect.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e){
                
                try{
                    openClientSocket();

                    /*
                   ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                    System.out.println("Created the object ouptut stream");
    
                    outToServer.writeObject(new ListUsers());
                    System.out.println("Sending IRC packet to list rooms");       
    
                    ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
                    System.out.println("GOT the List of users from server! ");
    
                     IRC_Packet irc_Packet = (IRC_Packet) inFromServer.readObject(); 
    
                    ListUsersResponse roomresp = (ListUsersResponse) irc_Packet;
    
                    ArrayList<String> ro = roomresp.getUsers();
                    System.out.println(ro);
                          
                     displayUser(ro);
                     

                    inFromServer.close();
*/                    closeClientSocket();
                    frame.setVisible(false);
                    frame.dispose();
                      
                    }
                    catch(Exception exception){
                        //TODO error and try again 
                       System.out.println("ERR: exception");
                        //System.exit(0);
    
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
    */
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
        String action= e.getActionCommand();
        if(action.equals("SendMessage")){
            message= textbox1.getText();
            chatbubble.append(UserName+ ": " +message + "\n");
            textbox1.setText(null);
        }


    }


    public String getName(){
        return UserName;
    }

    public String getMessage(){
        return message;
    }

    /**
     * This is to be used for Tell msg 
     * @param UserName
     * @param message
     * @param room
     */
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

            myChat.handshakeAndUsername("sjdhjskd");
            myChat.menuOptionMethods();
        }
    });
}
}