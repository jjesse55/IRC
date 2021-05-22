package code.ErrorPackets;
import code.Codes.*;

public class InvalidRoomName extends ErrorPacket {

    public InvalidRoomName() { super(OpCodes.OP_CODE_ERR, ErrorCodes.IRC_ERR_INVALID_ROOM_NAME);}
    
}