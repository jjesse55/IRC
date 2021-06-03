package code.IRC_Packets;

import java.io.Serializable;
import code.Codes.OpCodes;

public class IrcPacketHeader implements Serializable {
    
    protected final OpCodes OP_CODE;

    public IrcPacketHeader(OpCodes opCode) { this.OP_CODE = opCode; }

    public OpCodes getOpCode() {
        return this.OP_CODE;
    }
}