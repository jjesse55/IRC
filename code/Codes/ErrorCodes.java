package code.Codes;

/**
 * Codes for errors when processing a client's request
 */
public enum ErrorCodes {
    IRC_ERR_UNKNOWN,            
    IRC_ERR_ILLEGAL_OPCODE,    
    IRC_ERR_ILLEGAL_LENGTH,     //Wrong version
    IRC_ERR_NAME_EXISTS,       
    IRC_ERR_ILLEGAL_PROTOCOL,  
    IRC_ERR_INVALID_ROOM_NAME; 
}