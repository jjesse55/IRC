package code.OpPackets;

import code.Codes.OpCodes;

public class LeaveRoom extends OpPackets {
    String chatRoom;

    /**
     * 
     * Request to leave room
     * @param opCode
     */
    public LeaveRoom(OpCodes opCode)
    {
        super(OpCodes.OP_CODE_LEAVE_ROOM);
    }
    
}