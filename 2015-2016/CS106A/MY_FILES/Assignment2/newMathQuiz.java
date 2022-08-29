// questions in base 8

import acm.program.*;
import acm.util.*;

public class newMathQuiz extends ConsoleProgram{
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
				
				checkAnswer(readLine("What is " + summand1 + " + " + summand2 + " in base 8? "), solution);
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
				
				checkAnswer(readLine("What is " + subtraction1 + " - " + subtraction2 + " in base 8? "), solution);
			}
		}
		
	}
	
	private void checkAnswer(String answer, int solution){
		
		/*Change answer to octal*/
		int octalAnswer = Integer.parseInt(answer, 8);
		
		if(octalAnswer == solution){
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
			while(octalAnswer != solution){
				answer = readLine("Incorrect. Try again: ");
				octalAnswer = Integer.parseInt(answer, 8);
				attempts++;
				if(attempts == 3){
					String solutionInOctal = Integer.toString(solution, 8);
					println("Sorry, the answer is " + solutionInOctal + ".");
					break;
				}
			}
		}
	}
	
	private RandomGenerator rgen = new RandomGenerator();
}
