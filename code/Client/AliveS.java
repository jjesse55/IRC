package code.Client;

import java.net.ServerSocket;
import java.io.*;
import java.net.Socket;
import code.ErrorPackets.IllegalOpcode;
import code.IRC_Packets.IRC_Packet;
import code.OpPackets.GoodBye;
import code.OpPackets.KeepAlive;


public class AliveS extends Thread {

    //Class fields
    private ServerSocket listeningSocket;


    //Class methods
    public AliveS() {
        try {
            this.listeningSocket = new ServerSocket(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not establish keep alive connection. System exiting...");
            System.exit(1);
        }
    }

    public void run() {
        while (true) {
            try {
                Socket aliveConnection = this.listeningSocket.accept();
                ObjectInputStream inFromServer = new ObjectInputStream(aliveConnection.getInputStream());
                IRC_Packet serverPacket = (IRC_Packet) inFromServer.readObject();

                ObjectOutputStream outToServer = new ObjectOutputStream(aliveConnection.getOutputStream());
                outToServer.writeObject(this.handleRequestFromClient(serverPacket));
                aliveConnection.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("ERR: Exception raised receiving message from the server.");
            }
        }
    }


    private IRC_Packet handleRequestFromClient(IRC_Packet request) {
        switch (request.getPacketHeader().getOpCode()) {

            case OP_CODE_GOODBYE:
                GoodBye Gbye = (GoodBye) request;
                return new GoodBye(null);
                
            case OP_CODE_KEEP_ALIVE:
                KeepAlive aliveK = (KeepAlive) request;
                return new KeepAlive();

            default:
                return new IllegalOpcode();
        }
    }

    public int getPortNumber() {
        return this.listeningSocket.getLocalPort();
    }
}