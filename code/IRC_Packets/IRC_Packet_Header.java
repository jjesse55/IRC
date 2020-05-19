package code.IRC_Packets;

import java.io.Serializable;

import code.Codes.OpCodes;

/**
 * Header for class IRC_Packets
 * 
 * Similar to headers that are transimitted with packets and HTTP requests
 */
public class IRC_Packet_Header implements Serializable {
    
    //OpCode for the header of the IRC_Packet
    protected OpCodes opCode;

    public IRC_Packet_Header(OpCodes opCode) { this.opCode = opCode; }

    /**
     * @return the opCode
     */
    public OpCodes getOpCode() {
        return opCode;
    }
}