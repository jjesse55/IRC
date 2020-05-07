package code.Client;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.*;
import javax.swing.JFrame;



class ChatSwing
{

    
    JFrame frame;
    JLabel label;
    JTextArea chatbubble;
    JTextField textbox1;
    JButton button; 
    JComboBox roomMenu;
    JLabel labelRoom;
    String rooms[]= {"General", "Private Chat-Joseph Jesse", "Private Chat -Katie Rosas", "Games"};
    JLabel allRoomsList;
    JComboBox allRoomMenu;
    String allRooms[]={"Barney", "is", "a ", "dinosaur", "who", "lives"};


/**
 * very rough start to the layout! 
 * @param not used
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
        c.ipadx=40;
        c.ipady=20;
        label = new JLabel("Welcome to chat");
        label.setForeground(Color.DARK_GRAY);

        frame.add(label,c);
      
        
        //This is for the actual chat text 
        c.gridx=0;
        c.gridy=2;
        c.ipadx=200;
        c.ipady=400;
        chatbubble = new JTextArea("messages shown here");
        chatbubble.setBackground(Color.lightGray);
        frame.add(chatbubble,c);



        c.gridx=1;
        c.gridy=1;
        c.ipadx=0;
        c.ipady=90;
        labelRoom = new JLabel("Rooms Subscribed");
        labelRoom.setBackground(Color.GREEN);
        frame.add(labelRoom,c);

        c.gridx=2;
        c.gridy=1;
        c.ipadx=10;
        c.ipady=10;
        roomMenu= new JComboBox<>(rooms);
        frame.add(roomMenu, c);

         c.gridx=1;
         c.gridy=0;
         c.ipadx=10;
         c.ipady=0;
         allRoomsList= new JLabel ("All Rooms Available");
         frame.add(allRoomsList, c);

         c.gridx=2;
         c.gridy=0;
         c.ipadx=10;
         c.ipady=0;
         allRoomMenu= new JComboBox <> (allRooms);
         frame.add(allRoomMenu, c);






        //Adding a textbox for the chatting 
        c.gridx=0;
        c.gridy=4;
        c.ipadx=90;
        c.ipady=20;
        textbox1= new JTextField("Enter Message Here");
        textbox1.setBackground(Color.CYAN);
        frame.add(textbox1,c);

        
        
        c.gridx=1;
        c.gridy=4;
        c.ipadx=10;
        c.ipady=10;
        button= new JButton("Click to Send Message");
        frame.add(button,c);
        
        /**
         * not used not sure how to get this to work yet! 
         * @param 
         */
        button.addActionListener( new ActionListener(){
            
        public void actionPerformed(ActionEvent e){
            addMessageToChatBubble("hello");

              
         }

        });

        //frame controls
        frame.pack();

        frame.setLayout(null);
        frame.setSize(900,900);
        frame.setVisible(true);

    }  
    /**
     * This will be triggered by button to send message! and show up on chat
     * bubble 
     * @param string being sent in to be added to chat 
     */
    public static void addMessageToChatBubble(String event ){
    }

    public static void main(String [] args)
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
        public void run(){
            new ChatSwing();
        }
    });
 
}
}