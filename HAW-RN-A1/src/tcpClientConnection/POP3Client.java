/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpClientConnection;

/**
 *
 * @author NED
 */
import java.io.*;

public class POP3Client extends TCPSocket{

    public static final String NETASCII_EOL = "\r\n";
    /*** The default POP3 port.  Set to 110 according to RFC 1288. ***/
    public static final int DEFAULT_PORT = 110;
    /***
     * A constant representing the state where the client is not yet connected
     * to a POP3 server.
     ***/
    public static final int DISCONNECTED_STATE = -1;
    /***  A constant representing the POP3 authorization state. ***/
    public static final int AUTHORIZATION_STATE = 0;
    /***  A constant representing the POP3 transaction state. ***/
    public static final int TRANSACTION_STATE = 1;
    /***  A constant representing the POP3 update state. ***/
    public static final int UPDATE_STATE = 2;
    private int __popState;
    // The reply code. 
    public static final String OK = "+OK";
    public static final String ERROR = "-ERR";
    public static final String QUIT = "QUIT";
    public static final String NOOP = "NOOP";
    public static final String DELE = "DELE";
    public static final String RSET = "RSET";
    public static final String STAT = "STAT";
    private BufferedWriter __writer;
    private StringBuffer __commandBuffer;
    DataOutputStream outToServer;
    BufferedReader inFromServer;
    private static String _replyCode;

    /*
     * Login to the POP3 server with the given username and password.  You
     * must first connect to the server with connect before attempting to login.  
     * A login attempt is only valid if the client is in the AUTHORIZATION_STATE.
     * After logging in, the client enters the TRANSACTION_STATE.
     */
    public boolean login(String username, String password) throws IOException {

        if (getState() != AUTHORIZATION_STATE) {
            return false;
        }

        if (sendCommand("USER", username) != OK) {
            return false;
        }

        if (sendCommand("PASS", password) != OK) {
            return false;
        }

        setState(TRANSACTION_STATE);

        return true;
    }

    /*
     * Logout of the POP3 server.  
     * A logout attempt is valid in any state.  
     * If the client is in the TRANSACTION_STATE TRANSACTION_STATE, it enters the UPDATE_STATE UPDATE_STATE 
     */
    public boolean logout() throws IOException {
        if (getState() == TRANSACTION_STATE) {
            setState(UPDATE_STATE);
        }
        sendCommand(QUIT, null);
        return (_replyCode == OK);
    }

    // Sets POP3 client state. 
    public void setState(int state) {
        __popState = state;
    }

    // Returns the current POP3 client state.
    public int getState() {
        return __popState;
    }

     //Sends a command an arguments to the server and set the reply code.
     public String sendCommand(String command, String args) throws IOException {
        String message;

        __commandBuffer.setLength(0);
        __commandBuffer.append(command);

        if (args != null) {
            __commandBuffer.append(' ');
            __commandBuffer.append(args);
        }
        __commandBuffer.append(NETASCII_EOL);

        __writer.write(message = __commandBuffer.toString());
        __writer.flush();

        outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes(message + '\n');

        __getReply();
        return _replyCode;
    }

     //Gets the reply from the server
    private void __getReply() throws IOException {

        String line;

        inFromServer = new BufferedReader(new InputStreamReader(
                clientSocket.getInputStream()));

       
        line = inFromServer.readLine();

        if (line == null) {
            throw new EOFException("Connection closed without indication.");
        }

        if (line.startsWith(OK)) {
            _replyCode = OK;
        } else if (line.startsWith(ERROR)) {
            _replyCode = ERROR;
        } else {
            throw new IOException(
                    "Received invalid POP3 protocol response from server.");
        }

    }

    /*
     * Send a NOOP command to the POP3 server.  This is useful for keeping
     * a connection alive since most POP3 servers will timeout after 10
     * minutes of inactivity.  A noop attempt will only succeed if
     * the client is in the TRANSACTION_STATE }
     */
    public boolean noop() throws IOException {
        if (getState() == TRANSACTION_STATE) {
            return (sendCommand(NOOP, null) == OK);
        }
        return false;
    }

    /*
     * Delete a message from the POP3 server.  The message is only marked
     * for deletion by the server.  If you decide to unmark the message, you
     * must issuse a reset command.  Messages marked
     * for deletion are only deleted by the server on logout.
     * A delete attempt can only succeed if the client is in the TRANSACTION_STATE.
     */
    public boolean deleteMessage(int messageId) throws IOException {
        if (getState() == TRANSACTION_STATE) {
            return (sendCommand(DELE, Integer.toString(messageId)) == OK);
        }
        return false;
    }

    /*
     * Reset the POP3 session.  This is useful for undoing any message
     * deletions that may have been performed.  A reset attempt can only
     * succeed if the client is in the TRANSACTION_STATE.
     */
    public boolean reset() throws IOException {
        if (getState() == TRANSACTION_STATE) {
            return (sendCommand(RSET, null) == OK);
        }
        return false;
    }
    
    /*
     * Disconnects the client from the server, and sets the state to
     * DISCONNECTED_STATE.  The reply text information.
     */

    @Override
    public void disconnect() throws IOException
    {
        super.disconnect();
        __writer = null;
        setState(DISCONNECTED_STATE);
    }
    
    /*
     * Get the mailbox status.  A status attempt can only
     * succeed if the client is in the TRANSACTION_STATE. 
     * Returns a POP3MessageInfo instance containing the number of messages in the mailbox and the total
     * size of the messages in bytes.  Returns null if the status the attempt fails.
     */

}
