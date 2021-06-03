package code.OpPackets;

import code.Codes.OpCodes;

public class GoodBye extends OpPackets {

    private final String USER_NAME;

    public GoodBye(String userName){
        super(OpCodes.OP_CODE_GOODBYE);
        this.USER_NAME=userName;
    }

    public String getUsername() { return this.USER_NAME; }
}