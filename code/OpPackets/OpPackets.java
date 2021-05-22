package code.OpPackets;
import code.Codes.OpCodes;
import code.IRC_Packets.IrcPacket;

public class OpPackets extends IrcPacket {

    /**
     * @param opCode
     */ 
    public OpPackets(OpCodes opCode)
    {
        super(opCode);
    }
}