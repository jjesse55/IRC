package code.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import code.OpPackets.SendMessage;

public class Room extends Thread {

    private final String ROOM_NAME;
    private SendMessage messageToForward;
    private final ArrayList<User> USERS = new ArrayList<>();


    public Room(String roomName) {
        this.ROOM_NAME = roomName;
    }


    public void run() {
        System.out.println("LOG: Sending msg: " + this.messageToForward.getMessage()
        + " to all the users in the room: " + this.getRoomName());

        for (User user : this.USERS) {
            try {
                Socket socket = new Socket(user.getClientHost(), user.getPortNumber());
                ObjectOutputStream outToRoom = new ObjectOutputStream(socket.getOutputStream());

                outToRoom.writeObject(this.messageToForward);

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
        this.USERS.add(user);
    }

    public void removeUser(String userToRemove) {
        for (User user : this.USERS) {
            if (user.getUsername().equalsIgnoreCase(userToRemove)) {
                this.USERS.remove(user);
                return;
            }
        }
    }

    public ArrayList<String> getUsers() {
        if (this.USERS.isEmpty())
            return null;

        ArrayList<String> allUsersInTheRoom = new ArrayList<>();
        for (User user : this.USERS)
            allUsersInTheRoom.add(user.getUsername());

        return allUsersInTheRoom;
    }

    public boolean containsUser(String username) {
        for (User user : this.USERS) {
            if (user.getUsername().equalsIgnoreCase(username))
                return true;
        }

        return false;
    }

    public void setMessageToForward(SendMessage message) {
        this.messageToForward = message;
    }

    public boolean isEmpty() {
        return this.USERS.isEmpty();
    }

    public String getRoomName() {
        return this.ROOM_NAME;
    }
}