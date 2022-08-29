import acm.program.*;
import acm.util.*;

public class radioActiveDecay extends ConsoleProgram{
	
	public void run(){
		
		int sampleSize = readInt("How many atoms in sample? ");
		double decayConstant = readDouble("Decay constant to a ratio of 1: ");
		
		int remainingAtoms = sampleSize;
		
		int afterAYear = remainingAtoms;
		
		int years = 0;
		
		
		while(remainingAtoms > 0){
			for(int i = 0; i < remainingAtoms; i++){
				afterAYear = rgen.nextBoolean(decayConstant)? afterAYear - 1 : afterAYear - 0;  
			}
			remainingAtoms = afterAYear;
			years++;
			println("There are " + remainingAtoms + " remaining atoms at the end of year " + years);
		}

	}
	
	private RandomGenerator rgen = new RandomGenerator();

}
