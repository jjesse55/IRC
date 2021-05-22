package code.IRC_Packets;

import java.io.Serializable;
import code.Codes.OpCodes;

public abstract class IrcPacket implements Serializable {

    //Header of the IrcPacket
    protected final IrcPacketHeader PACKET_HEADER;

    /**
     * Initialize the header of the packet with an opCode passed in via argument
     * @param opCode
     */
    protected IrcPacket(OpCodes opCode) {
        this.PACKET_HEADER = new IrcPacketHeader(opCode);
    }

    /**
     * @return the PACKET_HEADER
     */
    public IrcPacketHeader getPacketHeader() {
        return this.PACKET_HEADER;
    }
}