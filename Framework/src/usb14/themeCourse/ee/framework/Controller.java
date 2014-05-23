package usb14.themeCourse.ee.framework;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Controller extends Thread{

	private static Controller instance;
	private Controllable controllable;
	
	
	// Constructor - Singleton
	
	
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
		while(true){
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			controllable.updateState(10);
			controllable.updatePrice(1000.0);
			System.out.println("current usage: " + controllable.getCurrentUsage() + "\t " + ((Fridge)controllable).getState() + "\t temperatuur: " + ((Fridge)controllable).getTemp());
		}
	}
	
	
	
	
	public static void main(String args[]){
		Fridge koelkast = new Fridge("koelkast1");
		Controller controller = new Controller(koelkast);
		try {
			PrintWriter writer = new PrintWriter("output.txt");
			controller.start();
		} catch (FileNotFoundException e) {
			System.out.println("Aardbei");
			e.printStackTrace();
		}
	}
	
	
}
