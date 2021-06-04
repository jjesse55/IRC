package code.Client;

import java.awt.event.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.*;
import javax.swing.JFrame;
import code.IRC_Packets.IrcPacket;
import code.OpPackets.SendMessage;
import code.OpPackets.SendMessageResponse;
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

    public ChatRoom(String name, ServerSocket LISTENING_SOCKET, String username) {
        super(username);
        this.ROOM_NAME = name;
        this.LISTENING_SOCKET = LISTENING_SOCKET;

        Color backgroundColor = new Color(47, 79, 79);
        this.FRAME.getContentPane().setBackground(backgroundColor);
        this.FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.FRAME.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(2, 0, 0, 0);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipadx = 0;
        constraints.ipady = 10;
        this.LABEL.setFont(new Font("", Font.BOLD, 40));
        this.LABEL.setForeground(Color.WHITE);
        this.FRAME.add(this.LABEL, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.ipadx = 320;
        constraints.ipady = 400;
        this.CHAT_BUBBLE.setEditable(false);
        this.CHAT_BUBBLE.setBackground(Color.lightGray);
        this.FRAME.add(this.CHAT_BUBBLE, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        this.LABEL_ROOM.setText("Current Room:  " + ROOM_NAME);
        this.LABEL_ROOM.setFont(new Font("", Font.PLAIN, 15));
        this.LABEL_ROOM.setForeground(Color.white);
        this.FRAME.add(this.LABEL_ROOM, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.ipadx = 320;
        constraints.ipady = 20;
        this.TEXT_BOX_1.setBackground(Color.lightGray);
        this.FRAME.add(this.TEXT_BOX_1, constraints);

        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.ipadx = 10;
        constraints.ipady = 10;
        this.FRAME.add(this.BUTTON, constraints);

        this.BUTTON.addActionListener(this);
        this.BUTTON.setActionCommand("SendMessage");

        this.FRAME.pack();
        this.FRAME.setLayout(null);
        this.FRAME.setSize(600, 600);
        this.FRAME.setVisible(true);
    }

    public void run() {
        while (true) {
            try {
                Socket newConnection = this.LISTENING_SOCKET.accept();
                ObjectInputStream inFromServer = new ObjectInputStream(newConnection.getInputStream());
                SendMessage message = (SendMessage) inFromServer.readObject();
                System.out.println(
                        "LOG: New messages received for room: " + this.ROOM_NAME + "\nMessage: " + message.getMessage());
                this.displayMessage(message.getUserName(), message.getMessage());
                ObjectOutputStream outToServer = new ObjectOutputStream(newConnection.getOutputStream());
                outToServer.writeObject(new SendMessageResponse());
                newConnection.close();
            } catch (Exception exception) {
                System.err.println("ERR: Exception raised in attempt to receive a message from the server.");
            }
        }
    }

    private void displayMessage(String username, String message) {
        this.CHAT_BUBBLE.append(username + ": " + message + "\n");
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String action = actionEvent.getActionCommand();
        if (action.equals("SendMessage")) {
            String message = this.TEXT_BOX_1.getText();
            this.TEXT_BOX_1.setText(null);
            String userName = username;
            SendMessage messageToSend = new SendMessage(message, userName, this.ROOM_NAME);
            System.out.println("LOG: Attempting to send message to room: " + this.ROOM_NAME + "\nMessage: " + message);
            IrcPacket response = super.sendPacketToServer(messageToSend);
            if (isErrorPacket(response)) {
                super.handleErrorResponseFromServer((ErrorPacket) response);
            } else {
                System.out.println("LOG: Message successfully sent.");
            }
        }
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