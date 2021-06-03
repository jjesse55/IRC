package code.ErrorPackets;

import code.Codes.ErrorCodes;
import code.Codes.OpCodes;

public class UnknownError extends ErrorPacket{
    
    public UnknownError() {
        super(OpCodes.OP_CODE_ERROR, ErrorCodes.IRC_ERROR_UNKNOWN);
    }
}