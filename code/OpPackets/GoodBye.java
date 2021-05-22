package code.OpPackets;

import code.Codes.OpCodes;

public class GoodBye extends OpPackets {

    String UserName;

      /**
     * This is the request to join/create a room if the room is not already created
     * @param opCode
     */
    public GoodBye(String userName){
        super(OpCodes.OP_CODE_GOODBYE);
        this.UserName=userName;
    }

    public String getUsername() { return this.UserName; }
}