package PhilosophenMonitor;

public class Main {
	public static void main(String[] args) {
		int counter = 5;
		Monitor m = new Monitor(counter);
		Philosoph[] philosophen = new Philosoph[counter];
		
		for(int i = 0; i < counter; i++) {
			philosophen[i] = new Philosoph(i, m);
			philosophen[i].start();
		}
		
		for(int i = 0; i < counter; i++) {
			try {
				philosophen[i].join();
			} catch (InterruptedException e) {}
		}
		
		System.out.println("Alle satt :P");
		
		
	}
}
