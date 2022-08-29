import acm.program.*;

public class extendingRationalClass extends ConsoleProgram{
	public void run(){
		Rational myRational = new Rational(570000000, 800000000);
		// myRational = myRational.add(45);
		println(myRational.toString());
	}
}
