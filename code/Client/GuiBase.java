package code.Client;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.io.*;
import java.net.Socket;
import code.Codes.OpCodes;
import code.ErrorPackets.ErrorPacket;
import code.IRC_Packets.IrcPacket;

public abstract class GuiBase extends JFrame {

    protected String username;
    protected Socket clientSocket;

    protected static final int SERVER_PORT = 7777;
    protected static final String SERVER_HOST = "localhost";


    protected GuiBase(String username) {
        this.username = username;
    }

    protected IrcPacket sendPacketToServer(IrcPacket packet) {
        IrcPacket toReturn = null;
        try {
            this.openClientSocket();
            ObjectOutputStream outToServer = new ObjectOutputStream(this.clientSocket.getOutputStream());
            outToServer.writeObject(packet);
            ObjectInputStream inFromServer = new ObjectInputStream(this.clientSocket.getInputStream());
            toReturn = (IrcPacket) inFromServer.readObject();
            inFromServer.close();
            this.closeClientSocket();
        } catch (Exception e) {
            System.out.println("ERR: Unable to exchange data with the server.");
            this.serverCrashes();
        }
        return toReturn;
    }

    protected void openClientSocket() {
        try {
            this.clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
        } catch (Exception exception) {
            System.err.println("ERR: Unable to open the client socket");
            this.serverCrashes();
        }
    }

    protected void closeClientSocket() {
        try {
            this.clientSocket.close();
        } catch (IOException exception) {
            System.err.println("ERR: Unable to close the client socket");
        }
    }

    protected void serverCrashes() {
        JFrame serverCrash = new JFrame("Server Stopped Responding");
        serverCrash.setVisible(true);
        JOptionPane.showMessageDialog(serverCrash, "The Server has stopped responding, IP Chat Exiting");
        serverCrash.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.exit(1);
    }

    protected void handleResponseFromServer(IrcPacket response) {
        switch (response.getPacketHeader().getOpCode()) {
            case OP_CODE_ERROR:
                ErrorPacket errorPacket = (ErrorPacket) response;
                this.handleErrorResponseFromServer(errorPacket);
                break;
            case OP_CODE_KEEP_ALIVE:
            case OP_CODE_HELLO:
            case OP_CODE_LIST_ROOMS_RESPONSE:
            case OP_CODE_LIST_USERS_RESPONSE:
            case OP_CODE_JOIN_ROOM_RESPONSE:
            case OP_CODE_LEAVE_ROOM_RESPONSE:
        }
    }

    protected void handleErrorResponseFromServer(ErrorPacket errorPacket) {
        switch (errorPacket.getErrorCode()) {
            case IRC_ERROR_ILLEGAL_OPCODE:
                System.err.println("ERR: Unsupported Operation Requested... Cannot perform.");
                break;
            case IRC_ERROR_ILLEGAL_LENGTH:
                System.err.println("ERR: Illegal packet length (too long).");
                break;
            case IRC_ERROR_NAME_EXISTS:
                System.err.println("ERR: Name already exists for user. Please try again using a different user name");
                break;
            case IRC_ERROR_ILLEGAL_PROTOCOL:
                System.err.println("ERR: Illegal protocol for application user... System exiting");
                System.exit(1);
                break;
            case IRC_ERROR_INVALID_ROOM_NAME:
                System.err.println("ERR: Name of room specified does not match that of an existing room."
                        + " Please try again...");
                break;
            default:
                System.err.println("ERR: Unknown server error. System exiting..");
                System.exit(1);
        }
    }

    protected String getUsername() {
        return this.username;
    }

    protected void setUsername(String username) {
        this.username = username;
    }

    protected boolean isErrorPacket(IrcPacket packet) {
        return packet.getPacketHeader().getOpCode() == OpCodes.OP_CODE_ERROR;
    }
}