package code.ErrorPackets;

import code.Codes.ErrorCodes;
import code.Codes.OpCodes;


/**
 * This is an error packet for when the client logging on tries to
 * register as a name that is already in use.
 */
public class NameExists extends ErrorPacket {

    public NameExists() {
        super(OpCodes.OP_CODE_ERR, ErrorCodes.IRC_ERR_NAME_EXISTS);
    }
}