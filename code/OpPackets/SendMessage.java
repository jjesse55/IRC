package code.OpPackets;

import code.Codes.OpCodes;

public class SendMessage extends OpPackets {
    private final String MESSAGE_TO_ROOM;
    private final String USERNAME;
    private final String ROOM_NAME;

    public SendMessage(String message, String userName, String roomName){
        super(OpCodes.OP_CODE_SEND_MESSAGE);
        this.MESSAGE_TO_ROOM=message;
        this.USERNAME=userName;
        this.ROOM_NAME = roomName;
    }

    public String getMessage(){ return this.MESSAGE_TO_ROOM;}
    public String getUserName(){ return this.USERNAME;}
    public String getRoomName() { return this.ROOM_NAME; }
}