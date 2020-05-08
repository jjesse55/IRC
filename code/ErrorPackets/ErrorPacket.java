package code.ErrorPackets;

import code.Codes.ErrorCodes;
import code.Codes.OpCodes;
import code.IRC_Packets.IRC_Packet;


/**
 * This is the base class for all error packets.
 * 
 * Extends the generic IRC packet class
 */
public abstract class ErrorPacket extends IRC_Packet{
    
    //Error Code for Specific Op Packets 
    protected ErrorCodes errorCode;
   
    public ErrorPacket(OpCodes opCode, ErrorCodes errorCode){
        super(opCode);
        this.errorCode = errorCode;
    }
}