package code.OpPackets;

import code.Codes.OpCodes;

public class HandShake extends OpPackets{
    String targetName;
    String messageToSend;

    /**
     * This is the first message sent to the server to establish connection
     * @param opCode
     */
    public HandShake(OpCodes opCode){
        super(OpCodes.OP_CODE_HELLO);
    }
    
}