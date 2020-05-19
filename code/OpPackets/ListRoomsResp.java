package code.OpPackets;

import code.Codes.OpCodes;

public class ListRoomsResp extends OpPackets{
    String roomNameUserBelongs;
    String itemNames;

    /**
     *  This is the response to listing all rooms 
     * @param opCode
     */
    public ListRoomsResp (String User, String item){
        super(OpCodes.OP_CODE_LIST_ROOMS_RESPONSE);
        this.roomNameUserBelongs=User;
        this.itemNames=item;
    }
    
}