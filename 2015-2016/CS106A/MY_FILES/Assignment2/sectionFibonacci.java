import acm.program.*;

public class sectionFibonacci extends ConsoleProgram{
	private static final int LIMIT = 10000;
	
	public void run(){
		println("Fibonacci to: " + LIMIT);
		
		// n-- is 0, already known 
		int nMinusMinus = 0;
		println(nMinusMinus);
		
		// n- is 1, already known print out
		int nMinus = 1;
		println(nMinus);
		
		// n is sum of n-- and n-, fibonacci start 
		int n = nMinus + nMinusMinus;
		
		// before we print out n, check if less than LIMIT
		while(n < LIMIT){
			// print out first n
			println(n);
			
			// n-- is now n-
			nMinusMinus = nMinus;
			// n- is now n
			nMinus = n;
			
			// new n is now updated n- and n--
			n = nMinus + nMinusMinus;
		}
	}
}

