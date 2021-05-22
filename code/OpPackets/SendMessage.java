package code.OpPackets;

import code.Codes.OpCodes;

public class SendMessage extends OpPackets {
    private String msgToRoom;
    private String userName;
    private String roomName;

    /**
     * This is the request to send a message
     */
    public SendMessage(String msg, String un, String roomName){
        super(OpCodes.OP_CODE_SEND_MESSAGE);
        this.msgToRoom=msg;
        this.userName=un;
        this.roomName = roomName;
    }

    public String getMessage(){ return this.msgToRoom;}
    public String getUserName(){ return this.userName;}
    public String getRoomName() { return this.roomName; }
}