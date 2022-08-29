import acm.program.*;
import acm.util.*;

public class randomWordTest extends ConsoleProgram{
	
	private static final int MAX = 15;
	private static final int MIN = 5;
	
	public void run(){
		println("This program creates and displays five random \"words\" within a range of MAX and MIN letters.");
		for(int i = 100; i > 0; i -= 20){
			String newWord = randomWord();
			println(newWord);
		}
	}
	
	private String randomWord(){
		String word = "";
		int numberOfChars = rgen.nextInt(MIN, MAX);
		for(int i = 0; i < numberOfChars; i++){
			int charAsInt = rgen.nextInt((int)'a', (int)'z');
			word += (char) charAsInt;
		}
		return word;
	}
	
	private RandomGenerator rgen = new RandomGenerator();
	
}
