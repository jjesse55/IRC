package code.Client;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.*;
import javax.swing.JFrame;



//the warning is ok to keep since we will be serializing but not yet!
class ChatSwing extends JFrame implements ActionListener
{

    /**
     *
     */
    JFrame frame;
    JFrame NameGetter;
    JLabel label;
    JTextArea chatbubble;
    JTextField textbox1;
    JButton button; 
    JComboBox roomMenu;
    JLabel labelRoom;
    String rooms[]= {"General", "Private Chat-Joseph Jesse", "Private Chat -Katie Rosas", "Games"};
    JLabel allRoomsList;
    JComboBox allRoomMenu;
    String allRooms[]={"Barney", "is", "a ", "dinosaur", "who", "lives", "General", "Private Chat-Joseph Jesse", "Private Chat -Katie Rosas", "Games"};
    String UserName; 
    String RespName; 
    String message;

/**
 * 
 * 
 */
    ChatSwing()
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
        roomMenu= new JComboBox<>(rooms);
        frame.add(roomMenu, c);

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
         allRoomMenu= new JComboBox <> (allRooms);
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
        
        /**
         * not used not sure how to get this to work yet! 
         * @param 
         */
        button.addActionListener( this);
        button.setActionCommand("SendMessage");

              


        //frame controls
        frame.pack();

        frame.setLayout(null);
        frame.setSize(600,600);
        frame.setVisible(true);

            userName();
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


    /**
    * This should be updated with join/create room response packet
    * @param Room
    */
    public void addRoomToSubscribed(String Room)
    {
        //TODO call list room! 
        

    }

    public void userName(){
        NameGetter= new JFrame("UserName Response");
        UserName=JOptionPane.showInputDialog(NameGetter, "Enter Your Name");
        while(UserName == null || UserName== ""){

          NameGetter= new JFrame("UserName Response");
          UserName=JOptionPane.showInputDialog(NameGetter, "Enter Your Name");

        }

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
    
    public void displayRooms(){
          JFrame Rooms= new JFrame("List All Rooms");
          JOptionPane.showMessageDialog(Rooms, allRooms);
    }
   
    public void displayUser(String [] users){
        JFrame user= new JFrame("Showing All Users");
        JOptionPane.showMessageDialog(user, users);
    }

    public static void main(String [] args)
    {
        
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
        public void run(){
            ChatSwing myChat= new ChatSwing();
            
        }
    });
        
 
}
}