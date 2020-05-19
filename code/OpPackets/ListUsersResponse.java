package code.OpPackets;

import code.Codes.OpCodes;

public class ListUsersResponse extends OpPackets {


    /**
     * This is the response to listing all users 
     * @param opCode
     */
    public ListUsersResponse()
    {
        super(OpCodes.OP_CODE_LIST_USERS_RESP);
    }
    
}