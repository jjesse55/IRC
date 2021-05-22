package code.Codes;

/**
 * OpCodes sent with IrcPacket objects
 */
public enum OpCodes {
    OP_CODE_ERR(0x00000001),                        //An error in the request from the client
    OP_CODE_KEEP_ALIVE(0x00000002),                 //Keep the connection alive between client/server
    OP_CODE_HELLO(0x00000003),                      //Handshake
    OP_CODE_LIST_ROOMS(0x00000004),                 //Request to list all the rooms
    OP_CODE_LIST_ROOMS_RESPONSE(0x00000005),        //Listing all the rooms response.
    OP_CODE_LIST_USERS(0x00000006),                 //List all the users
    OP_CODE_LIST_USERS_RESP(0x00000007),            //Response to listing all the users
    OP_CODE_JOIN_ROOM(0x00000008),                  //Request to join a room
    OP_CODE_JOIN_ROOM_RESP(0x00000009),             //Response that client has joined the room.
    OP_CODE_LEAVE_ROOM(0x0000000A),                 //Request to leave a room
    OP_CODE_LEAVE_ROOM_RESP(0x0000000B),            //Response that the client has left the room
    OP_CODE_SEND_MESSAGE(0x0000000C),               //Client request to send a msg to a room
    OP_CODE_SEND_MESSAGE_RESPONSE(0x0000000D),               //Server sending a message to all users in a room
    OP_CODE_SEND_PRIVATE_MESSAGE(0x0000000E),       //Request to send private message between clients
    OP_CODE_TELL_PRIVATE_MESSAGE(0x0000000F),     //Server sending private message from client-client
    OP_CODE_GOODBYE(0x00000010);                    //End the connection between the client/server



    private int opCode;     //Integer value of the opCode

    /**
     * Constructor for an opCode
     * @param opCode
     */
    OpCodes(int opCode) {
        this.opCode = opCode;
    }

    /**
     * Get the value of an opCode
     * @return
     */
    public int getOpCode() { return this.opCode; }
}