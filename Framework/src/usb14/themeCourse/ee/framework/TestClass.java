package usb14.themeCourse.ee.framework;

import java.util.SortedMap;
import java.util.TreeMap;

public class TestClass {

	public static void main(String args[]){
		/**
		SortedMap<Double, Double> function = new TreeMap<Double, Double>();
		
		function.put(100.0, 10.0);
		function.put(200.0, 50.0);
		function.put(300.0, 70.0);
		CostFunction result = new CostFunction(function);
		System.out.println("Cost function: "+result);
		for(int i=0;i<100;i+=20){
			double output = result.getDemandByCost(i);
			System.out.println("Demand: "+i+" by Cost: "+output);
		}
		*/
		
		SortedMap<Double, Double> fun = new TreeMap<Double, Double>();
		//<demand,cost>
		//fun.put(300.0,0.0);
		fun.put(300.0,0.0);
		//fun.put(500.0, 1600.0);
		CostFunction fridge = new CostFunction(fun);
		System.out.println("Cost function fridge: "+fridge);
		//Expected usage = 0.0
		System.out.println("Current cost: "+fridge.getDemandByCost(1000.0));
		
		
		fridge.updateCostForDemand(429.0,300.0);
		System.out.println("Cost function fridge: "+fridge);
		//Expected usage = 0.0
		System.out.println("Current usage: "+fridge.getDemandByCost(1000.0));
		
		fridge.updateCostForDemand(1500.0,300.0);
		System.out.println("Cost function fridge: "+fridge);
		//Expected usage = 300.0
		System.out.println("Current usage: "+fridge.getDemandByCost(1000.0));
	}
}
