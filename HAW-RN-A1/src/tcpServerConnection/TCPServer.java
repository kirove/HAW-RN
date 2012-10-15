package tcpServerConnection;

import exampleCode.*;
import java.io.*;

import java.net.*;

/*
 * TCPServer.java
 *
 * Version 1.0
 * Vorlesung Rechnernetze HAW Hamburg
 * Autor: M. H�bner (nach Kurose/Ross)
 * Zweck: TCP-Server Beispielcode:
 *        Bei Dienstanfrage einen Arbeitsthread erzeugen, der eine Anfrage bearbeitet:
 *        einen String empfangen, in Gro�buchstaben konvertieren und zur�cksenden
 */


/* Server, der Verbindungsanfragen entgegennimmt */
public class TCPServer {
	public static final int SERVER_PORT = 6789;

	public static void main(String[] args) {
		ServerSocket welcomeSocket;  // TCP-Server-Socketklasse
		Socket connectionSocket;     // TCP-Standard-Socketklasse

		int counter = 0; // Z�hlt die erzeugten Bearbeitungs-Threads

		try {
			/* Server-Socket erzeugen */
			welcomeSocket = new ServerSocket(SERVER_PORT);

			while (true) { // Server laufen IMMER
				System.out.println("Warte auf Verbindungswunsch auf Port "
						+ SERVER_PORT);
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
/* Arbeitsthread, der eine existierende Socket-Verbindung zur Bearbeitung erh�lt */
class TCPServerThread extends Thread {
	private int name;
	private Socket socket;

	public TCPServerThread(int num, Socket sock) {
		/* Konstruktor */
		this.name = num;
		this.socket = sock;
	}

	public void run() {
		BufferedReader inFromClient;
		DataOutputStream outToClient;

		String clientSentence;
		String capitalizedSentence;
		boolean serviceRequested = true; // Arbeitsthread beenden?

		System.out.println("TCP Server Thread " + name
				+ " is running until QUIT is received!");

		try {
			/* Socket-Basisstreams durch spezielle Streams filtern */
			inFromClient = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			outToClient = new DataOutputStream(socket.getOutputStream());

			while (serviceRequested) {
				/* String vom Client empfangen */
				clientSentence = inFromClient.readLine();
				System.out.println("TCP Server Thread detected job: "
						+ clientSentence);
				capitalizedSentence = clientSentence.toUpperCase() + '\n';

				/* Modifizierten String an Client senden */
				outToClient.writeBytes(capitalizedSentence);

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
}
