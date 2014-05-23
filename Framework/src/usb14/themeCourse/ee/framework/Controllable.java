package usb14.themeCourse.ee.framework;

public interface Controllable {

	public CostFunction getCostFunction();
	
	public void updatePrice(double price);
	
	public void updateState(int t);
	
	public double getCurrentUsage();
}
