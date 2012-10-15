/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpServerConnection;

import java.io.*;
import java.net.*;
import pop3proxy.ServerSettings;

/**
 *
 * @author Tobi
 */
public class POP3Server {
    
    
    

    private final ServerSettings serversettings;
    private POP3ServerThread serverThread;
    private ServerSocket welcomeSocket;
    private Socket connectionSocket;     // TCP-Standard-Socketklasse
    private int counter = 0; // Z�hlt die erzeugten Bearbeitungs-Threads

    public POP3Server(ServerSettings serversettings) {
        this.serversettings = serversettings;
        try {
            /* Server-Socket erzeugen */
            welcomeSocket = new ServerSocket(this.serversettings.getPort());

            while (true) { // Server laufen IMMER
                System.out.println("Warte auf Verbindungswunsch auf Port "
                        + this.serversettings.getPort());
                /*
                 * Blockiert auf Verbindungsanfrage warten --> nach
                 * Verbindungsaufbau Standard-Socket erzeugen und
                 * connectionSocket zuweisen
                 */
                connectionSocket = welcomeSocket.accept();

                /* Neuen Arbeits-Thread erzeugen und den Socket �bergeben */
                (new POP3ServerThread(++counter, connectionSocket)).start();
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    class POP3ServerThread extends Thread {

        private int id;
        private Socket socket;

        public POP3ServerThread(int id, Socket sock) {

            this.id = id;
            this.socket = sock;
        }

        @Override
        public void run() {
            BufferedReader inFromClient;
            DataOutputStream outToClient;

            String clientSentence;
            boolean serviceRequested = true; // Arbeitsthread beenden?
            boolean authState = true;
            boolean transState = false;

            System.out.println("POP3 Server Thread " + id
                    + " is running until client closes connection!");

            try {
                /* Socket-Basisstreams durch spezielle Streams filtern */
                inFromClient = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                outToClient = new DataOutputStream(socket.getOutputStream());

                outToClient.writeBytes("+OK Hello. I'm your POP3 server. I'm waiting for your commands!");
                while (serviceRequested) {
                    

                    /* String vom Client empfangen */
                    clientSentence = inFromClient.readLine();
                    System.out.println("TCP Server Thread detected job: "
                            + clientSentence);
                    
                    
                    //Quit kann jederzeit ausgeführt werden
                    if (clientSentence.matches("^QUIT$")){
                        outToClient.writeBytes("+OK Connection will be closed. See you soon!");
                        serviceRequested=false;
                    }
                    //Prüfen ob Server sich in er Authentifizierungs-Phase befindet
                    else if (authState){
                        System.out.println("Server is in Authentification State");
                        //CAPA
                        if (clientSentence.matches("^CAPA.*")) {
                            outToClient.writeBytes("-ERR");
                        }
                        //AUTH
                        else if (clientSentence.matches("^AUTH.*")) {
                            outToClient.writeBytes("-ERR");
                        }
                        //Basis Authentifizierung über Benutzername und Passwort
                        else if (clientSentence.matches("^USER\\s.+$")) {
                            String username = clientSentence.split("^USER\\s", 1)[0];
                            if (username.equals(serversettings.getUser())){
                                outToClient.writeBytes("+OK Are you really "+username+"? Please tell me your password.");
                                clientSentence = inFromClient.readLine();
                                if (clientSentence.matches("^PASS\\s.+$")){
                                    String password = clientSentence.split("^PASS\\s", 1)[0];
                                    if (password.equals(serversettings.getPass())){
                                        outToClient.writeBytes("+OK Hi "+username+"! How can I help you?");
                                        authState= false;
                                        transState = true;
                                    } else {
                                        outToClient.writeBytes("+ERR Wrong password dude! Fck off");
                                    }
                                } else if (clientSentence.matches("^QUIT$")){
                                    outToClient.writeBytes("+OK Connection will be closed. See you soon!");
                                    serviceRequested=false;
                                } else {
                                    outToClient.writeBytes("+ERR Invalid command. \"PASS <password>\" or \"QUIT\" required.");
                                }

                            } else {
                                outToClient.writeBytes("+ERR YOU SHALL NOT PASS!!! Sorry dude, I don't know your any "+username+".");
                            }
                        }    
                    }
                    
                    //Transaction State
                    else if (transState){
                        if (clientSentence.matches("^QUIT$")){
                            outToClient.writeBytes("+OK Connection will be closed. See you soon!");
                            serviceRequested=false;
                        }
                        
                        //STAT
                        else if (clientSentence.matches("^STAT$")){
                            //TODO: Anzahl mails und Größe zurückgeben. Zum Löschen markierte Mails werden nicht mit eingerechnet.
                        }
                        // LIST
                        else if (clientSentence.matches("^LIST\\s\\d*$")){
                            // TODO: IDs und Größe aller Mails auflisten
                        }
                        // RETR
                        else if (clientSentence.matches("^RETR\\s\\d+$")){
                            // TODO: E-Mail zeilenweise übertragen
                        }
                        //NOOP
                        else if (clientSentence.matches("^NOOP$")){
                            outToClient.writeBytes("+OK");
                        }
                        //RSET
                        else if (clientSentence.matches("^RSET$")){
                            // TODO: Alle (zum Löschen) markierten Nachrichten "deamarkieren"
                        }
                        
                    }
                    


                    


                    /* Modifizierten String an Client senden */
                    //			outToClient.writeBytes(capitalizedSentence);

                    /* Test, ob Arbeitsthread beendet werden soll */
//                    if (capitalizedSentence.indexOf("QUIT") > -1) {
//                        serviceRequested = false;
//                    }
                }
                /* Socket-Streams schlie�en --> Verbindungsabbau */
                
                //ToDo: E-Mails löschen
                socket.close();
            } catch (IOException e) {
                System.err.println(e.toString());
                System.exit(1);
            }

            System.out.println("TCP Server Thread " + id + " stopped!");
        }
    }
}
