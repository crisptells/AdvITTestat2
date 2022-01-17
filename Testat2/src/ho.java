package src;


import java.lang.Math;
public class ho {
	
	public static void main(String args[])
    {
		String key = "";
		//Zufälligen Schlüssel generieren
		for(int i = 0; i <= 16; i++) {
			int randomNumber = (int)(Math.random() * 8);
			System.out.println(randomNumber);
			key = key + randomNumber;
		}
		System.out.println("key: " + key);
}
}
