package code.OpPackets;

import code.Codes.OpCodes;

public class SendPrivateMessages extends OpPackets{

    /**
     * Request to send private message between clients
     * @param opCode
     */
    public SendPrivateMessages()
    {
        super(OpCodes.OP_CODE_SEND_PRIVATE_MESSAGE);
    }
    
}