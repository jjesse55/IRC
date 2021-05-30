package code.OpPackets;

import code.Codes.OpCodes;

public class SendMessage extends OpPackets {
    private final String MSG_TO_ROOM;
    private final String USERNAME;
    private final String ROOM_NAME;

    /**
     * Request to send a message
     */
    public SendMessage(String msg, String un, String roomName){
        super(OpCodes.OP_CODE_SEND_MESSAGE);
        this.MSG_TO_ROOM=msg;
        this.USERNAME=un;
        this.ROOM_NAME = roomName;
    }

    public String getMessage(){ return this.MSG_TO_ROOM;}
    public String getUserName(){ return this.USERNAME;}
    public String getRoomName() { return this.ROOM_NAME; }
}