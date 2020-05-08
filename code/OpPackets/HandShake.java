package code.OpPackets;

import code.Codes.OpCodes;

public class HandShake extends OpPackets{
    String targetName;
    String messageToSend;

    public HandShake(OpCodes opCode){
        super(opCode);
    }
    
}