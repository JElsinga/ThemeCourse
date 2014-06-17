package usb14.themeCourse.ee.application;

import java.util.*;

import usb14.themeCourse.ee.framework.Appliance;
import usb14.themeCourse.ee.framework.Controller;
import usb14.themeCourse.ee.framework.CostFunction;

public class Battery extends Appliance{
	
	public enum State {
		CHARGING, DISCHARGING, IDLE
	}
	private int charge;
	private int maxCharge;
	private State state;
	private int currentPrice;
	
	public Battery(String name) {
		super(name);
		this.charge = 0;
		this.maxCharge = 1000;
		
		updateCostFunction();
	}
	
	public State getState(){
		return this.state;
	}
	
	public int getCharge(){
		return this.charge;
	}
	
	public int getMaxCharge(){
		return this.maxCharge;
	}
	
	
	private void updateCostFunction(){
		SortedMap<Integer,Integer> newFunction = new TreeMap<Integer,Integer>();
		for(int demand=-maxCharge;demand<=maxCharge;demand+=10){
			if(0 <= charge+demand && charge+demand <= maxCharge){
				/*
				 * What the CostFunction will look like:
				 * 
				 * 0% CHARGE:			50% CHARGE:			100% Charge:
				 *          x         	x        |         	         |         
				 *          |  x      	   x     |         	         |         
				 *          |     x   	      x  |         	         |         
				 *          |        x	         x         	x        |         
				 *          |         	         |  x      	   x     |         
				 *          |         	         |     x   	      x  |         
				 * _________|_________	_________|________x	_________x________			
				 */
				
				int cost = (int)(((double)CostFunction.MAX_COST * 1.5) - ((double)(demand+maxCharge)/(double)(2*maxCharge))*(double)CostFunction.MAX_COST - ((double)charge/(double)maxCharge)*((double)CostFunction.MAX_COST/2.0));
				newFunction.put(demand, cost);
			}
		}
		setCostFunction(new CostFunction(removeInvalidCosts(newFunction)));
	}
	
	/**
	 * Returns a new map representing a CostFunction based on a given map. Deletes any demand
	 * that has a price lower than CostFunction.MIN_COST or higher than CostFunction.MAX_COST
	 * @param function	The function to clean up
	 * @return	The new function
	 */
	private SortedMap<Integer, Integer> removeInvalidCosts(SortedMap<Integer, Integer> function){
		SortedMap<Integer, Integer> newMap = new TreeMap<Integer, Integer>();
		for(int demand : function.keySet()){
			if (function.get(demand) >= CostFunction.MIN_COST 
					&& function.get(demand) <= CostFunction.MAX_COST)
				newMap.put(demand, function.get(demand));
		}
		return newMap;
	}
	
	@Override
	public void updatePrice(int price) {
			this.currentPrice = price;
			this.setChanged();
			this.notifyObservers();
	}

	@Override
	public void updateState() {
		int t;
		t = Controller.getInstance().getIntervalDuration();

		int usage = getCurrentUsage();
		charge += (int) Math.round(((double)usage/60.0)*(double)t);
		setState(usage);
		
		this.updateCostFunction();
	}

	private void setState(int usage){
		if(usage>0) state = State.CHARGING;
		else if(usage<0) state = State.DISCHARGING;
		else state = State.IDLE;
	}
	
	@Override
	public int getCurrentUsage() {
		return getCostFunction().getDemandByCost(currentPrice);
	}

}
