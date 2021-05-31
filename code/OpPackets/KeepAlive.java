package code.OpPackets;

import code.Codes.OpCodes;

public class KeepAlive extends OpPackets {

    public KeepAlive(){
        super(OpCodes.OP_CODE_KEEP_ALIVE);
    }
}