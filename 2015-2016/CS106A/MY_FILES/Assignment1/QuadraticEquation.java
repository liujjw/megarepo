/*Quadratic equation*/

import acm.program.*;

public class QuadraticEquation extends ConsoleProgram{

	public void run(){
		double a = readDouble("a= ");
		double b = readDouble("b= ");
		double c = readDouble("c= ");
		
		double positive = posQuadraticE(a, b, c);
		if (positive == -1){
			println("No solution");
		}else{
			println("First solution: " + positive);
			
			double negative = negQuadraticE(a, b, c);
			if(negative == positive){
				println("One solution.");
			}else{
				println("Second solution: " + negative);
			}
		}
	}
	
	private double posQuadraticE(double a, double b, double c){
		if((b * b) - 4 * a *c < 0) return (-1);
		
		return ((-b + Math.sqrt((b * b) - 4*(a * c))) / (2 * a));
	}
	
	private double negQuadraticE(double a, double b, double c){
		if((b * b) - 4 * a *c < 0) return (-1);
		
		return ((-b - Math.sqrt((b * b) - 4*(a * c))) / (2 * a));
	}
	
}
