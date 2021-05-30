package code.ErrorPackets;

import code.Codes.ErrorCodes;
import code.Codes.OpCodes;

public class IllegalProtocol extends ErrorPacket {

    public IllegalProtocol() {
        super(OpCodes.OP_CODE_ERROR, ErrorCodes.IRC_ERROR_ILLEGAL_PROTOCOL);
    }
}