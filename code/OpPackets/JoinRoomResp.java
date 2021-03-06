package code.OpPackets;

import code.Codes.OpCodes;

/**
 * This is the response from the server for a request to join a room
 */
public class JoinRoomResp extends OpPackets {
    
    public JoinRoomResp() {
        super(OpCodes.OP_CODE_JOIN_ROOM_RESP);
    }
}