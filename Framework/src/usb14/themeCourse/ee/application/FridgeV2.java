package usb14.themeCourse.ee.application;

import java.util.SortedMap;
import java.util.TreeMap;

import usb14.themeCourse.ee.framework.Appliance;
import usb14.themeCourse.ee.framework.Controller;
import usb14.themeCourse.ee.framework.CostFunction;

public class FridgeV2 extends Appliance{
	
	public static final int DEFAULTPRICE = 500;
	public static final int IDLEUSAGE = 0;
	public static final int COOLINGUSAGE = 200;
	public static final double MAXTEMP = 8;
	public static final double MINTEMP = 2;
	
	
	public enum State {
		ON, IDLE
	}
	
	private State state;
	private double temperature;
	private int currentPrice;
	private double slope;

	// Constructor
	
	
	public FridgeV2(String name) {
		super(name);
		this.state = State.IDLE;
		this.temperature = 2.75;
		this.slope = 1;
		
		SortedMap<Integer, Integer> function = new TreeMap<Integer, Integer>();
		function.put(IDLEUSAGE,CostFunction.MAX_COST);
		function.put(COOLINGUSAGE,0);
		super.setCostFunction(new CostFunction(function));
		
		//currentPrice = 500;
	}
	
	
	// Queries
	
	@Override
	public int getCurrentUsage() {
		int result = super.getCostFunction().getDemandByCost(currentPrice);
		System.out.println("Price: "+currentPrice+", Demand: "+result);
		return result;
	}
	
	public State getState() {
		return state;
	}
	
	public double getTemp(){
		return temperature;
	}
	
	
	// Commands
	
	private void updateCostFunction(){
		int cost;
		cost = (int) (slope*COOLINGUSAGE + DEFAULTPRICE);
		cost = cost > CostFunction.MAX_COST?CostFunction.MAX_COST:cost;
		cost = cost < CostFunction.MIN_COST?CostFunction.MIN_COST:cost;
		
		getCostFunction().updateCostForDemand(cost, COOLINGUSAGE);
	}
	
	
	
	@Override
	public void updateState(){
		//int t = Controller.getInstance().getIntervalDuration();
		
		if(this.state == State.ON) temperature -= 0.25;
		else temperature += 0.25;
		
		slope = (temperature-3.0)/4.0;
		System.out.println(slope);
		this.updateCostFunction();		

		this.state = getCurrentUsage()==COOLINGUSAGE?State.ON:State.IDLE;
	}
	
	@Override
	public void updatePrice(int price){
		this.currentPrice = price;
		
		
		setChanged();
		notifyObservers();
	}


	

}
