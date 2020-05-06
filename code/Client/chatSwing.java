package code.Client;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;


class ChatSwing
{

    
    JFrame frame;
    JLabel label;
    JTextArea chatbubble;
    JTextArea rooms;
    JTextField textbox1;
    JButton button; 


/**
 * very rough start to the layout! 
 * @param not used
 */
    ChatSwing()
    {


        /*JFrame frame;
        JLabel label;
        JTextArea chatbubble;
        JTextArea rooms;
        JTextField textbox1;
        JButton button; */
        
        
        //the screen 
        frame= new JFrame("IRC Chat");
        frame.setBackground(Color.LIGHT_GRAY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //label for window
        label = new JLabel("Welcome to chat");
        label.setBounds(50, 100, 400, 100);
        label.setLocation(0, 0);
        frame.add(label);
      
        
        //This is for the actual chat text 
        chatbubble = new JTextArea("messages shown here");
        chatbubble.setBounds(50,300,200,200);
        chatbubble.setBackground(Color.lightGray);
        frame.add(chatbubble);


        //this is for the Rooms 
        rooms= new JTextArea("Rooms");
        rooms.setBounds(350, 50, 100, 400);
        frame.add(rooms);





        //Adding a textbox for the chatting 
        textbox1= new JTextField("Enter Message Here");
        textbox1.setBounds(50,500,400,50);
        textbox1.setBackground(Color.CYAN);
        frame.add(textbox1);

        
        
        //assuming we have an open button
        //KATIE! FIX THIS BUTTON PLACEMENT 
        button= new JButton("Click to Send Message");
        button.setBounds(100, 600, 95, 30);
       // frame.add(button, BorderLayout.LINE_START);
        frame.add(button);
        
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
        frame.setSize(500,600);
        frame.setLayout(null); // no need to use a layout manager
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