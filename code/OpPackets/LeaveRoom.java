package code.OpPackets;

import code.Codes.OpCodes;

public class LeaveRoom extends OpPackets {
    private final String CHAT_ROOM;
    private final String USER_NAME;

    public LeaveRoom(String room, String userName)
    {
        super(OpCodes.OP_CODE_LEAVE_ROOM);
        this.CHAT_ROOM=room;
        this.USER_NAME= userName;
    }
    
    public String getUsername() { return this.USER_NAME; }
    public String getRoomName() { return this.CHAT_ROOM; }
}