package code.OpPackets;

import code.Codes.OpCodes;

public class KeepAlive extends OpPackets {

    /**
     * This is the "ping" to the server to make sure the connection is still alive
     * @param opCode
     */
    public KeepAlive(OpCodes opCode){
        super(OpCodes.OP_CODE_KEEP_ALIVE);
    }
    
}