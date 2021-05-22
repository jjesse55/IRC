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
    private final String ROOM_NAME;
    private final ServerSocket LISTENING_SOCKET;

    private final JFrame FRAME = new JFrame("IRC Chat");
    private final JLabel LABEL = new JLabel("IP Chat");
    private final JTextArea CHAT_BUBBLE = new JTextArea();
    private final JTextField TEXT_BOX_1 = new JTextField();
    private final JButton BUTTON = new JButton("Click to Send Message");
    private final JLabel LABEL_ROOM = new JLabel();

    // Run method for room threads
    public void run() {
        while (true) {
            try {
                Socket newConnection = this.LISTENING_SOCKET.accept();

                ObjectInputStream inFromClient = new ObjectInputStream(newConnection.getInputStream());

                SendMessage msg = (SendMessage) inFromClient.readObject();
                System.out.println(
                        "LOG: New messages recieved for room: " + this.ROOM_NAME + "\nMessage: " + msg.getMessage());

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

    public ChatRoom(String name, ServerSocket LISTENING_SOCKET, String username) {
        super(username);
        this.ROOM_NAME = name;
        this.LISTENING_SOCKET = LISTENING_SOCKET;

        Color bgColor = new Color(47, 79, 79);
        // the screen
        FRAME.getContentPane().setBackground(bgColor);
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FRAME.setLayout(new GridBagLayout()); // no need to use a layout manager
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 0, 0, 0);

        // LABEL for window
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 0;
        c.ipady = 10;
        LABEL.setFont(new Font("", Font.BOLD, 40));
        LABEL.setForeground(Color.WHITE);
        FRAME.add(LABEL, c);

        // This is for the actual chat text
        c.gridx = 0;
        c.gridy = 4;
        c.ipadx = 320;
        c.ipady = 400;
        CHAT_BUBBLE.setEditable(false);
        CHAT_BUBBLE.setBackground(Color.lightGray);
        FRAME.add(CHAT_BUBBLE, c);

        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 0;
        c.ipady = 0;
        LABEL_ROOM.setText("Current Room:  " + ROOM_NAME);
        LABEL_ROOM.setFont(new Font("", Font.PLAIN, 15));
        LABEL_ROOM.setForeground(Color.white);
        FRAME.add(LABEL_ROOM, c);

        // Adding a textbox for the chatting
        c.gridx = 0;
        c.gridy = 5;
        c.ipadx = 320;
        c.ipady = 20;
        TEXT_BOX_1.setBackground(Color.lightGray);
        FRAME.add(TEXT_BOX_1, c);

        c.gridx = 1;
        c.gridy = 5;
        c.ipadx = 10;
        c.ipady = 10;
        FRAME.add(BUTTON, c);

        BUTTON.addActionListener(this);
        BUTTON.setActionCommand("SendMessage");

        // FRAME controls
        FRAME.pack();
        FRAME.setLayout(null);
        FRAME.setSize(600, 600);
        FRAME.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("SendMessage")) {

            String message = TEXT_BOX_1.getText();
            TEXT_BOX_1.setText(null);
            String useName = username;

            SendMessage msgToSend = new SendMessage(message, useName, ROOM_NAME);
            System.out.println("LOG: Attempting to send message to room: " + ROOM_NAME + "\nMessage: " + message);

            IrcPacket resp = sendPacketToWelcomeServer(msgToSend);

            if (isErrPacket(resp)) {
                handleErrorResponseFromServer((ErrorPacket) resp);
            } else {
                System.out.println("LOG: Message successfully sent.");
            }
        }
    }

    /**
     * This is to be used for showing a message in the cat bubble and displaying who
     * it is from
     * 
     * @param username()
     * @param message
     */
    public void displayMessage(String username, String message) {
        CHAT_BUBBLE.append(username + ": " + message + "\n");
    }

    public void closeRoomWindow() {
        FRAME.setVisible(false);
        FRAME.dispose();
    }

    // Getters
    public ServerSocket getListeningSocket() {
        return this.LISTENING_SOCKET;
    }

    public String getRoomName() {
        return this.ROOM_NAME;
    }
}