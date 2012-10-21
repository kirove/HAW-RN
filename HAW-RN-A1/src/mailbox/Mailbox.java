/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mailbox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Tobi
 */
public class Mailbox implements MailboxForPOP3ProxyClients{

    private final List<Message> mails;
    
    public Mailbox(){
        this.mails = new ArrayList<Message>();
    }
    
    public synchronized MailboxForPOP3ProxyServer getMailboxForPOP3ProxyServer() {
        return new MailboxState(new ArrayList<Message>(mails));
    }
    
    public synchronized void update(MailboxForPOP3ProxyServer mb) {
        Set<Integer> toDelete = mb.getDeletes();
        List<Message> messages = mb.getMessages();
        
        for (Integer id : toDelete) {
            Message delMsg = messages.get(id);
            if (mails.contains(delMsg)) {
                mails.remove(delMsg);
            }
        }
    }

    @Override
    public synchronized boolean addMail(String mail) {
        return this.mails.add(new Message(mail));
    }

    protected static class MailboxState implements MailboxForPOP3ProxyServer{

        private List<Message> stateMessages;
        private Set<Integer> delete;
                
        private MailboxState(List<Message> mails){
            this.stateMessages = mails;
            this.delete= new HashSet<Integer>();
        }
        
        public Set<Integer> getDeletes(){
            return new HashSet<Integer>(delete);
        }
        
        public List<Message> getMessages(){
            return new ArrayList<Message>(stateMessages);
        }
        
        @Override
        public String getUIDL(int messageID) {
            return messageID+" "+this.stateMessages.get(messageID).getUIDL();
        }

        @Override
        public String getSizeString(int messageID) {
            return messageID+" "+getSize(messageID);
        }

        @Override
        public void delMail(int messageID) {
            delete.add(messageID);
        }

        @Override
        public String getMail(int messageID) {
            return stateMessages.get(messageID).getContent();
        }
        
        @Override
        public String[] getAllUIDLs() {
            String[] result = new String[stateMessages.size()-delete.size()];
            for (int i = 0; i < result.length; i++) {
                if (!delete.contains(i)){
                    result[i]= getUIDL(i);
                }
            }
            return result;
        }

        @Override
        public String[] listAllMails() {
            int numOfMsgs = stateMessages.size()-delete.size();
            String[] result = new String[numOfMsgs+1];
            int completeSize = 0;
            for (int i = 1; i < result.length; i++) {
                if (!delete.contains(i-1)){
                    result[i]= listMail(i-1);
                    completeSize += getSize(i-1);
                }
            }
            result[0] = numOfMsgs+ " ("+completeSize+" octets)";
            return result;
        }

        @Override
        public String listMail(int messageID) {
            return messageID+" "+stateMessages.get(messageID).getSize();
        }

        @Override
        public String state() {
            int size = 0;
            for (Message message : stateMessages) {
                size+=message.getSize();
            }
            return stateMessages.size()+" "+size;
        }

        @Override
        public int reset() {
            int result = delete.size();
            delete.clear();
            return result;
        }

        @Override
        public boolean validID(int msgId) {
            return (msgId<stateMessages.size() &&
                    msgId>=0 &&
                    !delete.contains(msgId))? true : false;
        }

        @Override
        public int getSize(int messageID) {
            return stateMessages.get(messageID).getSize();
        }


        
    }
    public static class Message {

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 59 * hash + (this.UIDL != null ? this.UIDL.hashCode() : 0);
            hash = 59 * hash + (this.content != null ? this.content.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Message other = (Message) obj;
            if ((this.UIDL == null) ? (other.UIDL != null) : !this.UIDL.equals(other.UIDL)) {
                return false;
            }
            if ((this.content == null) ? (other.content != null) : !this.content.equals(other.content)) {
                return false;
            }
            return true;
        }

        private String getUIDL() {
            return UIDL;
        }

        private String getContent() {
            return content;
        }
        
        private int getSize(){
            return this.content.getBytes().length;
        }
        
        final private String UIDL;
        final private String content;
        
        private Message(String content) {
           this.content = content;
           this.UIDL = (new StringBuilder(System.currentTimeMillis()+Double.toString(Math.random()))).append(content.hashCode()).toString();
        }
    }
    
}
