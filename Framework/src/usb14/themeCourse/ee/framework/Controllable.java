package usb14.themeCourse.ee.framework;

public interface Controllable {

	public CostFunction getCostFunction();
	
	public void updateStatus();
	
	public double getCurrentUsage();
}
