package code.IRC_Packets;

import java.io.Serializable;
import code.Codes.OpCodes;

public abstract class IrcPacket implements Serializable {

    //Header of the IrcPacket
    protected IrcPacketHeader packetHeader;

    /**
     * Initialize the header of the packet with an opCode passed in via argument
     * @param opCode
     */
    protected IrcPacket(OpCodes opCode) {
        this.packetHeader = new IrcPacketHeader(opCode);
    }

    /**
     * @return the packetHeader
     */
    public IrcPacketHeader getPacketHeader() {
        return this.packetHeader;
    }
}