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
    //JTextArea rooms;
    JTextField textbox1;
    JButton button; 
    JComboBox roomMenu;
    JLabel labelRoom;
    String rooms[]= {"General", "Private Chat-Joseph Jesse", "Private Chat -Katie Rosas", "Games"};


/**
 * very rough start to the layout! 
 * @param not used
 */
    ChatSwing()
    {
        //the screen 
        frame= new JFrame("IRC Chat");
        frame.setBackground(Color.LIGHT_GRAY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
       
        frame.setLayout(new GridBagLayout()); // no need to use a layout manager
        GridBagConstraints c= new GridBagConstraints();
        c.insets= new Insets(10,10,10,10);



        //label for window
        c.gridx=0;
        c.gridy=0;
        c.ipadx=40;
        c.ipady=20;
        label = new JLabel("Welcome to chat");
  //      label.setBounds(50, 100, 400, 100);
        label.setLocation(0, 0);
        frame.add(label,c);
      
        
        //This is for the actual chat text 
        c.gridx=0;
        c.gridy=1;
        c.ipadx=90;
        c.ipady=300;
        chatbubble = new JTextArea("messages shown here");
//        chatbubble.setBounds(50,300,200,200);
        chatbubble.setBackground(Color.lightGray);
        frame.add(chatbubble,c);


        //this is for the Rooms 
        //change this to a menu screen!! or checkboxes :?
        //so we can tell what room the person is in 
        /*
        rooms= new JTextArea("Rooms");
        rooms.setBounds(350, 50, 100, 400);
        frame.add(rooms);
        */

        c.gridx=1;
        c.gridy=0;
       // c.ipadx=30;
        c.ipady=90;
        labelRoom = new JLabel("Rooms Available");
//        labelRoom.setBounds(350,10,400, 100);
        labelRoom.setBackground(Color.GREEN);
        frame.add(labelRoom,c);

        c.gridx=1;
        c.gridy=1;
        //c.ipadx=20;
        c.ipady=300;
        roomMenu= new JComboBox<>(rooms);
//        roomMenu.setBounds(350, 50, 100, 100);
        frame.add(roomMenu,c);






        //Adding a textbox for the chatting 
        c.gridx=0;
        c.gridy=3;
        c.ipadx=90;
        c.ipady=20;
        textbox1= new JTextField("Enter Message Here");
        //textbox1.setBounds(50,500,400,50);
        textbox1.setBackground(Color.CYAN);
        frame.add(textbox1,c);

        
        
        //assuming we have an open button
        //KATIE! FIX THIS BUTTON PLACEMENT 
        c.gridx=1;
        c.gridy=3;
        c.ipadx=20;
        c.ipady=20;
        button= new JButton("Click to Send Message");
        //button.setBounds(100, 600, 95, 30);
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
        frame.setSize(500,600);
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