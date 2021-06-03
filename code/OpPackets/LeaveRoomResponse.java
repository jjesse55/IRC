package code.OpPackets;

import code.Codes.OpCodes;
public class LeaveRoomResponse extends OpPackets {

    public LeaveRoomResponse() {
        super(OpCodes.OP_CODE_LEAVE_ROOM_RESPONSE);
    }
}