import acm.program.*;

public class meanProgram extends ConsoleProgram{
	/*
	 * File: GymnasticsJudge.java
	 * --------------------------
	 * This file reads in an array of scores and computes the
	 * average.
	 */
	
/** Runs the program */
   public void run() {
      int nJudges = readInt("Enter number of judges: ");
      double[] scores = new double[nJudges];
      for (int i = 0; i < nJudges; i++) {
         scores[i] = readDouble("Enter score for judge " + i + ": ");
      }

      double averageScore = mean(scores);
      double standardDev = stdev(scores);
      println("The average score is " + averageScore);
      println("The standard deviation is " + standardDev);
   }
	
	public double mean(double[] array){
		double total = 0;
		
		for(int i = 0; i < array.length; i++){
			total += array[i];
		}
		
		return total / array.length;
	}
	
	public double stdev(double[] array){
		/*Calculate the mean of the distribution as in exercise 2.*/
		double mean = mean(array);
		
		/* Go through the individual data items in the distribution and calculate the square 
		 * of the difference between each data value and the mean. Add all these values to a running total.*/
		double runningTotal = 0;
		for(int i = 0; i < array.length; i++){
			double difference = (array[i] - mean) * (array[i] - mean);
			runningTotal += difference;
		}
		
		/* Take the total from step b and divide it by the number of data items. */
		double totalDivByNum = runningTotal / array.length;
		
		/* Calculate the square root of the resulting quantity, which represents the standard deviation. */
		return Math.sqrt(totalDivByNum);
	}
}
