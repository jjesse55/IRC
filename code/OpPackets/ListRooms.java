package code.OpPackets;

import code.Codes.OpCodes;

public class ListRooms extends OpPackets{
   
    /**
     * This is the request to listing all rooms 
     * @param opCode
     */
    public ListRooms(){
        super(OpCodes.OP_CODE_LIST_ROOMS);
    }

    
}