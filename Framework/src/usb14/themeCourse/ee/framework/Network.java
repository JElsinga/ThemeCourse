package usb14.themeCourse.ee.framework;

import java.util.List;

public class Network implements Controllable {

	/**
	 * List of children for which this network is a direct parent.
	 */
	private List<Controllable> children;

	/**
	 * Constructor
	 */
	public Network() {}

	@Override
	public CostFunction getCostFunction() {
		CostFunction result = new CostFunction();
		for(Controllable controllable: children){
			result.add(controllable.getCostFunction());
		}
		return result;
	}
	
	@Override
	public double getCurrentUsage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateStatus() {
		for(Controllable controllable: children){
			controllable.updateStatus();
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
	}

	/**
	 * Removes a child from this network (can be either a network or appliance child)
	 * @param controllable - Child to be removed from this network
	 * @requires this.children.contains(controllable)
	 * @ensures !this.children.contains(controllable)
	 */
	public void removeControllable(Controllable controllable) {
		if(children.contains(controllable)){
			children.remove(children.indexOf(controllable));
		}
	}

}
