package usb14.themeCourse.ee.framework;

public class Controller {

	private static Controller instance;
	private Controllable controllable;
	
	private Controller(Controllable controllable) {
		this.controllable = controllable;
	}
	
	public static void intialise(Controllable controllable){
		instance = new Controller(controllable);
	}
	
	public static Controller getInstance() {
		return instance;
	}

	public static void main(String[] args) {

	}
}
