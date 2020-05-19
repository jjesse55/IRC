package code.OpPackets;

import code.Codes.OpCodes;

public class LeaveRoom extends OpPackets {
    String chatRoom;

    /**
     * 
     * Request to leave room
     * @param opCode
     */
    public LeaveRoom(String room)
    {
        super(OpCodes.OP_CODE_LEAVE_ROOM);
        this.chatRoom=room;
    }
    
}