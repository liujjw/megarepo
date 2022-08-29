/*import acm.program.*;

public class Fibonacci extends ConsoleProgram {
	private static final int FIBONACCIS_UNTIL = 10000;
	public void run(){
		println("F0= 0"); // by definition
		println("F1= 1"); // by definition 
		int f2 = 0, f1 = 1;
		int f0 = 0;
		int counter = 2;
		while(true){
			f0 = f2 + f1;
			if(f0 > FIBONACCIS_UNTIL) break;
			println("F" + counter + "= " + f0);
			f2 = f1;
			f1 = f0;
			counter++;
		}
	}
}
*/

import acm.program.*;

public class Fibonacci extends ConsoleProgram{
	
	public void run(){
		int index = readInt("To what index n to calculate Fibonacci sequence to: ");
		println("F" + index + "= " + fibonacci(index));
	}
	
	/*Calculates the nth Fibonacci number, ie not a sequence of them up to n. */
	private int fibonacci(int index){
		int f2 = 0;
		int f1 = 1;
		int f0 = 0; // see below comment, assume we start at index 2, these values are from index 2
		for(int i = 2; i < index; i++){ // start from index 2, 0 and 1 are already defined to be 0 and 1
			f0 = f2 + f1;
			f2 = f1; // like Euclids algorithm for GCD, we are moving down the values
			f1 = f0;
		}
		return f0; // returns the nth fibonacci, not index n
	}
}
