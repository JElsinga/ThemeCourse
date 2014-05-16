package usb14.themeCourse.ee.framework;

public class Controller extends Thread{

	private static Controller instance;
	private Controllable controllable;
	
	// Singleton
	private Controller(Controllable controllable) {
		this.controllable = controllable;
	}
	
	/**
	 * Initialises the Controller with the given parameters. Should be
	 * called before calling getInstance(). Does nothing if this method
	 * was already called once.
	 * @param controllable	The Controllable that should be controlled by
	 * 						this controller.
	 */
	public static void intialise(Controllable controllable){
		if (instance == null)
			instance = new Controller(controllable);
	}
	
	/**
	 * Returns the Controller. Should be called after an initialise() call.
	 * @return null if called before initialise(), a Controller otherwise.
	 */
	public static Controller getInstance() {
		return instance;
	}
	
	// Commands
	
	public void run() {
		controllable.updateStatus(1000.0);
	}
	
	
}
