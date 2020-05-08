package code.OpPackets;

import code.Codes.OpCodes;

public class ListingResponse extends OpPackets{
    String roomNameUserBelongs;
    String itemNames;

    public ListingResponse(OpCodes opCode){
        super(opCode);
    }
    
}