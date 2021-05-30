package code.OpPackets;

import code.Codes.OpCodes;

public class JoinRoom extends OpPackets{
    private final String CHAT_ROOM;
    private final String USER_NAME;
    private final int PORT_NUMBER;

    /**
     * Request to join/create a room if the room is not already created
     */
    public JoinRoom(String room, String userName, int pN){
        super(OpCodes.OP_CODE_JOIN_ROOM);
        this.CHAT_ROOM=room;
        this.USER_NAME= userName;
        this.PORT_NUMBER=pN;
    }


    public String getRoomName() { return this.CHAT_ROOM; }
    public String getUsername() { return this.USER_NAME; }
    public int getPortNumber() { return this.PORT_NUMBER; }
}