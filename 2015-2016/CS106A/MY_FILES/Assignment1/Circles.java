import acm.program.*;

public class Circles extends ConsoleProgram {
	private final static double PI = 3.1415926;
	public void run(){
		println("Program requests a radius and returns the area of a cricle using A = pirr");
		double radius = readDouble("Give me a radius for a circle: ");
		double area = PI * radius * radius;
		println("Area of circle is: " + area + " units.");
	}
}
