package code.OpPackets;

import java.util.ArrayList;
import code.Codes.OpCodes;

public class ListRoomsResp extends OpPackets{
    
    private final ArrayList<String> ROOMS;

    /**
     *  Response to listing all rooms 
     */
    public ListRoomsResp (ArrayList<String> rooms) {
        super(OpCodes.OP_CODE_LIST_ROOMS_RESPONSE);
        this.ROOMS = rooms;
    }

    public ArrayList<String> getRooms(){
        return this.ROOMS;
    }
    
}