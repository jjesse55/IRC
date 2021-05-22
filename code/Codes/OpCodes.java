package code.Codes;

/**
 * OpCodes sent with IrcPacket objects
 */
public enum OpCodes {
    OP_CODE_ERR,                        //An error in the request from the client
    OP_CODE_KEEP_ALIVE,                 //Keep the connection alive between client/server
    OP_CODE_HELLO,                      //Handshake
    OP_CODE_LIST_ROOMS,                 //Request to list all the rooms
    OP_CODE_LIST_ROOMS_RESPONSE,        //Listing all the rooms response.
    OP_CODE_LIST_USERS,                 //List all the users
    OP_CODE_LIST_USERS_RESP,            //Response to listing all the users
    OP_CODE_JOIN_ROOM,                  //Request to join a room
    OP_CODE_JOIN_ROOM_RESP,             //Response that client has joined the room.
    OP_CODE_LEAVE_ROOM,                 //Request to leave a room
    OP_CODE_LEAVE_ROOM_RESP,            //Response that the client has left the room
    OP_CODE_SEND_MESSAGE,               //Client request to send a msg to a room
    OP_CODE_SEND_MESSAGE_RESPONSE,               //Server sending a message to all users in a room
    OP_CODE_SEND_PRIVATE_MESSAGE,       //Request to send private message between clients
    OP_CODE_GOODBYE;                    //End the connection between the client/server
}