package code.Server;

/**
 * This class is how the client will keep track of all the current users
 * 
 * All objects will have the username of the user, ip address, and port #
 */
public class User {

    private String username;
    private String ipAddress;
    private int port;

    public User(String username, String ipAddress, int port) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.port = port;
    }
}