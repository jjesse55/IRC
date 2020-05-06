package code.Client;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;


class ChatSwing implements ActionListener
{

/**
 * very rough start to the layout! 
 * @param not used
 */
    private static void createAndDisplayGUI()
    {
        //the screen 
        JFrame frame= new JFrame("IRC Chat");
        frame.setBackground(Color.LIGHT_GRAY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //label for window
        JLabel label = new JLabel("Welcome to chat");
        label.setBounds(50, 100, 400, 100);
        label.setLocation(0, 0);
        frame.add(label);
       
        JTextArea chatbubble = new JTextArea("messages shown here");
        chatbubble.setBounds(50,300,200,200);
        chatbubble.setBackground(Color.lightGray);
        frame.add(chatbubble);




        //Adding a textbox for the chatting 
        JTextField textbox1= new JTextField("Enter Message Here");
        textbox1.setBounds(50,500,400,50);
        textbox1.setBackground(Color.CYAN);
        frame.add(textbox1);

        
        
        //assuming we have an open button
        //KATIE! FIX THIS BUTTON PLACEMENT 
        JButton button= new JButton("Click to Send Message");
        button.setBounds(50, 600, 95, 30);
        frame.add(button);


        //frame controls
        frame.pack();
        frame.setSize(500,600);
        frame.setLayout(null); // no need to use a layout manager
        frame.setVisible(true);

    }  
    /**
     * This will be triggered by button to send message! and show up on chat
     * bubble 
     * @param ActionEvent 
     */
    public void actionPerformed(ActionEvent e){

    }
    public static void main(String [] args)
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
        public void run(){
            createAndDisplayGUI();
        }
    });
 
}
}