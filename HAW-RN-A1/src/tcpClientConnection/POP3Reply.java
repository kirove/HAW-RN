
package tcpClientConnection;

/***
 * POP3Reply stores POP3 reply code constants.
 ***/

public final class POP3Reply
{
    // The reply code indicating success of an operation. 
    public static final int OK = 0;

    // The reply code indicating failure of an operation.
    public static final int ERROR = 1;

    // Cannot be instantiated.
    private POP3Reply()
    {}
}
