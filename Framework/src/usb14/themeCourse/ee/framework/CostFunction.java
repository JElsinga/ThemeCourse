package usb14.themeCourse.ee.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.RowFilter.Entry;

public class CostFunction {

	//SortedMap<DEMAND,COST>
	private SortedMap<Double, Double> costByDemandMap;
	
	
	// Constructor
	
	
	/**
	 * Creates a new CostFunction.
	 */
	public CostFunction(SortedMap<Double, Double> costByDemandMap) {
		this.costByDemandMap = costByDemandMap;
	}
	
	
	// Queries
	public SortedMap<Double, Double> getCosts(){
		return costByDemandMap;
	}
	
	/**
	 * Gets the cost associated with a given demand
	 * @param demand	The given demand
	 * @return			The cost associated with the demand
	 */
	public double getCostByDemand(double demand) {
		double result = 0;
		if(costByDemandMap.containsKey(demand)){
			result = costByDemandMap.get(demand);
		}else{
			double previous = 0;
			for(double dem:costByDemandMap.keySet()){
				if(dem > demand){
					result = costByDemandMap.get(previous);
					break;
				}else{
					previous = dem;
				}
			}
		}
		return result;
	}
	
	/**
	 * Gets the demand associated with a given cost
	 * @param cost	The given cost
	 * @return		The demand associated with the cost
	 */
	public double getDemandByCost(double cost) {
		double viable = 0;
		Set<Double> set = costByDemandMap.keySet();
		//System.out.println(set);
		for(double d:set){
			//System.out.print("Checking: "+costByDemandMap.get(d)+" ");
			if(costByDemandMap.get(d) >= cost){
				viable = d;
				//System.out.println("Found viable: "+d);
			}else{
				//System.out.println("No viable found: "+viable);
			}
		}
		return viable;
		//return 0.0;
		/**
		double result = 0;
		double previous = 0;
		for(double d:costByDemandMap.keySet()){
			if(costByDemandMap.get(d) < cost){
				result = previous;
				System.out.println("True Result: "+result+", previous: "+previous);
				
			}else{
				previous = d;
				System.out.println("False Result: "+result+", previous: "+previous);
			}
		}
		return result;
		*/
	}
	
	/**
	 * Returns a new CostFunction that is the aggregation
	 * of this CostFunction and the given CostFunction.
	 * @param cf	The CostFunction that is to be added to this one.
	 * @return		The new CostFunction
	 */
	public CostFunction add(CostFunction cf){
		SortedMap<Double, Double> result = new TreeMap<Double, Double>();
		for(java.util.Map.Entry<Double, Double> entryFunction1: costByDemandMap.entrySet() ){
			for(java.util.Map.Entry<Double, Double> entryFunction2: cf.getCosts().entrySet()){
				// todo check of Albert het eens is met onze oplossing.
			}
			
		}
		
		return null;
	}
	
	
	// Commands
	
	/**
	 * Updates the cost for a given demand.
	 * @param cost		The new cost associated by the given demand 
	 * @param demand	The demand
	 * @throws 	IllegalArgumentException when the given demand does not exist
	 * 			in this cost function.
	 */
	protected void updateCostForDemand(double cost, double demand){
		if (costByDemandMap.containsKey(demand))
			costByDemandMap.put(demand, cost);
		else
			throw new IllegalArgumentException("The given demand does not exist in the cost function.");
	}
	
	public String toString(){
		String result = "";
		for(java.util.Map.Entry<Double, Double> entry :costByDemandMap.entrySet()){
			
			result = result + "\t Entry: "+ entry.toString();
		}
		return result;
	}
}
