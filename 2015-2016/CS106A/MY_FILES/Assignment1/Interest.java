import acm.program.*;

public class Interest extends ConsoleProgram{
	
	public void run(){
		println("Applies compounded interest to a balance showing two years thereof.");
		double balance  = readDouble("Starting balance: ");
		double interest = readDouble("Annual interest rate: ");
		
		double afterOneYear = balance + ((balance * interest) / 100);
		double afterTwoYears = afterOneYear + (interest * afterOneYear) / 100;
		
		println("After a year: $" + afterOneYear);
		println("After two years: $" + afterTwoYears);
	}
	
}
