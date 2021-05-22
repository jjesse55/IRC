package code.OpPackets;

import java.util.ArrayList;
import code.Codes.OpCodes;

public class ListUsersResponse extends OpPackets {

    private ArrayList<String> users;

    /**
     * This is the response to listing all users
     */
    public ListUsersResponse(ArrayList<String> users) {
        super(OpCodes.OP_CODE_LIST_USERS_RESP);
        this.users = users;
    }

    public ArrayList<String> getUsers(){
        return this.users;
    }
}