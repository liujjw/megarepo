import acm.program.*;
import acm.util.*;

public class monteCarloIntegration extends ConsoleProgram{

	public void run(){
		
		// monte carlo integration???
		
		int inCircle = 0;
		for(int i = 0; i < 10000; i++){
			double squareX = rgen.nextDouble(-1.0, 1.0);
			double squareY = rgen.nextDouble(-1.0, 1.0);
			
			if(((squareX * squareX) + (squareY * squareY)) < 1){
				inCircle++;
			}
		}
		double ratioCircleSquare = inCircle / 10000.0;
		println("Ratio between darts falling circle and square is: " + ratioCircleSquare);
	}
	
	private RandomGenerator rgen = new RandomGenerator();
}
