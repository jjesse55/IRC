package code.OpPackets;

import code.Codes.OpCodes;

public class SendMessage extends OpPackets {
    String msgtoRoom;
    String userName;

    /**
     * This is the request to send a message
     * @param opCode
     */
    public SendMessage(String msg, String un){
        super(OpCodes.OP_CODE_SEND_MESSAGE);
        this.msgtoRoom=msg;
        this.userName=un;
    }

    public String getMessage(){ return this.msgtoRoom;}
    public String getUserName(){ return this.userName;}
}