package code.ErrorPackets;

import code.Codes.ErrorCodes;
import code.Codes.OpCodes;

public class IllegalOpcode extends ErrorPacket {

    public IllegalOpcode() {
        super(OpCodes.OP_CODE_ERROR, ErrorCodes.IRC_ERROR_ILLEGAL_OPCODE);
    }
}