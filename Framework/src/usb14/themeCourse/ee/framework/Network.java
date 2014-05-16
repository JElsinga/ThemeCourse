package usb14.themeCourse.ee.framework;

import java.util.List;

public class Network implements Controllable {

	private List<Controllable> children;

///// constructor
	public Network() {
		
	}

///// queries
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

///// commands
	@Override
	public void updateStatus() {
		for(Controllable controllable: children){
			controllable.updateStatus();
		}
	}
	
	public void addControllable(Controllable controllable) {
		children.add(controllable);
	}

	public void removeControllable(Controllable controllable) {
		if(children.contains(controllable)){
			children.remove(children.indexOf(controllable));
		}
	}



}
