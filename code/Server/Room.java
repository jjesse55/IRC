package code.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import code.OpPackets.SendMessage;


/**
 * This is the SERVER's room class (seperate from a client room which is in
 * ChatRoom.java)
 */
public class Room extends Thread {

    // Class fields
    private String roomName;
    private SendMessage messageToFwd;
    private ArrayList<User> users = new ArrayList<>();


    // Class methods
    public Room(String roomName) {
        this.roomName = roomName;
    }


    public void run() {
        System.out.println("LOG: Sending msg: " + this.messageToFwd.getMessage()
        + " to all the users in the room: " + this.getRoomName());

        for (User user : this.users) {
            try {
                Socket socket = new Socket(user.getClientHost(), user.getPortNumber());
                ObjectOutputStream outToRoom = new ObjectOutputStream(socket.getOutputStream());

                outToRoom.writeObject(this.messageToFwd);

                ObjectInputStream inFromRoom = new ObjectInputStream(socket.getInputStream());

                inFromRoom.readObject();

                inFromRoom.close();
                socket.close();
            } catch (ClassNotFoundException exception) {
                System.out.println("ERR: Class Not Found");
            } catch (Exception exception) {
                System.out.println("ERR: Error sending message to user: " + user.getUsername());
            }
        }
    }


    public void addUser(User user) {
        this.users.add(user);
    }

    /**
     * Remove a user from a room either when the server/client disconnects from each
     * other or when the client requests to leave a room
     */
    public void removeUser(String userToRemove) {
        for (User user : this.users) {
            if (user.getUsername().equalsIgnoreCase(userToRemove)) {
                this.users.remove(user);
                return;
            }
        }
    }

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
        for (User user : this.users) {
            if (user.getUsername().equalsIgnoreCase(username))
                return true;
        }

        return false;
    }

    public void setMessageToForward(SendMessage msg) {
        this.messageToFwd = msg;
    }

    public boolean isEmpty() {
        return this.users.isEmpty();
    }

    public String getRoomName() {
        return this.roomName;
    }
}