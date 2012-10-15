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
import java.net.*;

public class TCPSocket {

    public static final String NETASCII_EOL = "\r\n";
    /** The default port the client should connect to. */
    public static int SERVER_PORT;
    /** The timeout to use after opening a socket. */
    protected int _timeout_;
    /** The socket used for the connection. */
    public  Socket clientSocket;
    /** The socket's InputStream. */
    protected InputStream _input_;
    /** The socket's OutputStream. */
    protected OutputStream _output_;
    /** The socket's connect timeout (0 = infinite timeout) */
    private static final int DEFAULT_CONNECT_TIMEOUT = 0;
    protected int connectTimeout = DEFAULT_CONNECT_TIMEOUT;

    /**
     * Default constructor for TCPSocket.  Initializes
     * _socket_ to null, _timeout_ to 0, _defaultPort to 0,
     * _isConnected_ to false.
     */
    public TCPSocket() {
        clientSocket = null;
        _input_ = null;
        _output_ = null;
        _timeout_ = 0;
        SERVER_PORT = 6789;

    }

    
    /**
     * Opens a Socket connected to a remote host at the specified port and
     * originating from the current host at a system assigned port.
     * and perform connection initialization actions.
     */
    public void connect(InetAddress host, int port)
            throws SocketException, IOException {

        clientSocket.connect(new InetSocketAddress(host, port), connectTimeout);
        clientSocket.setSoTimeout(_timeout_);
        _input_ = clientSocket.getInputStream();
        _output_ = clientSocket.getOutputStream();

    }

    /*
     * Disconnects the socket connection.
     * You should call this method after you've finished using the class
     * instance and also before you call connect() again. 
     * _isConnected_ is set to false, clientSocket is set to null,
     * _input_ is set to null, and _output_ is set to null.
     * <p>
     * @exception IOException  If there is an error closing the socket.
     */
    public void disconnect() throws IOException
    {
        if (clientSocket != null) clientSocket.close();
        if (_input_ != null) _input_.close();
        if (_output_ != null) _output_.close();
        if (clientSocket != null) clientSocket = null;
        _input_ = null;
        _output_ = null;
    }

    
    // True if the client is currently connected to a server, false otherwise.
    public boolean isConnected() {
        if (clientSocket == null) {
            return false;
        }

        return clientSocket.isConnected();
    }
}
