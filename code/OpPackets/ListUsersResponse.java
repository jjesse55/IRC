package code.OpPackets;

import java.util.ArrayList;
import code.Codes.OpCodes;

public class ListUsersResponse extends OpPackets {

    private final ArrayList<String> USERS;

    /**
     * Response to list all users
     */
    public ListUsersResponse(ArrayList<String> users) {
        super(OpCodes.OP_CODE_LIST_USERS_RESP);
        this.USERS = users;
    }

    public ArrayList<String> getUsers(){
        return this.USERS;
    }
}