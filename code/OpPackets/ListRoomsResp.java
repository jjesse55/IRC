package code.OpPackets;

import java.util.ArrayList;
import code.Codes.OpCodes;

public class ListRoomsResponse extends OpPackets{
    
    private final ArrayList<String> ROOMS;

    public ListRoomsResponse (ArrayList<String> rooms) {
        super(OpCodes.OP_CODE_LIST_ROOMS_RESPONSE);
        this.ROOMS = rooms;
    }

    public ArrayList<String> getRooms(){
        return this.ROOMS;
    }
    
}