package code.Server;

import java.util.ArrayList;


/**
 * This is the SERVER class for a room (seperate from a client room)
 * 
 * TODO - Joseph still needs to make the server able to work with rooms
 */
public class Room extends Thread {
    ArrayList <String> users; //change this
    String RoomName;

    public Room(String name){
        RoomName=name;
        users= new ArrayList <>();
    }

    public void addUser(String user){

    }

    public void removeUser(String user){

    }
}