package code.ErrorPackets;
import code.Codes.*;

public class InvalidRoomName extends ErrorPacket {

    public InvalidRoomName() { super(OpCodes.OP_CODE_ERROR, ErrorCodes.IRC_ERROR_INVALID_ROOM_NAME);}
    
}