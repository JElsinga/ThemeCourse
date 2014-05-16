package usb14.themeCourse.ee.framework;

import java.util.Map;

public class CostFunction {

	private Map<Double, Double> costByDemandMap;
	
	/**
	 * Creates a new CostFunction.
	 */
	public CostFunction(Map<Double, Double> costByDemandMap) {
		this.costByDemandMap = costByDemandMap;
	}
	
	
	// Queries
	
	
	/**
	 * Gets the cost associated with a given demand
	 * @param demand	The given demand
	 * @return			The cost associated with the demand
	 */
	public double getCostByDemand(double demand) {
		// TODO stub
		return 0.0;
	}
	
	/**
	 * Gets the demand associated with a given cost
	 * @param cost	The given cost
	 * @return		The demand associated with the cost
	 */
	public double getDemandByCost(double cost) {
		// TODO stub
		return 0.0;
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
