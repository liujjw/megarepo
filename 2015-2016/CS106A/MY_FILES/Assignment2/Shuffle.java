import java.util.Arrays;

import acm.program.*;
import acm.util.RandomGenerator;

public class Shuffle extends ConsoleProgram{
	
	public void run(){
		// uses selection sort to shuffle an array of integers from 1-52, possibly cards
		shuffleSelectionSort(myArray);
		String s = Arrays.toString(myArray);
		println(s);
	}
	
	public void shuffleSelectionSort(double[] myArray){
		RandomGenerator rgen = new RandomGenerator();
		for(int i = 0; i < myArray.length; i++){
			for(int j = i; j < myArray.length; j++){
				// take left hand and point to left element, compare that left
				// element to the right elements to find one that is the lowest, but instead of lowest pick a random value
				int index = rgen.nextInt(j, myArray.length - 1);
				
				double tmp = myArray[i];
				myArray[i] = myArray[index];
				myArray[index] = tmp;
			}
		}
	}
	
	private double[] myArray = {
			1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52	
	};
	
}
