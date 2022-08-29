import acm.program.*;
import acm.util.*;

public class carbon14Decay extends ConsoleProgram{
	public void run(){
		println("Carbon-14's half life is 5730 years.");
		double sampleSizeInGrams = readDouble("How many grams in sample? ");
		double decayInAYear = 1 / 5730;
		int years = 0;
		/* Half-life is defined as the amount of time for half of a sample to decay.*/
		
		while(sampleSizeInGrams > 0){
			if(rgen.nextBoolean()){
				sampleSizeInGrams -= 1;
				years++;
			}
		}
	}
	private RandomGenerator rgen = new RandomGenerator();
}
