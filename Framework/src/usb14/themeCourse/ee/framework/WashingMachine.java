package usb14.themeCourse.ee.framework;

import java.util.SortedMap;
import java.util.TreeMap;

public class WashingMachine extends Appliance {
	public enum State {
		ON, OFF
	}
	
	private final int startInterval = 14 * 60;
	private final int endInterval = 17 * 60;
	private final double demand = 120.0;
	private final double preferredCost = 900;
	private final double maxCost = 1400;
	
	private State state;
	private double currentPrice;
	private boolean hasStarted;
	private boolean hasFinished;
	private int remainingTime;
	
	public WashingMachine(String name) {
		super(name);
		this.state = State.OFF;
		this.hasStarted = false;
		this.hasFinished = false;
		this.remainingTime = 120;
		
		SortedMap<Double, Double> function = new TreeMap<Double, Double>();
		function.put(demand, 0.0);
		super.setCostFunction(new CostFunction(function));
	}

	@Override
	public void updatePrice(double price) {
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
		
		/* This washing machine will only start washing between 2 pm and 5 pm
		 * and will take 2 hours to finish.
		 */
		if (this.state == State.ON)
			this.remainingTime -= Controller.getInstance().getIntervalDuration();
		hasFinished = (remainingTime <= 0);
		
		if (hasFinished || currentTime < startInterval)
			// If done washing, or not time to wash yet, don't pay anything.
			super.getCostFunction().updateCostForDemand(0, demand);
		else if (!hasStarted && currentTime < endInterval) {
			// If not started, but time to start watching, set price depending on how late it is
			double cost = preferredCost + (maxCost - preferredCost) * (currentTime - startInterval) / (endInterval - startInterval);
			super.getCostFunction().updateCostForDemand(cost, demand);
		}
		else
			// If already started, or we really need to start washing, set to our maximum price
			super.getCostFunction().updateCostForDemand(maxCost, demand);
		
	}

	@Override
	public double getCurrentUsage() {
		return super.getCostFunction().getDemandByCost(currentPrice);
	}

}
