package code.OpPackets;

import code.Codes.OpCodes;

/**
 * IRC packet send to server to list all the current users
 */
public class ListUsers extends OpPackets {
    
    private final String ROOM_NAME;
    
    public ListUsers(String roomName) {
        super(OpCodes.OP_CODE_LIST_USERS);
        this.ROOM_NAME = roomName;
    }
    
    
    public String getRoomName() { return this.ROOM_NAME; }
}