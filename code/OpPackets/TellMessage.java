package code.OpPackets;

import code.Codes.OpCodes;

public class TellMessage extends OpPackets{

    /**
     * 
     * @param opCode
     */
    public TellMessage(OpCodes opCode)
    {
        super(opCode);
    }
    
}