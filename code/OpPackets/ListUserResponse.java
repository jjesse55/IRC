package code.OpPackets;

import code.Codes.OpCodes;

public class ListUserResponse extends OpPackets {


    /**
     * This is the response to listing all users 
     * @param opCode
     */
    public ListUserResponse(OpCodes opCode)
    {
        super(OpCodes.OP_CODE_LIST_USERS_RESP);
    }
    
}