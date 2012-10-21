package exampleCode;

/*
 * TCPServer.java
 *
 * Version 2.0
 * Vorlesung Rechnernetze HAW Hamburg
 * Autor: M. H�bner (nach Kurose/Ross)
 * Zweck: TCP-Server Beispielcode:
 *        Bei Dienstanfrage einen Arbeitsthread erzeugen, der eine Anfrage bearbeitet:
 *        einen String empfangen, in Gro�buchstaben konvertieren und zur�cksenden
 */
import java.io.*;
import java.net.*;

public class TCPServer {
  /* Server, der Verbindungsanfragen entgegennimmt */
  public static final int SERVER_PORT = 6789;

  public static void main(String[] args) {
    ServerSocket welcomeSocket; // TCP-Server-Socketklasse
    Socket connectionSocket; // TCP-Standard-Socketklasse

    int counter = 0; // Z�hlt die erzeugten Bearbeitungs-Threads

    try {
      /* Server-Socket erzeugen */
      welcomeSocket = new ServerSocket(SERVER_PORT);

      while (true) { // Server laufen IMMER
        System.out.println("TCP Server: Waiting for connection - listening TCP port " +
                           SERVER_PORT);
        /*
         * Blockiert auf Verbindungsanfrage warten --> nach
         * Verbindungsaufbau Standard-Socket erzeugen und
         * connectionSocket zuweisen
         */
        connectionSocket = welcomeSocket.accept();

        /* Neuen Arbeits-Thread erzeugen und den Socket �bergeben */
        (new TCPServerThread(++counter, connectionSocket)).start();
      }
    } catch (IOException e) {
      System.err.println(e.toString());
    }
  }
}


class TCPServerThread extends Thread {
  /* Arbeitsthread, der eine existierende Socket-Verbindung zur Bearbeitung erh�lt */
  private int name;
  private Socket socket;

  private BufferedReader inFromClient;
  private DataOutputStream outToClient;

  boolean serviceRequested = true; // Arbeitsthread beenden?

  public TCPServerThread(int num, Socket sock) {
    /* Konstruktor */
    this.name = num;
    this.socket = sock;
  }

  public void run() {
    String capitalizedSentence;

    System.out.println("TCP Server Thread " + name +
                       " is running until QUIT is received!");

    try {
      /* Socket-Basisstreams durch spezielle Streams filtern */
      inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      outToClient = new DataOutputStream(socket.getOutputStream());

      while (serviceRequested) {
        /* String vom Client empfangen und in Gro�buchstaben umwandeln */
        capitalizedSentence = readFromClient().toUpperCase();

        /* Modifizierten String an Client senden */
        writeToClient(capitalizedSentence);

        /* Test, ob Arbeitsthread beendet werden soll */
        if (capitalizedSentence.indexOf("QUIT") > -1) {
          serviceRequested = false;
        }
      }

      /* Socket-Streams schlie�en --> Verbindungsabbau */
      socket.close();
    } catch (IOException e) {
      System.err.println(e.toString());
      System.exit(1);
    }

    System.out.println("TCP Server Thread " + name + " stopped!");
  }

  private String readFromClient() {
    /* Liefere die n�chste Anfrage-Zeile (request) vom Client */
    String request = "";

    try {
      request = inFromClient.readLine();
    } catch (IOException e) {
      System.err.println("Connection aborted by client!");
      serviceRequested = false;
    }
    System.out.println("TCP Server Thread detected job: " + request);
    return request;
  }

  private void writeToClient(String reply) {
    /* Sende den String als Antwortzeile (mit newline) zum Client */
    try {
      outToClient.writeBytes(reply + '\n');
    } catch (IOException e) {
      System.err.println(e.toString());
      serviceRequested = false;
    }
    System.out.println("TCP Server Thread " + name +
                       " has written the message: " + reply);
  }
}
