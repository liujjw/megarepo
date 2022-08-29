import acm.program.*;

public class ImperialtoMetric extends ConsoleProgram{
	private static final double RATIO_CM_INCHES = 2.54;
	public void run(){
		println("Program requests feet and inches to convert into centimeters as floating point doubl precision number.");
		int feet = readInt("Feet: ");
		int inches = readInt("Inches: ");
		
		double centimeters = ((feet * 12) + inches) * RATIO_CM_INCHES;
		println(feet + " ft " + inches + " in = " + centimeters + " cm");
	}

}
