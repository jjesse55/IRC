package code.OpPackets;
import code.Codes.OpCodes;

public class TellPrivateMessages extends OpPackets {

    /**
     * Server sending private message from client to client 
     */
    public TellPrivateMessages()
    {
        super(OpCodes.OP_CODE_TELL_PRIVATE_MESSAGE);
    }
}