package usb14.themeCourse.ee.application;

import java.util.*;

import usb14.themeCourse.ee.framework.Appliance;
import usb14.themeCourse.ee.framework.Controller;
import usb14.themeCourse.ee.framework.CostFunction;

public class Battery extends Appliance{
	
	public static final int DEFAULTPRICE = 600;
	public static final int IDLEMULTIPLIER = 100;
	public static final int LOADMULTIPLIER = 100;
	
	public enum State {
		CHARGING, DISCHARGING, IDLE
	}
	private int load;
	private int maxLoad;
	private State state;
	private int currentPrice;
	private double slope;
	private int idler;
	private int loader;
	
	public Battery(String name) {
		super(name);
		this.load = 0;
		this.maxLoad = 1000;
		this.slope = 1;
		this.idler = 0;
		
		SortedMap<Integer,Integer> function = new TreeMap<Integer,Integer>();
		//Set everything to zero at the start.
		for(int i=-maxLoad;i<=maxLoad;i+=100) function.put(0, i);
		super.setCostFunction(new CostFunction(function));
		setState(getCurrentUsage());
		updateCostFunction();
		
		//currentPrice = 500;
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
		for(int demand=-maxLoad;demand<=maxLoad;demand+=100){
			//Everything before the cost function (cannot give what you dont have)
			if(load+demand < 0)
				getCostFunction().deleteCostForDemand(demand);
			//Everything after the cost function (cannot charge if full)
			else if(load+demand > maxLoad)
				getCostFunction().deleteCostForDemand(demand);
			//Don't allow the battery to actively do nothing (this stops unwanted idling)
			else if(demand == 0)
				getCostFunction().deleteCostForDemand(0);
			//Everything in the cost function (can charge/discharge so much)
			else{
				/**
				 * This is the cost function of battery.
				 * It is a linear strictly declining line in the form y = -ax+b
				 * where y is cost, and x is demand.
				 * the slope 'a' is negative to ensure decline and currently is set to -1 (in other words has on influence on the function)
				 * 		The slope could be used to ensure loading loops are kept to a minimum
				 * the intercept 'b' is a bit more complicated and is built up as follows:
				 * 		b = DEFAULTPRICE - load + (idler * IDLEMULTIPLIER)
				 * EFFECTS:
				 * 	In general -	By changing 'b' we can raise or lower the cost per demand.
				 * 					By raising 'b' we raise the cost meaning the battery will be willing to pay more.
				 * 					By decreasing 'b' we lower the cost meaning the battery will not be willing to pay more.		
				 *	var: DEFAULTPRICE -	This is the set price at which we would want to start loading the battery.
				 *						CurrentPrice - DEFAULTPRICE = stable load of the battery (load it will idle at).
				 *	var: load -			This is the load of the current battery.
				 *						Multiplying load increase the time before discharge (unknown reason)
				 *  var: loader -		This is a counter which counts how long the battery has been loading.
				 *  var: LOADMULTIPLIER-=====================================================================
				 *	var: idler -		This is a counter which counts how long the battery had been idle.
				 *  var: IDLEMULTIPLIER-This is a multiplier which is used to drive cost per demand when the battery has been idle.
				 *  					By increasing the multiplier the battery will be more likely to charge instead of being idle.
				 *  					For this battery the multiplier had a direct correlation to how much will be charged after being idle.
				 *						
				 */
				cost = (int) (-slope*demand+DEFAULTPRICE-(load)+(loader*LOADMULTIPLIER)+(idler*IDLEMULTIPLIER));
				//These are checks to ensure that the cost per demand dus not go below or above MIN_COST or MAX_COST respectively.
				cost = cost > CostFunction.MAX_COST?CostFunction.MAX_COST:cost;
				cost = cost < CostFunction.MIN_COST?CostFunction.MIN_COST:cost;
				
				getCostFunction().updateCostForDemand(cost, demand);
				
				//This is needed for the class Tester.java (otherwise can be removed).
				//currentPrice = 500;
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
		int t;
		try{
			t = Controller.getInstance().getIntervalDuration();
		}catch(NullPointerException e){
			t = 60;
		}
		this.updateCostFunction();
		int usage = getCurrentUsage();
		idler = usage == 0?idler+1:0;
		loader = usage > 0 && loader < 1?loader+1:0;
		load += (usage/60)*t;
		setState(usage);
		//slope = slope==1?0.2:slope;
		
	}

	private void setState(int usage){
		if(usage>0) state = State.CHARGING;
		else if(usage<0) state = State.DISCHARGING;
		else state = State.IDLE;
	}
	
	@Override
	public int getCurrentUsage() {
		//System.out.println(getCostFunction());
		int demand = getCostFunction().getDemandByCost(currentPrice);
		//System.out.println("Price: "+currentPrice+" Demand: "+demand);
		return demand;
	}

}
