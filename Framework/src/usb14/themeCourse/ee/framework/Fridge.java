package usb14.themeCourse.ee.framework;

import java.util.SortedMap;
import java.util.TreeMap;

public class Fridge extends Appliance{
	public static final int ON = 1;
	public static final int OFF = 0;
	
	
	private int state;
	private double temp;
	private int time;
	private double currentPrice;
	private final double usage = 283.5;
	private final double maxCost = 1500;

	// Constructor
	
	
	public Fridge(String name) {
		super(name);
		this.state = OFF;
		this.temp = 2;
		this.time = 0;
		this.currentPrice = 0;
		
		SortedMap<Double, Double> function = new TreeMap<Double, Double>();
		function.put(usage, 0.0);	
		CostFunction result = new CostFunction(function);
		super.setCostFunction(result);

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
	
	public int getState() {
		return state;
	}
	
	public double getTemp(){
		return temp;
	}
	
	
	// Commands
	
	public void setCostFunction(){
		double cost = 1000;
		if(time > 0 && time < 20) cost = maxCost;
		else if(temp>=7.5){
			cost = maxCost;
		}else if(temp < 2.5){ 
			cost = 0;
		}else{
			cost = Math.round((maxCost+2000)/(8-temp));
		}
		CostFunction result = super.getCostFunction();
		result.updateCostForDemand(cost, usage);
		super.setCostFunction(result);
	}
	
	
	
	@Override
	public void updateState(int t){

		//update costfunction
		this.setCostFunction();
		
		//update temp and time
		if(this.state == ON){
			temp -= 0.05*t;
			this.time += t;
		} else {
			temp += 0.025*t;
			this.time = 0;
		}
		
		//update state
		Double cur = getCurrentUsage();
		if(cur == 0){
			this.state = OFF;
		} else {
			this.state = ON;
		}
		
	}
	
	@Override
	public void updatePrice(double price){
		this.currentPrice = price;
	}


	

}
