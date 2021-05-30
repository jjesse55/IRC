package code.ErrorPackets;

import code.Codes.ErrorCodes;
import code.Codes.OpCodes;
import code.IRC_Packets.IrcPacket;

public abstract class ErrorPacket extends IrcPacket {
    
    protected final ErrorCodes ERROR_CODE;
   
    public ErrorPacket(OpCodes opCode, ErrorCodes errorCode){
        super(opCode);
        this.ERROR_CODE = errorCode;
    }

    public ErrorCodes getErrorCode() { return this.ERROR_CODE; }
}