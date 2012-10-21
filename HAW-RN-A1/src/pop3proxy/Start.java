package pop3proxy;

import java.io.IOException;
import tcpClientConnection.POP3Client;
import tcpClientConnection.TCPSocket;
import tcpServerConnection.POP3Server;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tobi
 */
public class Start {

    // common  settings
    public static final String MAIL_CACHE = "mailCache/";
    
    // server settings
    public static final int SERVER_PORT = 11000;
    public static final String SERVER_USER = "collectot";
    public static final String SERVER_PASS = "password";
    
    // POP3server settings
    public static final String POP3SERVERADDRESS = "pop.gmx.de";
    public static final int POP3SERVER_PORT = 110;
    public static final String POP3SERVER_USER = "kirove@gmx.de";
    public static final String POP3SERVER_PASS = "dontfuckwithme";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
//        String text = "CAPA vfjf";
//        String regex = "^CAPA.*";
//        System.out.println(text.matches(regex));
//        
//        
    //    POP3Server myPOP3Server = new POP3Server(new ServerSettings(SERVER_PORT, MAIL_CACHE, MAIL_CACHE));
        POP3Client POP3ServerKonto = new POP3Client(new POP3KontenSettings(POP3SERVERADDRESS,POP3SERVER_PORT, POP3SERVER_USER, POP3SERVER_PASS));
        
    }
}
