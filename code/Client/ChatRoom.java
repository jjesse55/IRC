package code.Client;

import java.awt.event.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.*;
import javax.swing.JFrame;
import code.IRC_Packets.IrcPacket;
import code.OpPackets.SendMessage;
import code.OpPackets.SendMessageResp;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import code.ErrorPackets.ErrorPacket;

public class ChatRoom extends GuiBase implements ActionListener, Runnable {

    // class fields
    private final String roomName;
    private final ServerSocket listeningSocket;

    private JFrame frame;
    private JLabel label;
    private JTextArea chatBubble;
    private JTextField textBox1;
    private JButton button;
    private JLabel labelRoom;
    private String message;

    // Run method for room threads
    public void run() {
        while (true) {
            try {
                Socket newConnection = this.listeningSocket.accept();

                ObjectInputStream inFromClient = new ObjectInputStream(newConnection.getInputStream());

                SendMessage msg = (SendMessage) inFromClient.readObject();
                System.out.println(
                        "LOG: New messages recieved for room: " + this.roomName + "\nMessage: " + msg.getMessage());

                displayMessage(msg.getUserName(), msg.getMessage());

                ObjectOutputStream outToClient = new ObjectOutputStream(newConnection.getOutputStream());
                outToClient.writeObject(new SendMessageResp());

                newConnection.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("ERR: Exception raised in attempt to receive a message from the server.");
            }
        }
    }

    // Class methods

    public ChatRoom(String name, ServerSocket listeningSocket, String username) {
        super(username);
        this.roomName = name;
        this.listeningSocket = listeningSocket;

        Color bgColor = new Color(47, 79, 79);
        // the screen
        frame = new JFrame("IRC Chat");
        frame.getContentPane().setBackground(bgColor);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new GridBagLayout()); // no need to use a layout manager
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 0, 0, 0);

        // label for window
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 0;
        c.ipady = 10;
        label = new JLabel("IP Chat");
        label.setFont(new Font("", Font.BOLD, 40));
        label.setForeground(Color.WHITE);
        frame.add(label, c);

        // This is for the actual chat text
        c.gridx = 0;
        c.gridy = 4;
        c.ipadx = 320;
        c.ipady = 400;
        chatBubble = new JTextArea();
        chatBubble.setEditable(false);
        chatBubble.setBackground(Color.lightGray);
        frame.add(chatBubble, c);

        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 0;
        c.ipady = 0;
        labelRoom = new JLabel("Current Room:  " + roomName);
        labelRoom.setFont(new Font("", Font.PLAIN, 15));
        labelRoom.setForeground(Color.white);
        frame.add(labelRoom, c);

        // Adding a textbox for the chatting
        c.gridx = 0;
        c.gridy = 5;
        c.ipadx = 320;
        c.ipady = 20;
        textBox1 = new JTextField();
        textBox1.setBackground(Color.lightGray);
        frame.add(textBox1, c);

        c.gridx = 1;
        c.gridy = 5;
        c.ipadx = 10;
        c.ipady = 10;
        button = new JButton("Click to Send Message");
        frame.add(button, c);

        button.addActionListener(this);
        button.setActionCommand("SendMessage");

        // frame controls
        frame.pack();
        frame.setLayout(null);
        frame.setSize(600, 600);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("SendMessage")) {

            message = textBox1.getText();
            textBox1.setText(null);
            String useName = username;

            SendMessage msgToSend = new SendMessage(message, useName, roomName);
            System.out.println("LOG: Attempting to send message to room: " + roomName + "\nMessage: " + message);

            IrcPacket resp = sendPacketToWelcomeServer(msgToSend);

            if (isErrPacket(resp)) {
                handleErrorResponseFromServer((ErrorPacket) resp);
            } else {
                System.out.println("LOG: Message successfully sent.");
            }
        }
    }

    public String getMessage() {
        return message;
    }

    /**
     * This is to be used for showing a message in the cat bubble and displaying who
     * it is from
     * 
     * @param username()
     * @param message
     */
    public void displayMessage(String username, String message) {
        chatBubble.append(username + ": " + message + "\n");
    }

    public void closeRoomWindow() {
        frame.setVisible(false);
        frame.dispose();
    }

    // Getters
    public ServerSocket getListeningSocket() {
        return this.listeningSocket;
    }

    public String getRoomName() {
        return this.roomName;
    }
}