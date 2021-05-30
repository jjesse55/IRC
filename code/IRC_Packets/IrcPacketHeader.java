package code.IRC_Packets;

import java.io.Serializable;
import code.Codes.OpCodes;

/**
 * Similar to headers that are transimitted with packets and HTTP requests
 */
public class IrcPacketHeader implements Serializable {
    
    //OpCode for the header of the IrcPacket
    protected final OpCodes OP_CODE;

    public IrcPacketHeader(OpCodes opCode) { this.OP_CODE = opCode; }

    /**
     * @return the OP_CODE
     */
    public OpCodes getOpCode() {
        return this.OP_CODE;
    }
}