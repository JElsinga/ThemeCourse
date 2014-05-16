package usb14.themeCourse.ee.framework;

import java.util.Observable;

public abstract class Appliance extends Observable implements Controllable {
	private final String name;
	private CostFunction costFunction;
	private double currentPrice;
	
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
	
	@Override
	public double getCurrentUsage(){
		return this.costFunction.getDemandByCost(currentPrice);
	}
	
	// Commands
	
	
	/**
	 * setCostFunction sets the costFunction of the appliance
	 * @param costFunction - The cost function for this appliance in its current state
	 * @requires costFunction != null
	 * @ensures this.costFunction = costFunction
	 */
	protected void setCostFunction(CostFunction costFunction){
		this.costFunction = costFunction;
		notifyObservers();
	}
	
	@Override
	public void updateStatus(double price){
		this.currentPrice = price;
		notifyObservers();
	}
}
