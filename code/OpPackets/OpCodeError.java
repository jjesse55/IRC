package code.OpPackets;

import code.Codes.OpCodes;

public class OpCodeError extends OpPackets {
   
    /**
     * 
     * @param opCode
     */
    public OpCodeError(OpCodes opCode){
        
        super(OpCodes.OP_CODE_ERR);
    }
    
}