package code.OpPackets;

import code.Codes.OpCodes;


/**
 * IRC packet send to server requesting to get a list of current rooms
 */
public class ListRooms extends OpPackets{

    /**
     * Request to list all rooms 
     */
    public ListRooms(){
        super(OpCodes.OP_CODE_LIST_ROOMS);
    }
}