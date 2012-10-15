package pop3proxy;

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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        String text = "CAPA vfjf";
//        String regex = "^CAPA.*";
//        System.out.println(text.matches(regex));
//        
//        
        POP3Server myPOP3Server = new POP3Server(new ServerSettings(SERVER_PORT, MAIL_CACHE, MAIL_CACHE));
        
    }
}
