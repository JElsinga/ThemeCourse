package usb14.themeCourse.ee.framework;

import java.util.Observable;

public class Supply extends Observable implements Runnable{

	//Cost function of the Supply
	private CostFunction costFunction;
	
	public Supply() {}

	/**
	 * Setter of variable costFuncion
	 * @param costFunction
	 */
	public void updateCostFunction(CostFunction costFunction){
		this.costFunction = costFunction;
	}
	
	/**
	 * getter of variable costFunction
	 * @return this.costFuncion
	 */
	public CostFunction getCostFunction(){
		return this.costFunction;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	
}
