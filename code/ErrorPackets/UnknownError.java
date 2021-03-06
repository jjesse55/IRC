package code.ErrorPackets;

import code.Codes.ErrorCodes;
import code.Codes.OpCodes;


/**
 * This class represents an unknown error that was encountered by the server
 */
public class UnknownError extends ErrorPacket{
    
    public UnknownError() {
        super(OpCodes.OP_CODE_ERR, ErrorCodes.IRC_ERR_UNKNOWN);
    }
}