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
		
		return errors;
	}
	
	private boolean testGetDemandByCost(){
		System.out.print("Testing getDemandByCost... ");
		SortedMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
		map.put(-2, CostFunction.MAX_COST);
		map.put(-1, 500);
		map.put(1, 300);
		map.put(2, 250);
		CostFunction cf = new CostFunction(map);
		
		if (cf.getDemandByCost(CostFunction.MAX_COST) != -2 ||
				cf.getDemandByCost(600) != -2 ||
				cf.getDemandByCost(500) != -1 ||
				cf.getDemandByCost(400) != -1 ||
				cf.getDemandByCost(100) != 2)
			return false;
		else
			return true;
	}
	
	private boolean testGetCostByDemand(){
		System.out.print("Testing getCostByDemand... ");
		SortedMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
		map.put(-2, CostFunction.MAX_COST);
		map.put(-1, 500);
		map.put(1, 300);
		map.put(2, 250);
		CostFunction cf = new CostFunction(map);
		
		if (cf.getCostByDemand(-3) != CostFunction.MAX_COST + 1 ||
				cf.getCostByDemand(-2) != CostFunction.MAX_COST ||
				cf.getCostByDemand(-1) != 500 ||
				cf.getCostByDemand(0) != 500 ||
				cf.getCostByDemand(1) != 300 ||
				cf.getCostByDemand(3) != 0)
			return false;
		else
			return true;
	}
}
