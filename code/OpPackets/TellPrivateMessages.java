package code.OpPackets;
import code.Codes.OpCodes;

public class TellPrivateMessages extends OpPackets {

    /**
     * Server sending private message from client to client 
     * @param opCode
     */

    public TellPrivateMessages(OpCodes opCode)
    {
        super(OpCodes.OP_CODE_TELL_PRIVATE_MESSAGE);
    }
    
}