package Testat2;

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
			BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
			
			while(true) {
				String theLine = userIn.readLine();
				if (theLine.equals(".")) break;
				
				s = new Socket(hostname, serverPort);

				networkIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
				networkOut = new PrintWriter(s.getOutputStream());
				
				networkOut.println(theLine);
				networkOut.flush();
				System.out.println(networkIn.readLine());
				
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
