package code.ErrorPackets;

import code.Codes.ErrorCodes;
import code.Codes.OpCodes;

public class IllegalProtocol extends ErrorPacket {

    public IllegalProtocol() {
        super(OpCodes.OP_CODE_ERR, ErrorCodes.IRC_ERR_ILLEGAL_PROTOCOL)
    }
}