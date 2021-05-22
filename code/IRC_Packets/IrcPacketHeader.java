package code.IRC_Packets;

import java.io.Serializable;
import code.Codes.OpCodes;

/**
 * Header for class IRC_Packets
 * 
 * Similar to headers that are transimitted with packets and HTTP requests
 */
public class IrcPacketHeader implements Serializable {
    
    //OpCode for the header of the IrcPacket
    protected OpCodes opCode;

    public IrcPacketHeader(OpCodes opCode) { this.opCode = opCode; }

    /**
     * @return the opCode
     */
    public OpCodes getOpCode() {
        return opCode;
    }
}