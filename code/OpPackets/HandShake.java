package code.OpPackets;

import code.Codes.OpCodes;
import code.Server.User;

public class HandShake extends OpPackets {
    private User user;
    private int protocol = 0x12345678;

    /**
     * This is the first message sent to the server to establish connection
     * @param opCode
     */
    public HandShake(User user){
        super(OpCodes.OP_CODE_HELLO);
        this.user = user;
    }

    public User getUser() { return this.user; }
    public int getProtocol() { return this.protocol; }
}