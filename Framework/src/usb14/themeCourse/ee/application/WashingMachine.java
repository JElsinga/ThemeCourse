package usb14.themeCourse.ee.application;

import java.util.SortedMap;
import java.util.TreeMap;

import usb14.themeCourse.ee.framework.Appliance;
import usb14.themeCourse.ee.framework.Controller;
import usb14.themeCourse.ee.framework.CostFunction;

public class WashingMachine extends Appliance {
	public enum State {
		ON, OFF
	}
	
	private final int startInterval = 14 * 60;
	private final int endInterval = 17 * 60;
	private final int demand = 100;
	private final int preferredCost = 450;
	
	private State state;
	private int currentPrice;
	private boolean hasStarted;
	private boolean hasFinished;
	private int remainingTime;
	
	public WashingMachine(String name) {
		super(name);
		this.state = State.OFF;
		this.hasStarted = false;
		this.hasFinished = false;
		this.remainingTime = 120;
		
		SortedMap<Integer, Integer> function = new TreeMap<Integer, Integer>();
		function.put(demand, 0);
		super.setCostFunction(new CostFunction(function));
	}

	@Override
	public void updatePrice(int price) {
		this.currentPrice = price;
		
		// update state based on the current price
		if (getCurrentUsage() == 0)
			this.state = State.OFF;
		else {
			this.state = State.ON;
			hasStarted = true;
		}
		
		setChanged();
		notifyObservers();
	}

	@Override
	public void updateState() {
		int currentTime = Controller.getInstance().getTime();
		int cost = 0;
		
		/* This washing machine will only start washing between 2 pm and 5 pm
		 * and will take 2 hours to finish.
		 */
		if (this.state == State.ON)
			this.remainingTime -= Controller.getInstance().getIntervalDuration();
		hasFinished = (remainingTime <= 0);
		
		if (hasFinished || currentTime < startInterval)
			// If done washing, or not time to wash yet, don't pay anything.
			cost = 0;
		else if (!hasStarted && currentTime < endInterval)
			// If not started, but time to start watching, set price depending on how late it is
			cost = preferredCost + (CostFunction.MAX_COST - preferredCost) * (currentTime - startInterval) / (endInterval - startInterval);
		else
			// If already started, or we really need to start washing, set to our maximum price
			cost = CostFunction.MAX_COST;
		
		super.getCostFunction().updateCostForDemand(cost, demand);
	}

	@Override
	public int getCurrentUsage() {
		return super.getCostFunction().getDemandByCost(currentPrice);
	}

}
