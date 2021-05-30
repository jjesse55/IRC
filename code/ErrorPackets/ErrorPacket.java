package code.ErrorPackets;

import code.Codes.ErrorCodes;
import code.Codes.OpCodes;
import code.IRC_Packets.IrcPacket;


/**
 * This is the base class for all error packets.
 */
public abstract class ErrorPacket extends IrcPacket {
    
    //Error Code for Specific Op Packets 
    protected final ErrorCodes ERROR_CODE;
   
    public ErrorPacket(OpCodes opCode, ErrorCodes errorCode){
        super(opCode);
        this.ERROR_CODE = errorCode;
    }

    public ErrorCodes getErrorCode() { return this.ERROR_CODE; }
}