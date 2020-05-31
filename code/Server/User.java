package code.Server;

import java.io.Serializable;

/**
 * This class is how the client will keep track of all the current users
 * 
 * All objects will have the username of the user, ip address, and port #
 */
public class User implements Serializable {

    //class fields
    private int port;
    private String username;

    //Host that all clients will use
    private static final String CLIENT_HOST = "localhost";


    //Class methods
    public User(String username, int port) {
        this.username = username;
        this.port = port;
    }

    //Getters
    public String getClientHost() { return CLIENT_HOST; }
    public int getPortNumber() { return this.port; }
    public String getUsername() { return this.username; }
}