package usb14.themeCourse.ee.framework;

import java.util.SortedMap;
import java.util.TreeMap;

public class Fridge extends Appliance{
	public enum State {
		ON, OFF
	}
	
	private State state;
	private double temp;
	private int time;
	private double currentPrice;
	private final double usage = 283.5;
	private final double maxCost = 1500;

	// Constructor
	
	
	public Fridge(String name) {
		super(name);
		this.state = State.OFF;
		this.temp = 2;
		this.time = 0;
		this.currentPrice = 0;
		
		SortedMap<Double, Double> function = new TreeMap<Double, Double>();
		function.put(usage, 0.0);	
		super.setCostFunction(new CostFunction(function));
	}
	
	
	// Queries
	
	@Override
	public double getCurrentUsage() {
		double result = super.getCostFunction().getDemandByCost(currentPrice);
		//System.out.println("Current CostFunction Fridge: "+super.getCostFunction());
		//System.out.println("Current Price Fridge = "+currentPrice);
		//System.out.print("Current Usage Fridge = "+result+"\t");
		return result;
		
	}
	
	public State getState() {
		return state;
	}
	
	public double getTemp(){
		return temp;
	}
	
	
	// Commands
	
	private void updateCostFunction(){
		double cost = 1000;
		if(time > 0 && time < 20) cost = maxCost;
		else if(temp >= 7.5){
			cost = maxCost;
		} else if(temp < 2.5){ 
			cost = 0;
		} else{
			cost = Math.round((maxCost+2000)/(8-temp));
		}
		super.getCostFunction().updateCostForDemand(cost, usage);
	}
	
	
	
	@Override
	public void updateState(){
		int t = Controller.getInstance().getIntervalDuration();
		
		// update temperature and time running
		if(this.state == State.ON){
			temp -= 0.05*t;
			this.time += t;
		} else {
			temp += 0.025*t;
			this.time = 0;
		}
		
		// update costfunction based on the new temperature and time running
		this.updateCostFunction();		
	}
	
	@Override
	public void updatePrice(double price){
		this.currentPrice = price;
		
		// update state based on the current price
		double cur = getCurrentUsage();
		if(cur == 0){
			this.state = State.OFF;
		} else {
			this.state = State.ON;
		}
		setChanged();
		notifyObservers();
	}


	

}
