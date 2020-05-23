package code.OpPackets;

import code.Codes.OpCodes;

public class SendMessageResp extends OpPackets{


    /**
     * server sending message to clients 
     * @param opCode
     */
    public SendMessageResp()
    {
        super(OpCodes.OP_CODE_SEND_MESSAGE_RESPONSE);
    } 

}