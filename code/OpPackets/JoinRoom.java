package code.OpPackets;

import code.Codes.OpCodes;

public class JoinRoom extends OpPackets{
    private String chatRoom;
    private String userName;
    private int portNumber;

    /**
     * This is the request to join/create a room if the room is not already created
     * @param opCode
     */
    public JoinRoom(String room, String  userName, int pN){
        super(OpCodes.OP_CODE_JOIN_ROOM);
        this.chatRoom=room;
        this.userName= userName;
        this.portNumber=pN;
    }


    public String getRoomName() { return this.chatRoom; }
    public String getUsername() { return this.userName; }
    public int getPortNumber() { return this.portNumber; }
}