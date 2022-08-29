import acm.program.*;

public class allUppercaseConsonants extends ConsoleProgram{
	
	public void run(){
		println("This program displays all uppercase consonants.");
		for(int i = 'A'; i <= 'Z'; i++){
			if(isEnglishConsonant((char) (i) )){
				println((char) (i));
			}
		}
	}
	
	private boolean isEnglishConsonant(char letter){
		// if not a char
		if(letter < 'A' || (letter > 'Z' && letter < 'a') || letter > 'z'){
			return false;
		}
		
		// for portability purposes switch make uppercase into lower 
		if(letter <= 'Z' && letter >= 'A'){
			letter += 'a' - 'A';
		}
		
		switch(letter){
		case 'a': case 'e': case 'i': case 'o': case 'u':
			return false;
		default:
			return true;
		}
	}
}
