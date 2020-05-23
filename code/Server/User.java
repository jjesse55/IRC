package code.Server;

/**
 * This class is how the client will keep track of all the current users
 * 
 * All objects will have the username of the user, ip address, and port #
 */
public class User {

    private int port;
    private String username;

    //Host that all clients will use
    private static final String CLIENT_HOST = "localhost";

    public User(String username, int port) {
        this.username = username;
        this.port = port;
    }


    //Getters
    public String getClientHost() { return CLIENT_HOST; }
    public int getPortNumber() { return this.port; }
    public String getUsername() { return this.username; }
}