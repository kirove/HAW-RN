package pop3proxy;

import java.io.IOException;
import mailbox.Mailbox;
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
    public static final mailbox.Mailbox MAILBOX = new Mailbox();
    //public static final String MAIL_CACHE = "mailCache/";
    
    // server settings
    public static final int SERVER_PORT = 11000;
    public static final String SERVER_USER = "myUser";
    public static final String SERVER_PASS = "pass123";
    
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
        POP3Server myPOP3Server = new POP3Server(new ProxyServerSettings(SERVER_PORT, SERVER_USER, SERVER_PASS), MAILBOX);
    //    POP3Client POP3ServerKonto = new POP3Client(new POP3KontenSettings(POP3SERVERADDRESS,POP3SERVER_PORT, POP3SERVER_USER, POP3SERVER_PASS));
        
    }
}
