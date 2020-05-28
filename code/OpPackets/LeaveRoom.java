package code.OpPackets;

import code.Codes.OpCodes;

public class LeaveRoom extends OpPackets {
    String chatRoom;
    String userName;

    /**
     * 
     * Request to leave room
     * @param opCode
     */
    public LeaveRoom(String room, String Usern)
    {
        super(OpCodes.OP_CODE_LEAVE_ROOM);
        this.chatRoom=room;
        this.userName= Usern;
    }
    
    public String getUsername() { return this.userName; }
    public String getRoomName() { return this.chatRoom; }
}