package code.IRC_Packets;

import code.Codes.OpCodes;

/**
 * 
 */
public abstract class IRC_Packet {

    //Header of the IRC_Packet
    protected IRC_Packet_Header packetHeader;

    /**
     * Initialize the header of the packet with an opCode passed in via argument
     * @param opCode
     */
    protected IRC_Packet(OpCodes opCode) { 
        this.packetHeader = new IRC_Packet_Header(opCode);
    }
}