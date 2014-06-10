package usb14.themeCourse.ee.framework;

import java.util.*;

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
	private double slopeDischarge = -0.0015;
	private double slopeCharge = -1.9;
	private double power = 2.9;
	
	public Battery(String name) {
		super(name);
		this.state = state.IDLE;
		this.load = 0;
		this.maxLoad = 1000;
		
		
		SortedMap<Integer, Integer> function = new TreeMap<Integer, Integer>();
		SortedMap<Integer, Integer> chargeFunc = new TreeMap<Integer, Integer>();
		SortedMap<Integer, Integer> dischargeFunc = new TreeMap<Integer, Integer>();
		int i = -maxLoad;
		while(i<=maxLoad){
			if(i < 0)	dischargeFunc.put(i, currentPrice);
			else if( i > 0) chargeFunc.put(i, currentPrice);
			function.put(i, currentPrice);
			i+=100;
		}
		charge = new CostFunction(chargeFunc);
		discharge = new CostFunction(dischargeFunc);
		super.setCostFunction(new CostFunction(function));
		this.currentPrice = 1000;
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
	
	private void updatePartFunctions(CostFunction zero, CostFunction update){

	}
	
	private void updateCostFunction(){
		int cost;
		//Cost for demand zero is always zero
		//Update for discharge cost function
		for(int demand : discharge.getDemands()){
			cost = (int) Math.abs((slopeDischarge*Math.pow(Math.abs(demand),power)));// - slope*maxLoad + 50);
			cost = load+demand < 0 ? 0 : cost;
			//System.out.println("Updateing discharge, demand: "+demand+" cost: "+cost+" load+demand: "+(load+demand));
			discharge.updateCostForDemand(cost, demand);
			super.getCostFunction().updateCostForDemand(cost, demand);
		}
		
		//Update for charge cost function
		for(int demand : discharge.getDemands()){
			cost = (int) (slopeCharge*demand - slopeCharge*maxLoad + 100);
			cost = cost > 0 ? cost : 0;
			cost = load+Math.abs(demand) > maxLoad ? 0 : cost;
			//System.out.println("Updating charge, demad: "+demand+" cost: "+cost);
			charge.updateCostForDemand(cost, demand);
			super.getCostFunction().updateCostForDemand(cost, demand);
		}
		/**
		while(usage <= maxLoad){
			int low = load+Math.abs(usage);
			System.out.println("load: "+low);
			if(load+Math.abs(usage) > maxLoad){
				cost = 0;
			}else{
				cost = (maxLoad-load)*(1-(load/maxLoad));
			}
			super.getCostFunction().updateCostForDemand(cost,usage);
			usage+=100;
		}
		*/
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
		//System.out.println(this.getCostFunction());
		//System.out.println(this.charge);
		//System.out.println("Load: "+load+" current usage: "+this.getCurrentUsage());
		load += getCurrentUsage();
		power = load>0?3 - (double)load/(double)1000:2;
		slopeCharge = -1 - (double)load/(double)1000;
		//System.out.println("power: "+power+", slope: "+slopeCharge);
		//power = load>0?(2+(load/1000)):2;
		//System.out.println("Power: "+power);
		//System.out.println("Load: "+load);
		//System.out.println("Cost Function: "+this.getCostFunction());
		
	}

	@Override
	public int getCurrentUsage() {
		//System.out.println("Current Price: "+currentPrice);
		int demandC = this.charge.getDemandByCost(currentPrice);
		int demandD = this.discharge.getDemandByCost(currentPrice);
		//System.out.println("Demand charge: "+demandC);
		//System.out.println("Demand discharge: "+demandD);
		int demand = demandC > Math.abs(demandD) ? demandC : demandD;
		//System.out.println("Demand: "+demand);
		return demand;
	}

}
