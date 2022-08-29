/*This program asks the user for two double precision floating-point numbers
 * , multiplies them together and divides them by 2, and then displays the result.
 */

import acm.program.*;

public class Doublemath extends ConsoleProgram {
	public void run(){
		println("Does arithmetic on two real numbers and spits out a result.");
		double n1 = readDouble("Give me a real:");
		double n2 = readDouble("Give me another real:");
		
		double result = (n1 * n2) / 2;
		println("Result is: " + result + ".");
	}
}
