package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Testat2Client {
	
	public static final int serverPort = 7777;
	
	public static void main(String[] args) {
		String hostname = "localhost";
		
		if (args.length > 0) {hostname = args[0];}
		
		PrintWriter networkOut = null;
		BufferedReader networkIn = null;
		Socket s = null;
		
		try {
			//Eingabemöglichkeit für den Benutzer
			BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
			
			//In Endlosschleife auf Benutzereingabe warten
			while(true) {
				
				//Auslesen der Zeile. Wenn Zeile = . dann beenden des Clients
				String theLine = userIn.readLine();
				if (theLine.equals(".")) break;
				
				//Verbindung zum Server herstellen
				s = new Socket(hostname, serverPort);
				
				//Initialisieren des Writers und des Readers
				networkIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
				networkOut = new PrintWriter(s.getOutputStream());
				
				//Eingegebene Zeile zum Server versenden
				networkOut.println(theLine);
				networkOut.flush();
				
				//Ankommende Zeile in die Konsole schreiben
				System.out.println(networkIn.readLine());
				
				//Wegen Non-Persistent alle Verbindungen schließen
				if(networkIn != null) {
					networkIn.close();
				}
				if(networkOut != null) {
					networkOut.close();
				}
				if(s != null) {
					s.close();
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		} 
	}
}
