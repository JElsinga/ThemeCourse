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
	 * @return			The cost associated with the demand
	 */
	public int getCostByDemand(int demand) {
		int result = CostFunction.MAX_COST;
		if(costByDemandMap.containsKey(demand)){
			result = costByDemandMap.get(demand);
		} else {
			int[] demands = getDemands();
			int i = 0;
			while(i < demands.length){
				if(demands[i] < 0 && demands[i] > demand){
					result = costByDemandMap.get(demands[i]);
					break;
				}
				if (demands[i] > 0 && demands[i] > demand){
					if (i > 0)
						result = costByDemandMap.get(demands[i-1]);
					break;
				}
				i++;
			}
			if(i == demands.length)
				result = costByDemandMap.get(demands[i-1]);
		}
		return result;
	}
	
	/**
	 * Gets the demand associated with a given cost
	 * @param cost	The given cost
	 * @return		The demand associated with the cost
	 */
	public int getDemandByCost(int cost) {
		int result = 0;
		int[] demands = getDemands();
		
		int i = 0;
		while(i < demands.length){	// kijk per demand of de cost groter of gelijk is aan cost
			if (costByDemandMap.get(demands[i]) == cost){
				result = demands[i];
				break;
			}
			if(demands[i] < 0 && costByDemandMap.get(demands[i]) < cost){
				result = demands[i];
				break;
			}
			if(demands[i] > 0 && costByDemandMap.get(demands[i]) < cost){
				if (i > 0)
					result = demands[i-1];
				break;
			}
			i++;
		}
		if (i == demands.length)
			result = demands[i - 1];
		
		return result;
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
	 * @throws 	IllegalArgumentException when cost is not between MIN_COST
	 * 			and MAX_COST.
	 */
	public void updateCostForDemand(int cost, int demand) throws IllegalArgumentException{
		Integer oldCost = costByDemandMap.get(demand);
		costByDemandMap.put(demand, cost);
		//throw new IllegalArgumentException("The given demand does not exist in the cost function.");
		try
		{
			validate(costByDemandMap);
		}
		catch (Exception e)
		{
			// Restore state
			if(oldCost!=null)
				costByDemandMap.put(demand, oldCost);
			// Rethrow exception
			throw e;
		}
	}
	
	/**
	 * Removes a demand (and indirectly its cost) from the cost function
	 * This function does not have to validate because it adds nothing to costByDemandMap
	 * @para demand		The value of the demand which has to be removed
	 */
	public void deleteCostForDemand(int demand){
		if(costByDemandMap.get(demand)!=null)
			costByDemandMap.remove(demand);
	}
	
	public String toString(){
		String result = "CostFunction";
		for(java.util.Map.Entry<Integer, Integer> entry :costByDemandMap.entrySet()){
			result = result + ", "+ entry.toString();
		}
		return result;
	}
	
	// Niet de mooiste manier om te valideren, wel de makkelijkste
	private void validate(SortedMap<Integer, Integer> costByDemandMap){
		int previousPrice = MAX_COST + 1;
		for(int demand:costByDemandMap.keySet()){
			if (demand == Integer.MIN_VALUE){
				throw new IllegalArgumentException("Demand cannot be Integer.MIN_VALUE (" + Integer.MIN_VALUE + ")");
			}
			if (costByDemandMap.get(demand) < MIN_COST || costByDemandMap.get(demand) > MAX_COST){
				throw new IllegalArgumentException("Prices '"+demand+","+costByDemandMap.get(demand)+"' must be between " + MIN_COST + " and " + MAX_COST);
			}
			if (costByDemandMap.get(demand) >= previousPrice){
				throw new IllegalArgumentException("A cost function must be strictly decreasing");
			}
			previousPrice = costByDemandMap.get(demand);
		}
	}
}
