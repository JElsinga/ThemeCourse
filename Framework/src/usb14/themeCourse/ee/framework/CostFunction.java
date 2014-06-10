package usb14.themeCourse.ee.framework;

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class CostFunction {
	
	public static final int MAX_COST = 1000;
	public static final int MIN_COST = 0;

	//SortedMap<DEMAND,COST>
	private SortedMap<Integer, Integer> costByDemandMap;
	
	
	// Constructor
	
	
	/**
	 * Creates a new CostFunction.
	 */
	public CostFunction(SortedMap<Integer, Integer> costByDemandMap) {
		validate(costByDemandMap);
		this.costByDemandMap = costByDemandMap;
	}
	
	
	
	// Queries
	
	/**
	 * Returns a strictly increasing array of demands in this CostFunction
	 */
	public int[] getDemands(){
		int result[] = new int[costByDemandMap.keySet().size()];
		int i = 0;
		for(int demand : costByDemandMap.keySet()){
			result[i] = demand;
			i++;
		}
		return result;
	}
	
	/**
	 * Returns a strictly decreasing array of costs in this CostFunction
	 */
	public int[] getCosts(){
		int result[] = new int[costByDemandMap.keySet().size()];
		int i = 0;
		for(int demand : costByDemandMap.keySet()){
			result[i] = costByDemandMap.get(demand);
			i++;
		}
		return result;
	}
	
	/**
	 * Gets the cost associated with a given demand
	 * @param demand	The given demand
	 * @return			The cost associated with the demand,
	 * 					returns MAX_COST + 1 if all demands in the cost
	 * 					function are higher than the given demand or 0
	 * 					when the demand is higher than any demand in the
	 * 					cost function. 
	 */
	public int getCostByDemand(int demand) {
		int result = 0;
		if(costByDemandMap.containsKey(demand)){
			result = costByDemandMap.get(demand);
		} else {
			int previous = Integer.MIN_VALUE;
			for(int dem : getDemands()){
				if(dem > demand){
					if (previous == Integer.MIN_VALUE)
						result = MAX_COST + 1;
					else
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
	public int getDemandByCost(int cost) {
		int viable = 0;
		for(int dem : getDemands()){	// kijk per demand of de cost groter of gelijk is aan cost
			if(costByDemandMap.get(dem) >= cost){
				viable = dem;
			}
		}
		return viable;
	}
	
	/**
	 * Returns a new CostFunction that is the aggregation
	 * of this CostFunction and the given CostFunction.
	 * @param cf	The CostFunction that is to be added to this one.
	 * @return		The new CostFunction
	 */
	public CostFunction add(CostFunction cf){
		SortedMap<Integer, Integer> result = new TreeMap<Integer, Integer>();
		
		// list of all costs
		SortedSet<Integer> values = new TreeSet<Integer>();
		values.addAll(this.costByDemandMap.values());
		values.addAll(cf.costByDemandMap.values());
				
		// add to result
		for(int highestCost: values){
			result.put(this.getDemandByCost(highestCost) + cf.getDemandByCost(highestCost), highestCost);
		}
		
		return new CostFunction(result);
	}
	
	
	// Commands
	
	/**
	 * Updates the cost for a given demand.
	 * @param cost		The new cost associated by the given demand 
	 * @param demand	The demand
	 * @throws 	IllegalArgumentException when the given demand does not exist
	 * 			in this cost function, or when cost is not between MIN_COST
	 * 			and MAX_COST.
	 */
	protected void updateCostForDemand(int cost, int demand){
		Integer oldCost = costByDemandMap.get(demand);
		if (oldCost != null)
			costByDemandMap.put(demand, cost);
		else
			throw new IllegalArgumentException("The given demand does not exist in the cost function.");
		try
		{
			validate(costByDemandMap);
		}
		catch (Exception e)
		{
			// Restore state
			costByDemandMap.put(demand, oldCost);
			// Rethrow exception
			throw e;
		}
	}
	
	public String toString(){
		String result = "";
		for(java.util.Map.Entry<Integer, Integer> entry :costByDemandMap.entrySet()){
			result = result + "\t Entry: "+ entry.toString();
		}
		return result;
	}
	
	private void validate(SortedMap<Integer, Integer> costByDemandMap){
		int previousPrice = MAX_COST + 1;
		for(int demand:costByDemandMap.keySet()){
			if (demand == Integer.MIN_VALUE){
				throw new IllegalArgumentException("Demand cannot be Integer.MIN_VALUE (" + Integer.MIN_VALUE + ")");
			}
			if (costByDemandMap.get(demand) < MIN_COST || costByDemandMap.get(demand) > MAX_COST){
				throw new IllegalArgumentException("Prices must be between " + MIN_COST + " and " + MAX_COST);
			}
			if (costByDemandMap.get(demand) >= previousPrice){
				throw new IllegalArgumentException("A cost function must be strictly decreasing");
			}
		}
	}
}
