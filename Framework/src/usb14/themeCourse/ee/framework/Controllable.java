package usb14.themeCourse.ee.framework;

public interface Controllable {

	public CostFunction getCostFunction();
	
	public void updateStatus(double price);
	
	public double getCurrentUsage();
}
