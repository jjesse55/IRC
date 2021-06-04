package code.Server;

import java.io.Serializable;

public class User implements Serializable {

    private final int PORT;
    private final String USERNAME;

    public static final String CLIENT_HOST = "localhost";

    public User(String username, int port) {
        this.USERNAME = username;
        this.PORT = port;
    }

    public int getPortNumber() { return this.PORT; }
    public String getUsername() { return this.USERNAME; }
}