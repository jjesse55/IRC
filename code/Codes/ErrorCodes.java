package code.Codes;

/**
 * Codes for errors when processing a client's rqst
 */
public enum ErrorCodes {
    IRC_ERR_UNKNOWN,            //Unknown error (general error)
    IRC_ERR_ILLEGAL_OPCODE,     //Illegal OpCode specified
    IRC_ERR_ILLEGAL_LENGTH,     //Wrong version
    IRC_ERR_NAME_EXISTS,        //Name for a client is pre-existant
    IRC_ERR_ILLEGAL_PROTOCOL,  //Wrong protocol specified to communicate with the server
    IRC_ERR_INVALID_ROOM_NAME;  //Client entered in an invalid room name
}