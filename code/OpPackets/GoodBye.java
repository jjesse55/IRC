package code.OpPackets;

import code.Codes.OpCodes;

public class GoodBye extends OpPackets {

    private final String USER_NAME;

      /**
     * This is the request to join/create a room if the room is not already created
     * @param userName
     */
    public GoodBye(String userName){
        super(OpCodes.OP_CODE_GOODBYE);
        this.USER_NAME=userName;
    }

    public String getUsername() { return this.USER_NAME; }
}