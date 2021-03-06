package code.ErrorPackets;

import code.Codes.ErrorCodes;
import code.Codes.OpCodes;


/**
 * This is the error IRC packet for when a user tries to use a name or
 * send a message that is too long in length
 */
public class IllegalLength extends ErrorPacket {

    public IllegalLength() {
        super(OpCodes.OP_CODE_ERR, ErrorCodes.IRC_ERR_ILLEGAL_LENGTH);
    }
}