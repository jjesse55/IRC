package code.OpPackets;

import code.Codes.OpCodes;

public class LeaveRoom extends OpPackets {
    String chatRoom;

    /**
     * 
     * @param opCode
     */
    public LeaveRoom(OpCodes opCode)
    {
        super(opCode);
    }
    
}