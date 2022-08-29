/*Write a program that reads in an array of integers and then displays a histogram of those numbers, 
 * divided into the ranges 0–9, 10–19,
 *  20–29, and so forth, up to the range containing only the value 100. 
 *  Your program should generate output that looks as much like the sample run as possible.*/
import acm.program.*;

public class Histogram extends ConsoleProgram{
	public void run(){
		int size = readInt("How many test scores? ");
		int[] scores = new int[size];
		
		for(int i = 0; i < size; i++){
			int tmp = readInt("Test #" + i + ": ");
			while(tmp < 0 || tmp > 100){
				tmp = readInt("Invalid: ");
			}
			scores[i] = tmp;
		}
		
		drawHistogram(scores);
	}
	
	private void drawHistogram(int[] scores){
		String asterisks = "";
		
		print("0-9   | ");
		for(int i = 0; i < scores.length; i++){
			if(scores[i] >= 0 && scores[i] <= 9){
				asterisks += "*";
			}
		}
		println(asterisks);
		asterisks = "";
		
		print("10-19 | ");
		for(int i = 0; i < scores.length; i++){
			if(scores[i] >= 10 && scores[i] <= 19){
				asterisks += "*";
			}
		}
		println(asterisks);
		asterisks = "";
		
		print("20-29 | ");
		for(int i = 0; i < scores.length; i++){
			if(scores[i] >= 20 && scores[i] <= 29){
				asterisks += "*";
			}
		}
		println(asterisks);
		asterisks = "";
		
		print("30-39 | ");
		for(int i = 0; i < scores.length; i++){
			if(scores[i] >= 30 && scores[i] <= 39){
				asterisks += "*";
			}
		}
		println(asterisks);
		asterisks = "";
		
		print("40-49 | ");
		for(int i = 0; i < scores.length; i++){
			if(scores[i] >= 40 && scores[i] <= 49){
				asterisks += "*";
			}
		}
		println(asterisks);
		asterisks = "";
		
		print("50-59 | ");
		for(int i = 0; i < scores.length; i++){
			if(scores[i] >= 50 && scores[i] <= 59){
				asterisks += "*";
			}
		}
		println(asterisks);
		asterisks = "";
		
		print("60-69 | ");
		for(int i = 0; i < scores.length; i++){
			if(scores[i] >= 60 && scores[i] <= 69){
				asterisks += "*";
			}
		}
		println(asterisks);
		asterisks = "";
		
		print("70-79 | ");
		for(int i = 0; i < scores.length; i++){
			if(scores[i] >= 70 && scores[i] <= 79){
				asterisks += "*";
			}
		}
		println(asterisks);
		asterisks = "";
		
		print("80-89 | ");
		for(int i = 0; i < scores.length; i++){
			if(scores[i] >= 80 && scores[i] <= 89){
				asterisks += "*";
			}
		}
		println(asterisks);
		asterisks = "";
		
		print("90-99 | ");
		for(int i = 0; i < scores.length; i++){
			if(scores[i] >= 90 && scores[i] <= 99){
				asterisks += "*";
			}
		}
		println(asterisks);
		asterisks = "";
		
		print("100   | ");
		for(int i = 0; i < scores.length; i++){
			if(scores[i] == 100){
				asterisks += "*";
			}
		}
		println(asterisks);
		asterisks = "";
	}
}
