package code.Codes;

/**
 * Codes for errors when processing a client's rqst
 */
public enum ErrorCodes {
    IRC_ERR_UNKNOWN(0x00000001),            //Unknown error (general error)
    IRC_ERR_ILLEGAL_OPCODE(0x00000002),     //Illegal OpCode specified
    IRC_ERR_WRONG_VERSION(0x00000003),      //Wrong version
    IRC_ERR_NAME_EXISTS(0x00000004);        //Name for a client is pre-existant

    private int errCode;    //Integer value representing the errCode

    ErrorCodes(int errCode) { this.errCode = errCode; }

    public int getErrCode() { return this.errCode; }
}