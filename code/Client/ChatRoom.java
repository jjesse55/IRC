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

    private final String ROOM_NAME;
    private final ServerSocket LISTENING_SOCKET;

    private final JFrame FRAME = new JFrame("IRC Chat");
    private final JLabel LABEL = new JLabel("IP Chat");
    private final JTextArea CHAT_BUBBLE = new JTextArea();
    private final JTextField TEXT_BOX_1 = new JTextField();
    private final JButton BUTTON = new JButton("Click to Send Message");
    private final JLabel LABEL_ROOM = new JLabel();

    public void run() {
        while (true) {
            try {
                Socket newConnection = this.LISTENING_SOCKET.accept();

                ObjectInputStream inFromClient = new ObjectInputStream(newConnection.getInputStream());

                SendMessage message = (SendMessage) inFromClient.readObject();
                System.out.println(
                        "LOG: New messages recieved for room: " + this.ROOM_NAME + "\nMessage: " + message.getMessage());

                displayMessage(message.getUserName(), message.getMessage());

                ObjectOutputStream outToClient = new ObjectOutputStream(newConnection.getOutputStream());
                outToClient.writeObject(new SendMessageResp());

                newConnection.close();
            } catch (Exception exception) {
                exception.printStackTrace();
                System.err.println("ERR: Exception raised in attempt to receive a message from the server.");
            }
        }
    }

    public ChatRoom(String name, ServerSocket LISTENING_SOCKET, String username) {
        super(username);
        this.ROOM_NAME = name;
        this.LISTENING_SOCKET = LISTENING_SOCKET;

        Color backgroundColor = new Color(47, 79, 79);
        this.FRAME.getContentPane().setBackground(backgroundColor);
        this.FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.FRAME.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 0, 0, 0);

        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 0;
        c.ipady = 10;
        this.LABEL.setFont(new Font("", Font.BOLD, 40));
        this.LABEL.setForeground(Color.WHITE);
        this.FRAME.add(this.LABEL, c);

        c.gridx = 0;
        c.gridy = 4;
        c.ipadx = 320;
        c.ipady = 400;
        this.CHAT_BUBBLE.setEditable(false);
        this.CHAT_BUBBLE.setBackground(Color.lightGray);
        this.FRAME.add(this.CHAT_BUBBLE, c);

        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 0;
        c.ipady = 0;
        this.LABEL_ROOM.setText("Current Room:  " + ROOM_NAME);
        this.LABEL_ROOM.setFont(new Font("", Font.PLAIN, 15));
        this.LABEL_ROOM.setForeground(Color.white);
        this.FRAME.add(this.LABEL_ROOM, c);

        c.gridx = 0;
        c.gridy = 5;
        c.ipadx = 320;
        c.ipady = 20;
        this.TEXT_BOX_1.setBackground(Color.lightGray);
        this.FRAME.add(this.TEXT_BOX_1, c);

        c.gridx = 1;
        c.gridy = 5;
        c.ipadx = 10;
        c.ipady = 10;
        this.FRAME.add(this.BUTTON, c);

        this.BUTTON.addActionListener(this);
        this.BUTTON.setActionCommand("SendMessage");

        this.FRAME.pack();
        this.FRAME.setLayout(null);
        this.FRAME.setSize(600, 600);
        this.FRAME.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent exception) {
        String action = exception.getActionCommand();
        if (action.equals("SendMessage")) {

            String message = this.TEXT_BOX_1.getText();
            this.TEXT_BOX_1.setText(null);
            String useName = username;

            SendMessage messageToSend = new SendMessage(message, useName, this.ROOM_NAME);
            System.out.println("LOG: Attempting to send message to room: " + this.ROOM_NAME + "\nMessage: " + message);

            IrcPacket response = sendPacketToWelcomeServer(messageToSend);

            if (isErrorPacket(response)) {
                handleErrorResponseFromServer((ErrorPacket) response);
            } else {
                System.out.println("LOG: Message successfully sent.");
            }
        }
    }

    public void displayMessage(String username, String message) {
        this.CHAT_BUBBLE.append(username + ": " + message + "\n");
    }

    public void closeRoomWindow() {
        this.FRAME.setVisible(false);
        this.FRAME.dispose();
    }

    public ServerSocket getListeningSocket() {
        return this.LISTENING_SOCKET;
    }

    public String getRoomName() {
        return this.ROOM_NAME;
    }
}