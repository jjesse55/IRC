package code.OpPackets;

import code.Codes.OpCodes;


/**
 * IRC packet send to server requesting to get a list of all current rooms
 */
public class ListRooms extends OpPackets{
   
    /**
     * This is the request to listing all rooms 
     * @param opCode
     */
    public ListRooms(){
        super(OpCodes.OP_CODE_LIST_ROOMS);
    }
}