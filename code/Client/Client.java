package code.Client;

import java.io.*;
import java.net.Socket;
import code.IRC_Packets.IRC_Packet;

public class Client {

    private Socket clientSocket;
    private static final int SERVER_PORT = 194; // Port number for the client process
    private static final String SERVER_HOST = "localhost";

    public static void main(String [] notUsed) throws Exception {
        Client client = new Client();

        ObjectInputStream inFromServer = new ObjectInputStream(client.getClientSocket().getInputStream());
        ObjectOutputStream outToServer = new ObjectOutputStream(client.getClientSocket().getOutputStream());

        /* TODO, test sending objects to and from the server (execute this loop)

        while(true) {
            //TODO, use the GUI to formulate a request object from the client that will populate the below object.
            IRC_Packet request = new IRC_Packet();
            outToServer.writeObject();    //This is the line that will send packets to the server
            IRC_Packet response = (IRC_Packet) inFromServer.readObject();
        }

        */
    }


    //Class methods
    public Client() throws Exception {
        this.clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
    }

    /**
     * Getter for the client socket
     * @return the clientSocket
     */
    public Socket getClientSocket() {
        return clientSocket;
    }

    /**
     * READ THIS!!!
     * This function determines what type of response packet is sent based on the opCode
     * and then calls the functionality on that object once it is dynamically casted.
     * @param response
     */
    public void handleResonseFromServer(IRC_Packet response) {
        switch(response.getPacketHeader().getOpCode()) {
            case OP_CODE_ERR:
                //Call the function for this specific object and so on...
                break;
            case OP_CODE_KEEP_ALIVE:
                break;
            case OP_CODE_HELLO:
                break;
            case OP_CODE_LIST_ROOMS:
                break;
            case OP_CODE_LIST_ROOMS_RESPONSE:
                break;
            case OP_CODE_LIST_USERS:
                break;
            case OP_CODE_LIST_USERS_RESP:
                break;
            case OP_CODE_JOIN_ROOM:
                break;
            case OP_CODE_JOIN_ROOM_RESP:
                break;
            case OP_CODE_LEAVE_ROOM:
                break;
            case OP_CODE_LEAVE_ROOM_RESP:
                break;
            case OP_CODE_SEND_MESSAGE:
                break;
            case OP_CODE_TELL_MESSAGE:
                break;
            case OP_CODE_SEND_PRIVATE_MESSAGE:
                break;
            case OP_CODE_TELL_PRIVATE_MESSAGE:
                break;
        }
    }
}