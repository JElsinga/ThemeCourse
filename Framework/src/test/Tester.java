package test;

import usb14.themeCourse.ee.application.Battery;
import usb14.themeCourse.ee.application.FridgeV2;
import usb14.themeCourse.ee.framework.*;

public class Tester {
	
	public static void main(String args[]){
		Battery battery = new Battery("Battery");
		FridgeV2 fridge = new FridgeV2("Fridge");
		
		System.out.println(fridge.getCostFunction());
		
		for(int i=0;i<24;i++){
			System.out.println("Temperature: "+fridge.getTemp());
			fridge.updateState();
			
			System.out.println(fridge.getCostFunction());
		}
		
		/**
		 * Let the battery run for a bit
		 * We assume time t = 60 minutes
		 */
		for(int i=0;i<24;i++){
			int load = battery.getLoad();
			//System.out.println("\t\t Load: "+load);
			battery.updateState();
			//System.out.println(battery.getCostFunction());
			//int usage = battery.getCurrentUsage();
			//System.out.println("\t\t Usage: "+usage);
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
