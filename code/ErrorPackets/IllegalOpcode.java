package code.ErrorPackets;

import code.Codes.ErrorCodes;
import code.Codes.OpCodes;


/**
 * This is an error IRC Packet for when the clients has requested an operation that
 * is not defined by a legal opCode
 */
public class IllegalOpcode extends ErrorPacket {

    public IllegalOpcode(OpCodes opCode) {
        super(opCode, ErrorCodes.IRC_ERR_ILLEGAL_OPCODE);
    }
}