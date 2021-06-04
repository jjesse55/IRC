package code.Client;

import java.net.ServerSocket;
import java.io.*;
import java.net.Socket;
import code.ErrorPackets.IllegalOpcode;
import code.IRC_Packets.IrcPacket;
import code.OpPackets.GoodBye;
import code.OpPackets.KeepAlive;

public class ServerAlive extends Thread {

    private ServerSocket listeningSocket;


    public ServerAlive() {
        try {
            this.listeningSocket = new ServerSocket(0);
        } catch (Exception exception) {
            System.err.println("Could not establish keep alive connection. System exiting...");
            System.exit(1);
        }
    }

    public void run() {
        while (true) {
            try {
                Socket aliveConnection = this.listeningSocket.accept();
                ObjectInputStream inFromServer = new ObjectInputStream(aliveConnection.getInputStream());
                IrcPacket serverPacket = (IrcPacket) inFromServer.readObject();
                ObjectOutputStream outToServer = new ObjectOutputStream(aliveConnection.getOutputStream());
                outToServer.writeObject(this.handleRequestFromClient(serverPacket));
                aliveConnection.close();
            } catch (Exception exception) {
                System.err.println("ERR: Exception raised receiving message from the server.");
            }
        }
    }


    private IrcPacket handleRequestFromClient(IrcPacket request) {
        switch (request.getPacketHeader().getOpCode()) {
            case OP_CODE_GOODBYE:
                return new GoodBye(null);
            case OP_CODE_KEEP_ALIVE:
                return new KeepAlive();
            default:
                return new IllegalOpcode();
        }
    }

    public int getPortNumber() {
        return this.listeningSocket.getLocalPort();
    }
}