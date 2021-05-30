package code.OpPackets;

import code.Codes.OpCodes;

public class KeepAlive extends OpPackets {

    /**
     * Pings the server to make sure the connection is still alive
     */
    public KeepAlive(){
        super(OpCodes.OP_CODE_KEEP_ALIVE);
    }
}