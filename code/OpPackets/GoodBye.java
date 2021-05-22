package code.OpPackets;

import code.Codes.OpCodes;

public class GoodBye extends OpPackets {

    String userName;

      /**
     * This is the request to join/create a room if the room is not already created
     * @param userName
     */
    public GoodBye(String userName){
        super(OpCodes.OP_CODE_GOODBYE);
        this.userName=userName;
    }

    public String getUsername() { return this.userName; }
}