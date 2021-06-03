package code.OpPackets;

import code.Codes.OpCodes;

public class ListRooms extends OpPackets{

    public ListRooms(){
        super(OpCodes.OP_CODE_LIST_ROOMS);
    }
}