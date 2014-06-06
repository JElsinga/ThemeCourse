package usb14.themeCourse.ee.framework;

public interface Controllable {

	public CostFunction getCostFunction();
	
	public void updatePrice(int price);
	
	public void updateState();
	
	public int getCurrentUsage();
}
