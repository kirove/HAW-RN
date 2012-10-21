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
    // Commands
    private final String USER = "USER ";
    private final String PASS = "PASS ";
    private final String QUIT = "QUIT";
    private final String STAT = "STAT";
    private final String LIST = "LIST";
    private final String RETR = "RETR ";
    private final String DELE = "DELE ";
    private final String NOOP = "NOOP";
    private final String RSET = "RSET";
    private final String UIDL = "UIDL";
    private final String AUTH = "AUTH";
    private final String CAPA = "CAPA";
    private final String OK = "+OK ";
    private final String ERR = "-ERR ";
    private final String CRNL = "\r\n";

    private static enum STATUS {

        AUTH, TRANS, UPDATE
    }

    public POP3Server(ServerSettings serversettings) {
        this.serversettings = serversettings;
        try {
            /* Server-Socket erzeugen */
            welcomeSocket = new ServerSocket(this.serversettings.getPort());

            while (true) { // Server laufen IMMER
                System.out.println("POP3 Server wartet auf Verbindung auf TCP Port "
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
        private BufferedReader inFromClient;
        private DataOutputStream outToClient;
        private boolean serviceRequested = true; // Arbeitsthread beenden?
        private STATUS currentState = STATUS.AUTH;
        private String clientSentence = "";

        // Konstruktor
        public POP3ServerThread(int id, Socket sock) {
            this.id = id;
            this.socket = sock;
        }

        @Override
        public void run() {



            System.out.println("POP3 Server Thread " + id
                    + " is running until QUIT is received!");

            try {
                /* Socket-Basisstreams durch spezielle Streams filtern */
                inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outToClient = new DataOutputStream(socket.getOutputStream());

                sendWelcomeMessage();

                while (serviceRequested) {

                    //Prüfen ob Server sich in er Authentifizierungs-Phase befindet
                    if (currentState == STATUS.AUTH) {
                        authenticate();
                    } //Transaction State
                    else if (currentState == STATUS.TRANS) {
                        transaction();
                    }

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

        private boolean isQuitCommand(String message) {
            return message.equalsIgnoreCase(QUIT);
        }

        private boolean isNoopCommand(String message) {
            return message.equalsIgnoreCase(NOOP);
        }

        private boolean isCapaOrAuthCommand(String message) {
            message = message.toUpperCase();
            return (message.startsWith(CAPA) || message.startsWith(AUTH)) ? true : false;
        }

        private boolean isUserCommand(String message) {
            message = message.toUpperCase();
            return (message.startsWith(USER)) ? true : false;
        }

        private boolean isPassCommand(String message) {
            message = message.toUpperCase();
            return (message.startsWith(PASS)) ? true : false;
        }

        private boolean isStatCommand(String message) {
            return message.equalsIgnoreCase(STAT);
        }

        private boolean isRsetCommand(String message) {
            return message.equalsIgnoreCase(RSET);
        }

        private boolean isListCommand(String message) {
            message = message.toUpperCase();
            return (message.startsWith(LIST)) ? true : false;
        }

        private boolean isUidlCommand(String message) {
            message = message.toUpperCase();
            return (message.startsWith(UIDL)) ? true : false;
        }

        private boolean isRetrCommand(String message) {
            message = message.toUpperCase();
            return (message.startsWith(RETR)) ? true : false;
        }

        private boolean isDeleCommand(String message) {
            message = message.toUpperCase();
            return (message.startsWith(DELE)) ? true : false;
        }

        private void transaction() {
            readFromClient();
            if (isStatCommand(clientSentence)) {
                sendStatMessage();
            } else if (isRsetCommand(clientSentence)) {
                sendRsetMessage();
            } else if (isListCommand(clientSentence)) {
                sendListMessage();
            } else if (isUidlCommand(clientSentence)) {
                sendUidlMessage();
            } else if (isRetrCommand(clientSentence)) {
                sendRetrMessage();
            } else if (isDeleCommand(clientSentence)) {
                sendDeleMessage();
            } else if (isQuitCommand(clientSentence)) {
                sendQuitAck();
            } else if (isNoopCommand(clientSentence)) {
                sendNoopAck();
            } else {
                sendInvalidCommandMessage();
            }

        }

        private void sendRetrMessage() {
            System.out.println(">>RETR");
            // TODO: Implement retr command
            throw new UnsupportedOperationException("Not yet implemented");
        }

        private void sendDeleMessage() {
            System.out.println(">>DELE");
            // TODO: Implement Dele command
            throw new UnsupportedOperationException("Not yet implemented");
        }

        private void sendUidlMessage() {
            System.out.println(">>UIDL");
            // TODO: Implement uidl command
            throw new UnsupportedOperationException("Not yet implemented");
        }

        private void sendRsetMessage() {
            System.out.println(">>RSET");
            // TODO: Implement stat command
            throw new UnsupportedOperationException("Not yet implemented");
        }

        private void sendStatMessage() {
            System.out.println(">>STAT");
            // TODO: Implement stat command
            throw new UnsupportedOperationException("Not yet implemented");
        }

        private void sendListMessage() {
            System.out.println(">>LIST");
            // TODO: Implement list command
            throw new UnsupportedOperationException("Not yet implemented");
        }

        private void sendQuitAck() {
            sendOKMessage("Connection will be closed. See you soon!");
            serviceRequested = false;
        }

        private void sendNoopAck() {
            sendOKMessage("Received your noop coomand. What do you want?");
        }

        private void sendInvalidCommandMessage() {
            sendERRMessage("Your command is not valid(at this position).");
        }

        private void sendWelcomeMessage() {
            sendOKMessage("Hello. I'm your POP3 server. Who are you?");
        }

        private void sendOKMessage(String messageText) {
            writeToClient(OK + messageText + CRNL);
        }

        private void sendERRMessage(String messageText) {
            writeToClient(ERR + messageText + CRNL);
        }

        private String readFromClient() {
            /* Liefere die n�chste Anfrage-Zeile (request) vom Client */
            String request = "";

            try {
                request = inFromClient.readLine();
                request = request.trim();
            } catch (IOException e) {
                System.err.println("Connection aborted by client!");
                serviceRequested = false;
            }
            System.out.println("-- POP3 Server Thread detected job: " + request);

            clientSentence = request;
            return request;
        }

        private void writeToClient(String reply) {
            /* Sende den String als Antwortzeile (mit newline) zum Client */
            try {
                outToClient.writeBytes(reply/* + '\n'*/);
            } catch (IOException e) {
                System.err.println(e.toString());
                serviceRequested = false;
            }
            System.out.println("-- POP3 Server Thread " + id
                    + " has written the message: " + reply);
        }

        private void authenticate() {
            readFromClient();
            System.out.println("POP3 Server Thread starts Authentication:\n");
            if (isCapaOrAuthCommand(clientSentence)) {
                sendERRMessage("AUTH and CAPA are not supported. Use USER and PASS to authenticate.");
            } else if (isUserCommand(clientSentence)) {
                String username = clientSentence.substring(5);
                System.out.println("'"+username+"' = '"+serversettings.getUser()+"'?");
                if (username.equals(serversettings.getUser())) {
                    // Korrekter Benutzername
                    sendOKMessage("Are you really " + username + "? Please tell me your password.");
                    readFromClient();
                    if (isPassCommand(clientSentence)) {
                        String password = clientSentence.substring(5);
                        if (password.equals(serversettings.getPass())) {
                            // Korrektes Passwort
                            sendOKMessage("Hi " + username + "! How can I help you?");
                            currentState = STATUS.TRANS;
                        } else {
                            // Falsches Passwort
                            sendERRMessage("Wrong password dude!");
                        }
                    } else if (isQuitCommand(clientSentence)) {
                        sendQuitAck();
                    } else if (isNoopCommand(clientSentence)) {
                        sendNoopAck();
                    } else {
                        sendInvalidCommandMessage();
                    }
                } else {
                    // Falscher Benutzername
                    sendERRMessage("YOU SHALL NOT PASS!!! Sorry dude, I don't know any " + username + ".");
                }
            } else if (isQuitCommand(clientSentence)) {
                sendQuitAck();
            } else if (isNoopCommand(clientSentence)) {
                sendNoopAck();
            } else {
                sendInvalidCommandMessage();
            }
        }
    }
}
