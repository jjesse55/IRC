package code.ErrorPackets;
import code.Codes.OpCodes;
import code.IRC_Packets.IRC_Packet;

public class ErrorPacket extends IRC_Packet{
    
   
    public ErrorPacket(OpCodes opCode){
        super(opCode);

    }
}


