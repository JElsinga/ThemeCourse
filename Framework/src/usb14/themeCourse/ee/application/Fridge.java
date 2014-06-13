package usb14.themeCourse.ee.application;

import java.util.SortedMap;
import java.util.TreeMap;

import usb14.themeCourse.ee.framework.Appliance;
import usb14.themeCourse.ee.framework.Controller;
import usb14.themeCourse.ee.framework.CostFunction;

public class Fridge extends Appliance{
	public enum State {
		ON, OFF
	}
	
	private State state;
	private double temp;
	private int time;
	private int currentPrice;
	private final int usage = 280;
	private final int maxCost = CostFunction.MAX_COST;

	// Constructor
	
	
	public Fridge(String name) {
		super(name);
		this.state = State.OFF;
		this.temp = 4;
		this.time = 0;
		
		SortedMap<Integer, Integer> function = new TreeMap<Integer, Integer>();
		function.put(0, CostFunction.MAX_COST);
		function.put(usage, 0);	
		super.setCostFunction(new CostFunction(function));
	}
	
	
	// Queries
	
	@Override
	public int getCurrentUsage() {
		return super.getCostFunction().getDemandByCost(currentPrice);
	}
	
	public State getState() {
		return state;
	}
	
	public double getTemp(){
		return temp;
	}
	
	
	// Commands
	
	private void updateCostFunction(){
		int cost;
		
		if(time > 0 && time < 20) 
			cost = maxCost;
		else
			// Met deze functie zou de temperatuur tussen 3 en 7 graden moeten schommelen.
			// Bij een prijs van 500 blijkt het tussen de 4 en 5 te blijven, dus er is wat 
			// marge om warmer of kouder te worden bij een hogere of lagere energieprijs.
			cost = (int) Math.round(((temp-3.0)/4.0) * (float)maxCost);
		if (cost < 0 ) cost = 0;
		
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
	public void updatePrice(int price){
		this.currentPrice = price;
		
		// update state based on the current price
		if(getCurrentUsage() == 0)
			this.state = State.OFF;
		else
			this.state = State.ON;
		
		setChanged();
		notifyObservers();
	}


	

}
