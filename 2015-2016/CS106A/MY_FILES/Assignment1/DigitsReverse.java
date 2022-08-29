import acm.program.*;

public class DigitsReverse extends ConsoleProgram{
	public void run(){
		println("This program reverses the order of the digits entered.");
		int original_number = readInt("Enter a positive integer: ");
		int reversed = 0;
		int beforeAddtoReverse = 0;
		while(original_number > 0){
			beforeAddtoReverse = original_number % 10;
			while(beforeAddtoReverse < original_number){
				if(beforeAddtoReverse == 0){
					break;
				}
				beforeAddtoReverse *= 10;	
			}
			// todo?
			reversed += beforeAddtoReverse;
			original_number /= 10; 
		}
		println("The reversed number is: " + reversed);
	}

}
