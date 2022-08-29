import acm.program.*;

public class nDigits extends ConsoleProgram{
	public void run(){
		int n = readInt("This program computes the number of digits in a number n: ");
		println("The number of digits in " + n + " is: " + nDigitsCalculator(n));
	}
	
	private int nDigitsCalculator(int n){
		// calculate the number of digits in n, assumed to be a positive integer
		int counter = 0;
		while(n % 10 != n){
			n /= 10;
			counter++;
		}
		counter++;
		return counter;
	}
}
