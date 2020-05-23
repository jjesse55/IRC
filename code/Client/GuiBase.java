package code.Client;

import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;

import code.Codes.ErrorCodes;
import code.Codes.OpCodes;
import code.ErrorPackets.ErrorPacket;
import code.IRC_Packets.IRC_Packet;
import code.OpPackets.HandShake;
import code.OpPackets.OpPackets;

public abstract class GuiBase extends JFrame {

    //Fields for connecting to the server
    protected Socket clientSocket;
    protected static final int SERVER_PORT = 7777; // Port number for the client process
    protected static final String SERVER_HOST = "localhost";

    protected String username;

    
    protected void openClientSocket() {
        try {
            this.clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
        } catch(Exception e) {
            e.printStackTrace();
            System.err.println("Unable to open the client socket");
        }
    }

    protected void closeClientSocket() {
        try {
            this.clientSocket.close();
        } catch(IOException e) {
            e.printStackTrace();
            System.err.println("Unable to close the client socket");
        }
    }


    protected IRC_Packet sendPacketToWelcomeServer(IRC_Packet packet) {
        IRC_Packet toReturn = null;

        try{
            openClientSocket();
            ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
            System.out.println("Created the object ouptut stream");

            outToServer.writeObject(packet);
            System.out.println("Sending IRC packet to list rooms");       

            ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("GOT the List of users from server! ");

            toReturn = (IRC_Packet) inFromServer.readObject(); 
            
            inFromServer.close();
            closeClientSocket();
            }
            catch(IOException ex){
                //TODO no clue here
                System.out.println("Err: IO Exception");
                System.exit(0);

            }
            catch(ClassNotFoundException exception){
                //TODO ERROR and try again?
                System.out.println("ERR: Class Not Found");

            }
            catch(Exception exception){
                //TODO error and try again 
               System.out.println("ERR: exception");
                //System.exit(0);
            }

        return toReturn;
    }

    /**
     * READ THIS!!! This function determines what type of response packet is sent
     * based on the opCode and then calls the functionality on that object once it
     * is dynamically casted.
     * 
     * @param response
     */
    protected void handleResponseFromServer(IRC_Packet response) {
        switch (response.getPacketHeader().getOpCode()) {
            case OP_CODE_ERR:
                ErrorPacket errorPacket = (ErrorPacket) response;
                this.handleErrorResponseFromServer(errorPacket);
               break; 
            case OP_CODE_KEEP_ALIVE:
                break;
            case OP_CODE_HELLO:
                break;
            case OP_CODE_LIST_ROOMS_RESPONSE:
                break;
            case OP_CODE_LIST_USERS_RESP:
                break;
            case OP_CODE_JOIN_ROOM_RESP:
                break;
            case OP_CODE_LEAVE_ROOM_RESP:
                break;
        }
    }

    /**
     * This method is specifically for handling errors from the server
     */
    protected void handleErrorResponseFromServer(ErrorPacket errorPacket) {
        switch(errorPacket.getErrorCode()) {
            case IRC_ERR_ILLEGAL_OPCODE:
                System.err.println("Err: Unsupported Operation Requested... System exitting");
                break;
            case IRC_ERR_ILLEGAL_LENGTH:
                System.err.println("ERR: Illegal packet length (too long).");
                break;
            case IRC_ERR_NAME_EXISTS:
                System.err.println("ERR: Name already exists for user. Please try again using a different user name");
                break;
            case IRC_ERR_ILLEGAL_PROTOCOL:
                System.err.println("ERR: Illegal protocol for application user... System exiting");
                System.exit(1);
                break;
            default:
                System.err.println("ERR: System Exitting..");
                System.exit(1);
        }
    }

    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }

    protected boolean isErrPacket(IRC_Packet packet) { return packet.getPacketHeader().getOpCode() == OpCodes.OP_CODE_ERR; }
}