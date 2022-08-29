import java.util.Arrays;

import acm.program.*;

public class Mode extends ConsoleProgram{
	
	public void run(){
		
		// ordering data
		Arrays.sort(myArray);
		
		int highestRepeats = 0;
		double currentMode = -1;
		
		for(int i = 0; i < myArray.length; i++){
			int repeats = 0;
			double ogElement = myArray[i];
			if(i + 1 != myArray.length){
				double n1Element = myArray[i + 1];
				
				if(ogElement == n1Element){
					repeats++;
					
					int increment = i;
					while(true){
						if(increment + 2 == myArray.length) break;
						
						double n2Element = myArray[increment + 2];
						
						if(n2Element == n1Element){
								repeats++;
								increment++;
						}else{
							break;
						}
					}
				}
				
			}
			if(repeats > highestRepeats){
				highestRepeats = repeats;
				currentMode = myArray[i];
			}
			

		}
		
		println(currentMode);
		
	
	}
	
	private double[] myArray = {
			19,19,19,19,19,19,	11,6,21,5,29,28,13,40,49,46,38,38,38,38,38,38,38,38,38,38,38,38,38,38,38,38,38,38,38,38,38,38,9,44,32,47,1,10,2,7,36, 36, 50,33, 33, 33, 33,27,12,35,45,3,48,25,39,15,42,16,26,18,4,8,17,43,30,23,22,14,31,37,41,34,24,20	};
	
	/**
	 * How to find the mode:
	 * Order the data.
	 * Iterate over data
	 * 
	 * eg
	 * look at first element
	 * look at second element to compare
	 * if yes then keep looking until no
	 * if no then look at the next element and do comparisons
	 * keep a tracker to track the modes
	 * 
	 * idea:
	 * hb radix sort?
	 * ie sort them into buckets
	 * if their ends match, put them into buckets until theyre all in sorted buckets
	 * look through each bucket for 
	 * put each value into a bucket, matching one get hashed into the same bucket
	 * while radix sorting, group into further buckets
	 * 
	 * hb go back to regular idea first
	 * 
	 * binary search for values, em not necessary they will already be sorted
	 *  
	 *  
	 *  */
}
