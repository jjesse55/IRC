package code.OpPackets;

import code.Codes.OpCodes;

public class ListUsers extends OpPackets {
    
    private final String ROOM_NAME;
    
    public ListUsers(String roomName) {
        super(OpCodes.OP_CODE_LIST_USERS);
        this.ROOM_NAME = roomName;
    }
    
    
    public String getRoomName() { return this.ROOM_NAME; }
}