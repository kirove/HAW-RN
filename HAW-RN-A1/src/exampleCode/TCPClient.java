package exampleCode;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/*
 * TCPClient.java
 *
 * Version 1.0
 * Vorlesung Rechnernetze HAW Hamburg
 * Autor: M. H�bner (nach Kurose/Ross)
 * Zweck: TCP-Client Beispielcode:
 *        TCP-Verbindung zum Server aufbauen, einen vom Benutzer eingegebenen
 *        String senden, den String in Gro�buchstaben empfangen und ausgeben
 */

public class TCPClient {

	public static final int SERVER_PORT = 6789;

	public static Socket clientSocket;   // TCP-Standard-Socketklasse

	/* Client starten. Ende, wenn quit eingegeben wurde */
	public void startJob() {
		/* Variablen f�r Ein- Ausgabestreams */
		Scanner inFromUser;
		DataOutputStream outToServer;
		BufferedReader inFromServer;
		
		String sentence; // vom User �bergebener String
		String modifiedSentence; // vom Server modifizierter String
		boolean serviceRequested = true; // Client beenden?

		/* Ab Java 7: try-with-resources mit automat. close benutzen! */
		try {
			/* Socket erzeugen --> Verbindungsaufbau mit dem Server */
			clientSocket = new Socket("localhost", SERVER_PORT);

			/* Socket-Basisstreams durch spezielle Streams filtern */
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));

			/* Konsolenstream (Standardeingabe) initialisieren */
			inFromUser = new Scanner(System.in);

			while (serviceRequested) {
				System.out.println("ENTER TCP-DATA: ");
				/* String vom Benutzer (Konsoleneingabe) holen */
				sentence = inFromUser.nextLine();

				/* String an den Server senden */
				outToServer.writeBytes(sentence + '\n');

				/* Modifizierten String vom Server empfangen */
				modifiedSentence = inFromServer.readLine();
				System.out.println("FROM SERVER: " + modifiedSentence);

				/* Test, ob Client beendet werden soll */
				if (modifiedSentence.indexOf("QUIT") > -1) {
					serviceRequested = false;
				}
			}
			/* Socket-Streams schlie�en --> Verbindungsabbau */
			clientSocket.close();
		} catch (IOException e) {
			System.err.println(e.toString());
			System.exit(1);
		}
		System.out.println("TCP Client stopped!");
	}

	public static void main(String[] args) {
		TCPClient myClient = new TCPClient();
		myClient.startJob();
	}
}
