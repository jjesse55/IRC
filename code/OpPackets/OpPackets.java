package code.OpPackets;
import code.Codes.OpCodes;
import code.IRC_Packets.IRC_Packet;

public class OpPackets extends IRC_Packet{
    
    //Error Code for Specific Op Packets 
    int errorCode;

    /**
     * 
     * @param opCode
     */ 
    public OpPackets(OpCodes opCode)
    {
        super(opCode);
    }
}