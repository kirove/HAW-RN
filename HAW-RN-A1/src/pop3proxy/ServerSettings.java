/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pop3proxy;

import java.util.Objects;
import java.net.*;

/**
 *
 * @author Tobi
 */
public class ServerSettings {
    
    private int port;
    private String user;
    private String pass;

    public ServerSettings(int port, String user, String pass) {
        this.port = port;
        this.user = user;
        this.pass = pass;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    @Override
    public String toString() {
        return "ServerSettings{" + "port=" + port + ", user=" + user + ", pass=" + pass + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.port;
        hash = 97 * hash + Objects.hashCode(this.user);
        hash = 97 * hash + Objects.hashCode(this.pass);
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
        final ServerSettings other = (ServerSettings) obj;
        if (this.port != other.port) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        if (!Objects.equals(this.pass, other.pass)) {
            return false;
        }
        return true;
    }

    
    
    
    
    
    
}
