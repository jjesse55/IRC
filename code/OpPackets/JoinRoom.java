package code.OpPackets;

import code.Codes.OpCodes;

public class JoinRoom extends OpPackets{
    String chatRoom;

    /**
     * This is the request to join/create a room if the room is not already created
     * @param opCode
     */
    public JoinRoom(String room){
        super(OpCodes.OP_CODE_JOIN_ROOM);
        this.chatRoom=room;
    }

    
}