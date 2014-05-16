package usb14.themeCourse.ee.framework;

public abstract class Appliance implements Controllable {
	private final String name;
	private CostFunction costFunction;
	
	/**
	 * Dit is een test
	 * @param name
	 */
	public Appliance(String name) {
		this.name = name;
	}
	
///// queries
	public String getName(){
		return this.name;
	}
	
	@Override
	public CostFunction getCostFunction(){
		return this.costFunction;
	}
	
	private void setCostFunction(CostFunction costFunction){
		this.costFunction = costFunction;
	}
	

}
