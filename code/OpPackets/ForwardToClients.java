package code.OpPackets;

import code.Codes.OpCodes;

public class ForwardToClients extends OpPackets {
    String targetName;
    String nameOfUser;
    String msgtoRoom;

    /**
     * 
     * @param opCode
     */
    public ForwardToClients(OpCodes opCode){
        super(opCode);
    }
    
}