package code.OpPackets;

import code.Codes.OpCodes;

public class SendMessageResponse extends OpPackets{

    public SendMessageResponse()
    {
        super(OpCodes.OP_CODE_SEND_MESSAGE_RESPONSE);
    } 

}