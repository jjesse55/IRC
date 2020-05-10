package code.OpPackets;

import code.Codes.OpCodes;

public class ListingResponse extends OpPackets{
    String roomNameUserBelongs;
    String itemNames;

    /**
     *  This is the response to listing all rooms 
     * @param opCode
     */
    public ListingResponse(OpCodes opCode){
        super(OpCodes.OP_CODE_LIST_ROOMS_RESPONSE);
    }
    
}