import acm.program.*;
import acm.util.*;

public class untilHeads extends ConsoleProgram{
	
	public void run() {
		
		double masterCounter = 0;
		for(int i = 0; i < 100; i++){
			int counter = 0;
			int headCounter = 0;
			
			while(headCounter != 3){
				
				String result = rgen.nextBoolean()? "Heads" : "Tails";
				println(result);
				counter++;
				
				if(result == "Heads"){
					headCounter++;
				}else{
					headCounter = 0;
				}
			}
			masterCounter += counter;
			/* Number of flips for one run // println("It took " + counter + " flips to get 3 consecutive heads."); */
		}
		double average = masterCounter / 100;
		println("It took an average of " + average + " flips to get 3 consecutive heads.");
	}
	
	private RandomGenerator rgen = new RandomGenerator();
}
