package usb14.themeCourse.ee.application;

import java.util.*;

import usb14.themeCourse.ee.framework.Appliance;
import usb14.themeCourse.ee.framework.Controller;
import usb14.themeCourse.ee.framework.CostFunction;

public class Battery2 extends Appliance{
	
	public static final int DEFAULTPRICE = 1000;
	
	public enum State {
		CHARGING, DISCHARGING, IDLE
	}
	private int charge;
	private int maxCharge;
	private State state;
	private int currentPrice;
	private double slope;
	private int idler;
	private int loader;
	
	public Battery2(String name) {
		super(name);
		this.charge = 0;
		this.maxCharge = 1000;
		this.slope = 2;
		this.idler = 0;
		
		SortedMap<Integer,Integer> function = new TreeMap<Integer,Integer>();
		updateCostFunction();
		
		//currentPrice = 500;
	}
	
	public State getState(){
		return this.state;
	}
	
	public int getCharge(){
		return this.charge;
	}
	
	public int getMaxCharage(){
		return this.maxCharge;
	}
	
	
	private void updateCostFunction(){
		int cost;
		SortedMap<Integer,Integer> newFunction = new TreeMap<Integer,Integer>();
		for(int demand=-maxCharge;demand<=maxCharge;demand+=10){
			if(0 <= charge+demand && charge+demand <= maxCharge){
				/*
				 * How the CostFunction will look:
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
				cost = (int) (((double)CostFunction.MAX_COST * 1.5) - ((double)(demand+maxCharge)/(double)(2*maxCharge))*(double)CostFunction.MAX_COST - ((double)charge/(double)maxCharge)*((double)CostFunction.MAX_COST/2.0));
				newFunction.put(demand, (int)(cost));
			}
		}

		setCostFunction(new CostFunction(removeInvalidCosts(newFunction)));
		System.out.println("Battery2: "+getCostFunction());
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
		idler = usage == 0?idler+1:0;
		loader = usage > 0 && loader < 1?loader+1:0;
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
