package usb14.themeCourse.ee.framework;

import java.util.Observable;

public abstract class Appliance extends Observable implements Controllable {
	private final String name;
	private CostFunction costFunction;
	
	
	// Constructor
	
	
	/**
	 * Constructor of the abstract class Appliance
	 * @param name - Name of the appliance
	 * @requires name != null
	 * @ensures this.name = name
	 */
	public Appliance(String name) {
		this.name = name;
	}
	
	
	// Queries
	
	
	/**
	 * Function the get the name the appliance
	 * @return this.name
	 */
	public String getName(){
		return this.name;
	}
	
	@Override
	public CostFunction getCostFunction(){
		return this.costFunction;
	}
	
		
	// Commands
	
	
	/**
	 * Sets the costFunction of the appliance
	 * @param costFunction - The cost function for this appliance in its current state
	 * @requires costFunction != null
	 * @ensures this.costFunction = costFunction
	 */
	protected void setCostFunction(CostFunction costFunction){
		this.costFunction = costFunction;
	}

}
