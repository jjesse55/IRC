package code.OpPackets;

import java.util.ArrayList;
import code.Codes.OpCodes;

public class ListUsersResponse extends OpPackets {

    private final ArrayList<String> USERS;

    public ListUsersResponse(ArrayList<String> users) {
        super(OpCodes.OP_CODE_LIST_USERS_RESPONSE);
        this.USERS = users;
    }

    public ArrayList<String> getUsers(){
        return this.USERS;
    }
}