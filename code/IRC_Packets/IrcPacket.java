package code.IRC_Packets;

import java.io.Serializable;
import code.Codes.OpCodes;

public abstract class IrcPacket implements Serializable {

    protected final IrcPacketHeader PACKET_HEADER;

    protected IrcPacket(OpCodes opCode) {
        this.PACKET_HEADER = new IrcPacketHeader(opCode);
    }

    public IrcPacketHeader getPacketHeader() {
        return this.PACKET_HEADER;
    }
}