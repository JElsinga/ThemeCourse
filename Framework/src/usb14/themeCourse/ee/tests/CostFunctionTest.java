package usb14.themeCourse.ee.tests;

import java.util.SortedMap;
import java.util.TreeMap;
import usb14.themeCourse.ee.framework.CostFunction;



public class CostFunctionTest implements UnitTest {

	public int runTests(){
		int errors = 0;
		System.out.println("Testing CostFunction");
		
		if(testGetDemandByCost())
			System.out.println("Okay");
		else{
			System.out.println("Error");
			errors++;
		}
		
		if(testGetCostByDemand())
			System.out.println("Okay");
		else{
			System.out.println("Error");
			errors++;
		}
		
		if(testAdd())
			System.out.println("Okay");
		else{
			System.out.println("Error");
			errors++;
		}
		
		return errors;
	}
	
	private boolean testGetDemandByCost(){
		System.out.print("Testing getDemandByCost... ");
		SortedMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
		map.put(-50, 700);
		map.put(-20, 600);
		map.put(0, 500);
		map.put(20, 400);
		map.put(50, 300);
		CostFunction cf = new CostFunction(map);
		
		if (cf.getDemandByCost(250) != 50 ||
				cf.getDemandByCost(300) != 50 ||
				cf.getDemandByCost(350) != 20 ||
				cf.getDemandByCost(550) != 0 ||
				cf.getDemandByCost(800) != -50)
			return false;
		else
			return true;
	}
	
	private boolean testGetCostByDemand(){
		System.out.print("Testing getCostByDemand... ");
		SortedMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
		map.put(-50, 700);
		map.put(-20, 600);
		map.put(0, 500);
		map.put(20, 400);
		map.put(50, 300);
		CostFunction cf = new CostFunction(map);
		
		if (cf.getCostByDemand(60) != 300 ||
				cf.getCostByDemand(50) != 300 ||
				cf.getCostByDemand(30) != 400 ||
				cf.getCostByDemand(20) != 400 ||
				cf.getCostByDemand(10) != 500 ||
				cf.getCostByDemand(-10) != 500 ||
				cf.getCostByDemand(-20) != 600 ||
				cf.getCostByDemand(-30) != 600)
			return false;
		else
			return true;
	}
	
	private boolean testAdd(){
		System.out.print("Testing add... ");
		SortedMap<Integer, Integer> map1 = new TreeMap<Integer, Integer>();
		map1.put(0, CostFunction.MAX_COST);
		map1.put(10, 20);
		map1.put(20, 10);
		SortedMap<Integer, Integer> map2 = new TreeMap<Integer, Integer>();
		map2.put(0, CostFunction.MAX_COST);
		map2.put(10, 30);
		map2.put(20, 10);
		CostFunction cf1 = new CostFunction(map1);
		CostFunction cf2 = new CostFunction(map2);
		
		CostFunction cf = cf1.add(cf2);
		
		if (cf.getCostByDemand(0) != CostFunction.MAX_COST ||
				cf.getCostByDemand(10) != 30 ||
				cf.getCostByDemand(20) != 20 ||
				cf.getCostByDemand(30) != 20 ||	// Weet niet zeker of dit wel zo zou moeten zijn
				cf.getCostByDemand(40) != 10 ||
				cf.getCostByDemand(50) != 10)
			return false;
		else
			return true;
	}
}
