package usb14.themeCourse.ee.application;

import java.util.*;

import usb14.themeCourse.ee.framework.Appliance;
import usb14.themeCourse.ee.framework.CostFunction;

public class Battery extends Appliance{
	public enum State {
		CHARGING, DISCHARGING, IDLE
	}
	
	private int load;
	private int maxLoad;
	private State state;
	private int currentPrice;
	public CostFunction charge;
	public CostFunction discharge;
	private double slope;
	private int idler;
	
	public Battery(String name) {
		super(name);
		this.state = state.IDLE;
		this.load = 0;
		this.maxLoad = 1000;
		this.slope = 1;
		this.idler = 0;
		
		SortedMap<Integer,Integer> function = new TreeMap<Integer,Integer>();
		super.setCostFunction(new CostFunction(function));
		updateCostFunction();
		
		currentPrice = 1000;
	}
	
	public State getState(){
		return this.state;
	}
	
	public int getLoad(){
		return this.load;
	}
	
	public int getMaxLoad(){
		return this.maxLoad;
	}
	
	
	private void updateCostFunction(){
		int cost;
		for(int demand=-maxLoad;demand<maxLoad;demand+=100){
			//Everything before the costfunction (cannot give what you dont have)
			if(load+demand < 0)
				getCostFunction().deleteCostForDemand(demand);
			//Everything after the costfunction (cannot charge if full)
			else if(load+demand > maxLoad)
				getCostFunction().deleteCostForDemand(demand);
			//Everything in the costfunction (can charge/discharge so much)
			else{
				cost = (int) (-slope*demand+(1100-(load/2)));
				getCostFunction().updateCostForDemand(cost, demand);
			}
		}
		
	}
	
	@Override
	public void updatePrice(int price) {
			this.currentPrice = price;
			this.setChanged();
			this.notifyObservers();
	}

	@Override
	public void updateState() {
		//int t = Controller.getInstance().getIntervalDuration();

		this.updateCostFunction();
		int usage = getCurrentUsage();
		idler = usage == 0?idler+1:idler;
		load += usage;
		
	}

	@Override
	public int getCurrentUsage() {
		//System.out.println(getCostFunction());
		int demand = getCostFunction().getDemandByCost(currentPrice);
		//System.out.println("Price: "+currentPrice+" Demand: "+demand);
		return demand;
	}

}
