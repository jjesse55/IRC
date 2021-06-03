package code.ErrorPackets;

import code.Codes.ErrorCodes;
import code.Codes.OpCodes;

public class NameExists extends ErrorPacket {

    public NameExists() {
        super(OpCodes.OP_CODE_ERROR, ErrorCodes.IRC_ERROR_NAME_EXISTS);
    }
}