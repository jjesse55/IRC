package code.OpPackets;

import java.util.ArrayList;

import java.io.Serializable;
import code.Codes.OpCodes;

public class ListRoomsResp extends OpPackets{
    
    private ArrayList<String> rooms;

    /**
     *  This is the response to listing all rooms 
     * @param opCode
     */
    public ListRoomsResp (ArrayList<String> rooms) {
        super(OpCodes.OP_CODE_LIST_ROOMS_RESPONSE);
        this.rooms = rooms;
    }

    public ArrayList<String> getRooms(){
        return this.rooms;
    }
    
}