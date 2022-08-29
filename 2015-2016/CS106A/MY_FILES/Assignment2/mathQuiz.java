import acm.program.*;
import acm.util.*;

public class mathQuiz extends ConsoleProgram{
	private static final int NUMBER_OF_QUESTIONS = 5;
	
	public void run(){
		for(int i = 0; i < NUMBER_OF_QUESTIONS; i++){
			if(rgen.nextBoolean()){
				// addition problem
				int summand1 = rgen.nextInt(0, 20);
				int summand2 = rgen.nextInt(0, 20);
				int solution = summand1 + summand2;
				
				while(solution > 20){
					summand1 = rgen.nextInt(0, 20);
					summand2 = rgen.nextInt(0, 20);
					solution = summand1 + summand2;
				}
				
				checkAnswer(readInt("What is " + summand1 + " + " + summand2 + " ?"), solution);
			}else{
				// subtraction problem
				int subtraction1 = rgen.nextInt(0, 20);
				int subtraction2 = rgen.nextInt(0, 20);
				int solution = subtraction1 - subtraction2;
				
				while(solution < 0){
					subtraction1 = rgen.nextInt(0, 20);
					subtraction2 = rgen.nextInt(0, 20);
					solution = subtraction1 - subtraction2;
				}
				
				checkAnswer(readInt("What is " + subtraction1 + " - " + subtraction2 + " ?"), solution);
			}
		}
		
	}
	
	private void checkAnswer(int answer, int solution){
		if(answer == solution){
			int encouragement = rgen.nextInt(1, 5);
			switch(encouragement){
			case 1:
				println("Correct!");
				break;
			case 2:
				println("Awesome!");
				break;
			case 3:
				println("You got this!");
				break;
			default:
				println("Hurray!");
				break;
			}
		}else{
			int attempts = 1;
			while(answer != solution){
				answer = readInt("Incorrect. Try again: ");
				attempts++;
				if(attempts == 3){
					println("Sorry, the answer is " + solution + ".");
					break;
				}
			}
		}
	}
	
	private RandomGenerator rgen = new RandomGenerator();
}
