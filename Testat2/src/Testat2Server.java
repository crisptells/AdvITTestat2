package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Testat2Server {
	
	public final static int DEFAULT_PORT = 7777;
	private ServerSocket server;
	
	
	public Testat2Server(int port) {
		try {
			//Neuen ServerSocket erstellen, der auf dem Vorgegebenen Port 7777 läuft
			server = new ServerSocket(port);
			System.out.println("Server started on port: " + port);
		} catch (IOException e) {
			//Wenn Server nicht gestartet werden kann, soll die Fehlernachricht ausgegeben werden
			System.err.println(e);
		}
	}
	
	/**
	 * Generiert eine zufällige, 16-Stellen lange Zahlenfolge
	 * @return 16-Stelliger, generierter Schlüssel
	 */
	public String generateKey() {
		String key = "";
		//Zufälligen 16-Stelligen Schlüssel generieren
		for(int i = 0; i <= 15; i++) {
			int randomNumber = (int) (Math.random() * 8);
			key = key + randomNumber;
		}
		return key;
	}
	
	/**
	 * Startet den Server
	 * @throws IOException, wenn nicht vom InputStream gelesen werden kann
	 */
	public void startListening() {
		Socket connection = null;
		PrintWriter out = null;
		BufferedReader in = null;
		String content = "";
		String answer = "FAILED";
		
		while(true) {
			try {
				//Verbindung zum Client herstellen
				connection = server.accept();
				System.out.println("Verbindung zu Client hergestellt!");
				out = new PrintWriter(connection.getOutputStream());
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				
				//Auslesen des vom client geschickten textes
				content = in.readLine();
				
				//Zerlegen des Strings
				String[] contentArray = content.split(" ", 2);
				
				//Wenn der Text in mehr oder weniger als 2 Teile aufgeteilt wird, stimmt etwas mit der Anfrage nicht
				if(contentArray.length != 2) {
					answer = "FAILED - fehlerhafte Struktur";
					out.println(answer);
					out.close();
					return;
				}
				//Starten der Bearbeitung des Befehls
				answer = fileOperation(contentArray);
				out.println(answer);
				out.close();
				
				//Schließen der Verbindung nach jeder ausführung eines Befehls
				if (connection != null) {
					connection.close();
				}
				
			} catch (IOException e) {
				System.err.println(e);	
			}
		}
	}
	
	/**
	 * Nimmt den Befehl, der vom User eingegeben wurde und erstellt die Antwort
	 * @param contentArray Array, der den Befehl und die Nachricht enthält
	 * @return Antwort, die der Client geschickt bekommt
	 * @throws FileNotFoundException Wenn Datei nicht gefunden wird
	 */
	public String fileOperation(String[] contentArray) {
		String answer = "FAILED";
		//Überprüfen, ob SAVE oder GET
		if(contentArray[0].equals("SAVE")) {
			//Schlüssel generieren
			String key = generateKey();
			
		    //Text in Datei mit Namen des Keys speichern
			try {
				//Der Benutzername wird dynamisch erfasst
				String userName = System.getProperty("user.name");
				//Neue File mit dem Namen der "key" Variable .txt
				File file = new File("C:/Users/"+userName+"/Desktop/Messages/"+key+".txt");
				FileWriter fWriter= new FileWriter(file);
				
				//Textinhalt schreiben
				fWriter.write(contentArray[1]);
				fWriter.flush();
				//Schreiben beenden
				fWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//Send key to Client
			answer = ("KEY "+ key);
		} else if(contentArray[0].equals("GET")) {
			String readKey = contentArray[1];
			try {
				//Datei mit dem Namen des Keys lokalisieren
				String userName = System.getProperty("user.name");
		        File myObj = new File("C:/Users/"+userName+"/Desktop/Messages/"+readKey+".txt");
		        
		        //Mithilfe eines Scanners den Dateiinhalt auslesen und in data schreiben.
		        Scanner myReader = new Scanner(myObj);
		        String data = "";
		        while (myReader.hasNextLine()) {
		          data = myReader.nextLine();
		        }
		        myReader.close();
		        
		        //Ausgelesenen String an Client zurückgeben
		        answer = ("OK "+ data);
			} catch (FileNotFoundException e) {
				//Wenn Datei nicht gefunden wird, wird FAILED an den Client zurückgegeben
				answer = "FAILED - Kein gültiger Schlüssel";
		    }
		} else {
			//Wenn ein anderer Befehl als SAVE oder GET mitgegeben wird
			answer = "FAILED - Befehl wurde nicht erkannt";
		}
		return answer;
	}
	
	public static void main(String[] args) {
		int port = DEFAULT_PORT;
		
		if (args.length > 0) {
				port = Integer.parseInt(args[0]);
		}
		//Starten des Servers auf dem Vorgegebenen Port
		Testat2Server messageServer = new Testat2Server(port);
		messageServer.startListening();
	}
}
