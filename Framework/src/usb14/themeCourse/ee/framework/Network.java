package usb14.themeCourse.ee.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Network extends Observable implements Controllable {

	/**
	 * List of children for which this network is a direct parent.
	 */
	private List<Controllable> children;

	
	// Constructor
	
	
	/**
	 * Constructor
	 */
	public Network() {
		children = new ArrayList<Controllable>();
	}
	
	
	// Queries
	

	/**
	 * Gets the cost function of this network, based on the sum of the
	 * cost functions of its children.
	 * @return	null if this network doesn't have any children, a CostFunction
	 * 			if it does.
	 */
	@Override
	public CostFunction getCostFunction() {
		CostFunction result = null;
		if (!children.isEmpty()){
			result = children.get(0).getCostFunction();
			for(int i = 1; i < children.size(); i++){
				result = result.add(children.get(i).getCostFunction());
			}
		}
		return result;
	}
	
	/**
	 * Gets the amount of energy the network is currently using.
	 * @return	the sum of the usages of its children.
	 */
	@Override
	public int getCurrentUsage() {
		int result = 0;
		for(Controllable controllable: children){
			result += controllable.getCurrentUsage();
		}
		return result;
	}
	
	/**
	 * Returns a new array of the Controllables in this network.
	 * @return
	 */
	public Controllable[] getControllables(){
		Controllable[] result = new Controllable[children.size()];
		for(int i = 0; i < result.length; i++){
			result[i] = children.get(i);
		}
		return result;
	}
	
	
	// Commands

	
	/**
	 * Propagates the price change to all its children.
	 * @param price	The new price for energy.
	 */
	@Override
	public void updatePrice(int price) {
		for(Controllable controllable: children){
			controllable.updatePrice(price);
		}
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Propagates the price change to all its children.
	 * @param t	The current time.
	 */
	@Override
	public void updateState() {
		for(Controllable controllable: children){
			controllable.updateState();
		}
	}
	
	
	/**
	 * Adds a child controllable to this network (can be another network or appliance)
	 * @param controllable - Child to be added to this network
	 * @requires controllable != null
	 * @requires !this.children.contains(controllable)
	 * @requires controllable cannot have this as child or sub-child
	 * @ensures this.children.contains(controllable)
	 */
	public void addControllable(Controllable controllable) {
		children.add(controllable);
		notifyObservers();
	}

	/**
	 * Removes a child from this network (can be either a network or appliance child)
	 * @param controllable - Child to be removed from this network
	 * @requires this.children.contains(controllable)
	 * @ensures !this.children.contains(controllable)
	 */
	public void removeControllable(Controllable controllable) {
		children.remove(controllable);
		notifyObservers();
	}


	

}
