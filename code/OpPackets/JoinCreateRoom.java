package code.OpPackets;

import code.Codes.OpCodes;

public class JoinCreateRoom extends OpPackets{
    String chatRoom;

    /**
     * This is the request to join/create a room if the room is not already created
     * @param opCode
     */
    public JoinCreateRoom(String room){
        super(OpCodes.OP_CODE_JOIN_ROOM);
        this.chatRoom=room;
    }

    
}