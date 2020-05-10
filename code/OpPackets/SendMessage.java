package code.OpPackets;

import code.Codes.OpCodes;

public class SendMessage extends OpPackets {
    String targetName;
    String nameOfUser;
    String msgtoRoom;

    /**
     * This is the request to send a message
     * @param opCode
     */
    public SendMessage(OpCodes opCode){
        super(OpCodes.OP_CODE_SEND_MESSAGE);
    }
    
}