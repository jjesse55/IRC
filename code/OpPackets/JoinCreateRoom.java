package code.OpPackets;

import code.Codes.OpCodes;

public class JoinCreateRoom extends OpPackets{
    String chatRoom;

    public JoinCreateRoom(OpCodes opCode){
        super(opCode);
    }

    
}