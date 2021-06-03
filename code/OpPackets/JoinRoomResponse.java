package code.OpPackets;

import code.Codes.OpCodes;

public class JoinRoomResponse extends OpPackets {
    
    public JoinRoomResponse() {
        super(OpCodes.OP_CODE_JOIN_ROOM_RESPONSE);
    }
}