package code.OpPackets;

import code.Codes.OpCodes;

/**
 * IRC packet for response sent from server to leave a room
 */
public class LeaveRoomResp extends OpPackets {

    public LeaveRoomResp() {
        super(OpCodes.OP_CODE_LEAVE_ROOM_RESP);
    }
}