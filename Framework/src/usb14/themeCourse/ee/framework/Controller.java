package usb14.themeCourse.ee.framework;

public class Controller extends Thread{

	private static Controller instance;
	private Controllable controllable;
	private int time;
	private int interval;
	
	// Constructor - Singleton
	
	
	private Controller(Controllable controllable, int interval) {
		this.controllable = controllable;
		this.interval = interval;
	}
	
	/**
	 * Initialises the Controller with the given parameters. Should be
	 * called before calling getInstance(). Does nothing if this method
	 * was already called once.
	 * @param controllable	The Controllable that should be controlled by
	 * 						this controller.
	 * @param interval		How long the Controller should wait between
	 * 						each update.
	 */
	public static void initialise(Controllable controllable, int interval){
		if (instance == null)
			instance = new Controller(controllable, interval);
	}
	
	/**
	 * Returns the Controller. Should be called after an initialise() call.
	 * @return null if called before initialise(), a Controller otherwise.
	 */
	public static Controller getInstance() {
		return instance;
	}
	
	/**
	 * Returns how much time passes during one interval.
	 * @return an integer representing the time during one interval
	 */
	public int getIntervalDuration() {
		return interval;
	}
	
	/**
	 * Returns the total ellapsed time.
	 * @return an integer representing the total ellapsed time.
	 */
	public int getTime() {
		return time;
	}
	
	// Commands
	
	public void run() {
		double difference = 0.0;
		int price = 500;
		
		while(this.time < 1440) {
			int demandGoal = 100;
			
			price = controllable.getCostFunction().getCostByDemand(demandGoal);
			while(price == 0){
				demandGoal += 50;
				price = controllable.getCostFunction().getCostByDemand(demandGoal);
			}
			System.out.println(time + ": " + controllable.getCostFunction().toString());
			controllable.updatePrice(price);
			difference += Math.abs(controllable.getCurrentUsage() - demandGoal);
			
			try {
				sleep(10);
			} catch (InterruptedException e) {}
			
			controllable.updateState();
			this.time+=interval;
		}
		System.out.println("Total Difference: "+difference);
	}
	
	
}
