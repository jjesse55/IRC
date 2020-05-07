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
    String allRooms[]={"Barney", "is", "a ", "dinosaur", "who", "lives", "General", "Private Chat-Joseph Jesse", "Private Chat -Katie Rosas", "Games"};


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
        c.ipadx=0;
        c.ipady=10;
        label = new JLabel("IRC Chatty Chat");
        label.setFont(new Font("", Font.BOLD,40));
        label.setForeground(Color.WHITE);

        frame.add(label,c);
      
        
        //This is for the actual chat text 
        c.gridx=0;
        c.gridy=4;
        c.ipadx=200;
        c.ipady=400;
        chatbubble = new JTextArea("messages loading..");
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
        button.addActionListener( new ActionListener(){
            
        public void actionPerformed(ActionEvent e){
            addMessageToChatBubble("hello");

              
         }

        });

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