/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mailbox;

import java.util.List;
import java.util.Set;

/**
 *
 * @author Tobi
 */
public interface MailboxForPOP3ProxyServer{
    
    String getUIDL(int messageID);
    String[] getAllUIDLs();
    String getSize(int messageID);
    void delMail(int messageID);
    String getMail(int messageID);
    String[] listAllMails();
    String listMail(int messageID);
    String state();
    int reset();
    Set<Integer> getDeletes();
    List<Mailbox.Message> getMessages();
    boolean validID(int msgId);
    
}
