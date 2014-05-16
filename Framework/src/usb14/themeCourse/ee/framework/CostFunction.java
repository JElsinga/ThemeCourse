package usb14.themeCourse.ee.framework;

import java.util.Map;
import java.util.SortedMap;

public class CostFunction {

	private SortedMap<Double, Double> costByDemandMap;
	
	/**
	 * Creates a new CostFunction.
	 */
	public CostFunction(SortedMap<Double, Double> costByDemandMap) {
		this.costByDemandMap = costByDemandMap;
	}
	
	
	// Queries
	
	
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
					result = previous;
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
		return 0.0;
		
		/**double previous = 0;
		for(double d:costByDemandMap.keySet()){
			if(d > demand){
				result = previous;
			}else{
				previous = d;
			}
		}**/
	}
	
	/**
	 * Returns a new ConstFunction that is the aggregation
	 * of this CostFunction and the given CostFunction.
	 * @param cf	The CostFunction that is to be added to this one.
	 * @return		The new CostFunction
	 */
	public CostFunction add(CostFunction cf){
		// TODO Auto-generated method stub
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
			costByDemandMap.put(cost, demand);
		else
			throw new IllegalArgumentException("The given demand does not exist in the cost function.");
	}
}
