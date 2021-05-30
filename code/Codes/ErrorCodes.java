package code.Codes;

/**
 * Codes for errors when processing a client's request
 */
public enum ErrorCodes {
    IRC_ERROR_UNKNOWN,            
    IRC_ERROR_ILLEGAL_OPCODE,    
    IRC_ERROR_ILLEGAL_LENGTH,     //Wrong version
    IRC_ERROR_NAME_EXISTS,       
    IRC_ERROR_ILLEGAL_PROTOCOL,  
    IRC_ERROR_INVALID_ROOM_NAME; 
}