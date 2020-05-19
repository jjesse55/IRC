package code.OpPackets;

import code.Codes.OpCodes;

/**
 * IRC packet send to server to list all the current users
 */
public class ListUsers extends OpPackets {
    
    public ListUsers() {
        super(OpCodes.OP_CODE_LIST_USERS);
    }
}