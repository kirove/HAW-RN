/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pop3proxy;

import java.util.Objects;

/**
 *
 * @author kirove
 */
public class POP3KontenSettings {

    private String pop3ServerAddress;
    private int pop3ServerPort;
    private String username;
    private String password;

    public POP3KontenSettings(String pop3ServerAddress, int pop3ServerPort, String username, String password) {
        this.pop3ServerAddress = pop3ServerAddress;
        this.pop3ServerPort = pop3ServerPort;
        this.username = username;
        this.password = password;
    }

    public String getServerAddress() {
        return pop3ServerAddress;
    }

    public int getPort() {
        return pop3ServerPort;
    }

    public String getUser() {
        return username;
    }

    public String getPass() {
        return password;
    }

    @Override
    public String toString() {
        return "ServerSettings{" + "/n" + "Server Name= " + pop3ServerAddress + "/n" + "port= " + pop3ServerPort + "/n" + "username= " + username + "/n" + "password= " + password + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.pop3ServerAddress);
        hash = 97 * hash + this.pop3ServerPort;
        hash = 97 * hash + Objects.hashCode(this.username);
        hash = 97 * hash + Objects.hashCode(this.password);
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
        final POP3KontenSettings other = (POP3KontenSettings) obj;
        if (this.pop3ServerPort != other.pop3ServerPort) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        return true;
    }
}
