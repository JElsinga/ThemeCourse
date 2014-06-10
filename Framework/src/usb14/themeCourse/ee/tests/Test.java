package usb14.themeCourse.ee.tests;

public class Test {
	public static void main(String[] args){
		int errors = 0;
		
		CostFunctionTest cfTest = new CostFunctionTest();
		errors += cfTest.runTests();
		
		System.out.println("Total number of errors: " + errors);
	}
}
