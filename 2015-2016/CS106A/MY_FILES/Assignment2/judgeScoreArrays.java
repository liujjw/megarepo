/*Because individual judges may have some bias, 
 * it is common practice to throw out the highest and 
 * lowest score before computing the average. Write a program that 
 * reads in scores from a panel of seven judges and then computes the average 
 * of the five scores that remain after discarding the highest and lowest.
 * */

import acm.program.*;

public class judgeScoreArrays extends ConsoleProgram{
	private static final int JUDGES = 7;
	
	public void run(){
		
		double highest = 0;
		double lowest = 0;
		double total = 0;
		
		double[] scores = new double[JUDGES];
		for(int i = 0; i < scores.length; i++){
			scores[i] = readDouble("?");
			if(i == 0){
				highest = scores[i];
				lowest = scores[i];
			}else{
				if(scores[i] > highest) highest = scores[i];
				if(scores[i] < lowest) lowest = scores[i];
			}
			total += scores[i];
		}
		
		total = total - highest - lowest;
		total /= JUDGES - 2;
		println(total);
		
		
	}
}
