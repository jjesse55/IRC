package code.ErrorPackets;

import code.Codes.ErrorCodes;
import code.Codes.OpCodes;


/**
 * Error packet when a client logs on and tries to
 * register as a name that is already in use.
 */
public class NameExists extends ErrorPacket {

    public NameExists() {
        super(OpCodes.OP_CODE_ERROR, ErrorCodes.IRC_ERROR_NAME_EXISTS);
    }
}