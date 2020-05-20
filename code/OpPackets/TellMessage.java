package code.OpPackets;

import code.Codes.OpCodes;

public class TellMessage extends OpPackets{

    /**
     * server sending message to clients 
     * @param opCode
     */
    public TellMessage()
    {
        super(OpCodes.OP_CODE_TELL_MESSAGE);
    }
    
}