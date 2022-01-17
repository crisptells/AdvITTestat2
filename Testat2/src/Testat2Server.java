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
			server = new ServerSocket(port);
			System.out.println("Server started on port: " + port);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public String generateKey() {
		String key = "";
		//Zufälligen Schlüssel generieren
		for(int i = 0; i <= 16; i++) {
			int randomNumber = (int) (Math.random() * 8);
			key = key + randomNumber;
		}
		System.out.println("key: " + key);
		return key;
	}
	
	public void startListening() {
		Socket connection = null;
		PrintWriter out = null;
		BufferedReader in = null;
		String content = "";
		String answer = "";
		
		while(true) {
			try {
				//Verbindung zum Client herstellen
				connection = server.accept();
				System.out.println("Verbindung zu Client hergestellt!");
				out = new PrintWriter(connection.getOutputStream());
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				
				//Auslesen des vom client geschickten textes
				content = in.readLine();
				System.out.println("Message: " + content);
				
				//Zerlegen des Strings
				String[] contentArray = content.split(" ", 2);
				System.out.println("Befehl: "+contentArray[0]);
				System.out.println("Text: "+contentArray[1]);
				
				if(contentArray.length != 2) {
					answer = "FAILED - wrong command structure";
					out.println(answer);
					out.close();
					return;
				}
				
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
	
	public String fileOperation(String[] contentArray) {
		//Überprüfen, ob SAVE oder GET
		if(contentArray[0].equals("SAVE")) {
			System.out.println("Befehl wurde als SAVE erkannt");
			//Schlüssel generieren
			String key = generateKey();
			
		    //Text in Datei mit Namen des Keys speichern
			try {
				File file = new File("C:/Users/Luis/Desktop/Messages/"+key+".txt");
				FileWriter fWriter= new FileWriter(file);
				fWriter.write(contentArray[1]);
				fWriter.flush();
				fWriter.close();
				System.out.println("sucess");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//Send key to Client
			return("KEY "+ key);
		} else if(contentArray[0].equals("GET")) {
			System.out.println("Befehl wurde als GET erkannt");
			String readKey = contentArray[1];
			try {
				//Datei mit dem Namen des Keys lokalisieren
		        File myObj = new File("C:/Users/Luis/Desktop/Messages/"+readKey+".txt");
		        
		        //Mithilfe eines Scanners den Dateiinhalt auslesen und in data schreiben.
		        Scanner myReader = new Scanner(myObj);
		        String data = "";
		        while (myReader.hasNextLine()) {
		          data = myReader.nextLine();
		        }
		        myReader.close();
		        
		        //Ausgelesenen String an Client zurückgeben
		        return ("OK "+ data);
			} catch (FileNotFoundException e) {
				//Wenn Datei nicht gefunden wird, wird FAILED an den Client zurückgegeben
				return "FAILED";
		    }
		} 
		return "FAILED - Befehl wurde nicht erkannt";
	}
	
	public static void main(String[] args) {
		int port = DEFAULT_PORT;
		
		if (args.length > 0) {
				port = Integer.parseInt(args[0]);
		}
		
		Testat2Server messageServer = new Testat2Server(port);
		messageServer.startListening();
	}
}