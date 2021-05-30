package code.Codes;

/**
 * OpCodes sent with IrcPacket objects
 */
public enum OpCodes {
    OP_CODE_ERROR,                        //An error in the request from the client
    OP_CODE_KEEP_ALIVE,                 //Keep the connection alive between client/server
    OP_CODE_HELLO,                      //Handshake
    OP_CODE_LIST_ROOMS,                 
    OP_CODE_LIST_ROOMS_RESPONSE,       
    OP_CODE_LIST_USERS,                 
    OP_CODE_LIST_USERS_response,            
    OP_CODE_JOIN_ROOM,                 
    OP_CODE_JOIN_ROOM_response,            
    OP_CODE_LEAVE_ROOM,              
    OP_CODE_LEAVE_ROOM_response,           
    OP_CODE_SEND_MESSAGE,               
    OP_CODE_SEND_MESSAGE_RESPONSE,      //Server sending a message to all users in a room
    OP_CODE_SEND_PRIVATE_MESSAGE,       
    OP_CODE_GOODBYE;                    
}