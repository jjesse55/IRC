package code.OpPackets;

import java.io.Serializable;

import code.Codes.OpCodes;

public class HandShake extends OpPackets {
    private String userName;
    private int protocol = 0x12345678;

    /**
     * This is the first message sent to the server to establish connection
     * @param opCode
     */
    public HandShake(String userName){
        super(OpCodes.OP_CODE_HELLO);
        this.userName = userName;
    }

    public String getUserName() { return this.userName; }
    public int getProtocol() { return this.protocol; }
}