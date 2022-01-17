package PhilosophenMonitor;

public class Philosoph extends Thread {
	int id;
	Monitor monitor;
	
	public Philosoph(int id, Monitor m) {
		this.id = id;
		this.monitor = m;
	}
	
	public void run() {
		try {
			sleep((long)(Math.random()*1000));
			monitor.startEating(id);
			sleep((long)(Math.random()*1000));
			monitor.stopEating(id);
		} catch (InterruptedException e) {}
	}
	
	
}
