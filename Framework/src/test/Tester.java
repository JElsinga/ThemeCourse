package test;

import usb14.themeCourse.ee.framework.*;

public class Tester {
	
	public static void main(String args[]){
		Battery battery = new Battery("Battery");
		
		//battery.updateState();
		
		/**
		 * Let the battery run for a bit
		 * We assume time t = 60 minutes
		 */
		for(int i=0;i<24;i++){
			int load = battery.getLoad();
			System.out.print("Load: "+load);
			battery.updateState();
			//System.out.println(battery.getCostFunction());
			int usage = battery.getCurrentUsage();
			System.out.println("\t Usage: "+usage);
			//System.out.println("Expected next load: "+(load+usage));
		}
		
		/**
		int i = -battery.getMaxLoad();
		while(i <= battery.getMaxLoad()){
			int cost = battery.getCostFunction().getCostByDemand(i);
			//System.out.println("Demand: "+i+" Cost: "+cost);
			i+=100;
		}
		//battery.updatePrice(1000);
		//battery.getCurrentUsage();
		//int cost = 1000;
		//System.out.println("Whole cost function: ");
		//int demand = battery.getCostFunction().getDemandByCost(cost);
		//System.out.println("Cost: "+cost+" Demand: "+demand);
	*/
	}

}
