package usb14.themeCourse.ee.framework;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

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
		int runTime = 50;
		int interval = 10;
		Double price = 1000.0; 
		Double usage = 0.0; 
		Double totalPrice = 0.0;
		String output = "output.txt";
		int timeCounter = 0;
		PrintWriter writer;
		try {
			writer = new PrintWriter("output.txt");
			Fridge fridge = ((Fridge)controllable);
			writer.println("State update for controllable: "+fridge.getName());
			writer.println("Time \t State \t Temp \t Usage");
			for(int i=0;i<runTime;i++){
				controllable.updatePrice(price);
				writer.println(timeCounter+"\t\t "+fridge.getState()+"\t\t "+fridge.getTemp()+"\t"+fridge.getCurrentUsage());
				
				controllable.updateState(interval);
				usage += controllable.getCurrentUsage();
				if(controllable.getCurrentUsage()!=0) totalPrice += price;
			timeCounter+=interval;
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("The total runtime was: "+runTime*60+" minutes with "+runTime+" intervals of "+interval+" minutes.");
		System.out.println("\t\t The total usage was: "+usage+" kilowatts");
		System.out.println("\t\t The total cost was:  "+totalPrice+" units");
		System.out.println("The state transition data can be found in "+output);
	}
	
	
	
	
	public static void main(String args[]){
		Fridge koelkast = new Fridge("koelkast1");
		Controller controller = new Controller(koelkast);
		controller.start();
	}
	
	
}
