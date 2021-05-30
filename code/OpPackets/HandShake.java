package code.OpPackets;

import code.Codes.OpCodes;
import code.Server.User;

public class HandShake extends OpPackets {
    private final User USER;
    private final int PROTOCOL = 0x12345678;

    /**
     * First message sent to the server to establish connection
     * @param user
     */
    public HandShake(User user){
        super(OpCodes.OP_CODE_HELLO);
        this.USER = user;
    }

    public User getUser() { return this.USER; }
    public int getProtocol() { return this.PROTOCOL; }
}