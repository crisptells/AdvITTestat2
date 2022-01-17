package PhilosophenMonitor;

public class Monitor {
	private boolean[] eating;
	private int counter;
	
	public Monitor(int counter) {
		this.counter = counter;
		this.eating = new boolean[counter];
		for(int i = 0; i<counter; i++) {
			eating[i] = false;
		}
	}
	
	public synchronized void startEating(int id) {
		while(eating[(id+counter-1) % counter] || eating[(id+1) % counter]) {
			System.out.println(id + " will essen, aber kann nicht :(");
			try {
				wait();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		eating[id] = true;
		System.out.println(id + " isst jetzt was :)");
	}

	public synchronized void stopEating(int id) {
		eating[id] = false;
		System.out.println(id + " hört auf zu essen und fängt an zu denken");
		notifyAll();
	}
}
