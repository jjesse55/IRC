package code.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import code.OpPackets.SendMessage;
import code.OpPackets.SendMessageResp;

/**
 * This is the SERVER class for a room (seperate from a client room)
 * 
 * TODO - Joseph still needs to make the server able to work with rooms
 */
public class Room extends Thread {

    private String roomName;
    private ArrayList<User> users = new ArrayList<>(); // change this

    private SendMessage messageToFwd;


    public void run() {
        for (User user : this.users) {
            try {
                System.out.println("Sending the msg to all the users in the room");
                Socket socket = new Socket(user.getClientHost(), user.getPortNumber());
                ObjectOutputStream outToRoom = new ObjectOutputStream(socket.getOutputStream());

                outToRoom.writeObject(this.messageToFwd);

                ObjectInputStream inFromRoom = new ObjectInputStream(socket.getInputStream());

System.out.println("right before Room run() reads object from server");
                SendMessageResp resp = (SendMessageResp) inFromRoom.readObject();

System.out.println("right after Room run() reads object from server");
                inFromRoom.close();
                socket.close();
            } catch (IOException ex) {
                System.out.println("Err: IO Exception 43");
                System.exit(0);
            } catch (ClassNotFoundException exception) {
                System.out.println("ERR: Class Not Found");
            } catch (Exception exception) {
                System.out.println("ERR: exception");
            }
        }
    }


    public Room(String roomName) {
        this.roomName = roomName;
    }

    public void addUser(User user) { this.users.add(user); }

    /**
     * Remove a user from a room either when the server/client disconnects from each
     * other or when the client requests to leave a room
     */
    public void removeUser(User user) { this.users.remove(user); }

    /**
     * Gets the list of users for when a client requests to list all users in a room
     */
    public ArrayList<String> getUsers() {
        if (this.users.isEmpty())
            return null;

        ArrayList<String> allUsersInTheRoom = new ArrayList<>();
        for (User user : this.users)
            allUsersInTheRoom.add(user.getUsername());

        return allUsersInTheRoom;
    }

    /**
     * Checks to see if a user is in the current room
     */
    public boolean containsUser(String username) {
        return this.users.contains(username);
    }

    public void setMessageToForward(SendMessage msg) { this.messageToFwd = msg; }
}