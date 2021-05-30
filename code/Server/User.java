package code.Server;

import java.io.Serializable;

/**
 * This class is how the client will keep track of all the current users
 */
public class User implements Serializable {

    //class fields
    private final int PORT;
    private final String USERNAME;

    //Host that all clients will use
    private static final String CLIENT_HOST = "localhost";


    //Class methods
    public User(String username, int port) {
        this.USERNAME = username;
        this.PORT = port;
    }

    //Getters
    public String getClientHost() { return CLIENT_HOST; }
    public int getPortNumber() { return this.PORT; }
    public String getUsername() { return this.USERNAME; }
}