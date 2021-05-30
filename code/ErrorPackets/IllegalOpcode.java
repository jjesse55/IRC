package code.ErrorPackets;

import code.Codes.ErrorCodes;
import code.Codes.OpCodes;


/**
 * This is an error IRC Packet when clients request an operation that
 * is not defined by a legal opCode
 */
public class IllegalOpcode extends ErrorPacket {

    public IllegalOpcode() {
        super(OpCodes.OP_CODE_ERR, ErrorCodes.IRC_ERR_ILLEGAL_OPCODE);
    }
}