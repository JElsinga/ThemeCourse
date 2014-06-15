package usb14.themeCourse.ee.application;

import java.util.*;

import usb14.themeCourse.ee.framework.Appliance;
import usb14.themeCourse.ee.framework.Controller;
import usb14.themeCourse.ee.framework.CostFunction;

public class Battery extends Appliance{
	
	public static final int DEFAULTPRICE = 1000;
	public static final int IDLEMULTIPLIER = 500;
	public static final int LOADMULTIPLIER = 500;
	
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
	
	public Battery(String name) {
		super(name);
		this.charge = 0;
		this.maxCharge = 1000;
		this.slope = 2;
		this.idler = 0;
		
		SortedMap<Integer,Integer> function = new TreeMap<Integer,Integer>();
		//Set everything to zero at the start.
		for(int i=-maxCharge;i<=maxCharge;i+=100) function.put(0, i);
		super.setCostFunction(new CostFunction(function));
		setState(getCurrentUsage());
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
		for(int demand=-maxCharge;demand<=maxCharge;demand+=100){
			if(0 <= charge+demand && charge+demand <= maxCharge){
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
				cost = (int) (-slope*demand+DEFAULTPRICE-(charge)+(loader*LOADMULTIPLIER)+(idler*IDLEMULTIPLIER));
				//These are checks to ensure that the cost per demand does not go below or above MIN_COST or MAX_COST respectively.
				//System.out.println("Adding: Demand: "+demand+", Cost: "+cost);
				newFunction.put(demand, cost);
				
			}
		}
		SortedMap<Integer, Integer> newMap = mapToV2(newFunction);
		//setCostFunction(new CostFunction(mapTo(newFunction)));
		setCostFunction(new CostFunction(newMap));
		//System.out.println("Load: "+charge);
		//System.out.println("Battery: "+getCostFunction());
	}
	
	private SortedMap<Integer, Integer> mapToV2(SortedMap<Integer, Integer> function){
		SortedMap<Integer, Integer> newMap = new TreeMap<Integer, Integer>();
		Iterator it = function.keySet().iterator();
		boolean below = false;
		int previous = 0;
		while(it.hasNext() & !below){
			int demand = (int) it.next();
			int cost = function.get(demand);
			//System.out.println("From - Demand: "+demand+" Cost: "+cost);
			if(cost >= CostFunction.MAX_COST && function.get(previous) >= CostFunction.MAX_COST){
				newMap.remove(previous);
				newMap.put(demand, CostFunction.MAX_COST);
			}else if(cost >= CostFunction.MAX_COST){
				newMap.put(demand, CostFunction.MAX_COST);
			}else if(cost <= CostFunction.MIN_COST & !below){
				newMap.put(demand, CostFunction.MIN_COST);
				below = true;
			}else if(!below){
				newMap.put(demand,cost);
			}
			previous = demand;
		}
		for(int dem : newMap.keySet()){
			//System.out.println("To - Demand: "+dem+" Cost: "+newMap.get(dem));
		}
		return newMap;
	}
	
	private SortedMap<Integer, Integer> mapTo(SortedMap<Integer, Integer> function){
		SortedMap<Integer, Integer> newMap = new TreeMap<Integer, Integer>();
		int lowest = function.get(function.lastKey());
		for (int demand : function.keySet()){
			newMap.put(demand, function.get(demand) - lowest);
		}
		double fraction = (double)(CostFunction.MAX_COST) / (double)newMap.get(newMap.firstKey());
		for (int demand : function.keySet()){
			newMap.put(demand, (int)Math.round((double)newMap.get(demand) * fraction));
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
		try{
			t = Controller.getInstance().getIntervalDuration();
		}catch(NullPointerException e){
			t = 60;
		}
		
		int usage = getCurrentUsage();
		idler = usage == 0?idler+1:0;
		loader = usage > 0 && loader < 1?loader+1:0;
		charge += (int) Math.round(((double)usage/60.0)*(double)t);
		setState(usage);
		
		this.updateCostFunction();
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
