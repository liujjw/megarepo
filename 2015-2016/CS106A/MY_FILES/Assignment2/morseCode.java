/*Write a program that reads in a string from the user 
 * and translates each letter in the string to its equivalent in Morse code, using periods to 
 * represent dots and hyphens to represent dashes. Separate words in the output by calling println
 *  whenever you encounter a space in the input, but ignore all other punctuation characters. 
 *  */

import acm.program.*;

public class morseCode extends ConsoleProgram{
	private static final String[] morseEncoding = {
			".- ",
			"-... ",
			"-.-. ",
			"-.. ",
			". ",
			"..-. ",
			"--. ",
			".... ",
			".. ",
			".--- ",
			"-.- ",
			".-.. ",
			"-- ",
			"-. ",
			"--- ",
			".--. ",
			"--.- ",
			".-. ",
			"... ",
			"- ",
			"..- ",
			"...- ",
			".-- ",
			"-..- ",
			"-.-- ",
			"--.. "
	};

	public void run(){
		/* Array to encode alphabet in Morse */
		String line = readLine("Message: ");
		line = line.toUpperCase();
		
		for(int i = 0; i < line.length(); i++){
			
			char oneChar = line.charAt(i);
			
			if(Character.isLetter(oneChar)){
				int character = oneChar - 'A';
				
				switch(character){
				case 0:
					print(morseEncoding[0]);
					break;
				case 1:
					print(morseEncoding[1]);
					break;
				case 2:
					print(morseEncoding[2]);
					break;
				case 3:
					print(morseEncoding[3]);
					break;
				case 4:
					print(morseEncoding[4]);
					break;
				case 5:
					print(morseEncoding[5]);
					break;
				case 6:
					print(morseEncoding[6]);
					break;
				case 7:
					print(morseEncoding[7]);
					break;
				case 8:
					print(morseEncoding[8]);
					break;
				case 9:
					print(morseEncoding[9]);
					break;
				case 10:
					print(morseEncoding[10]);
					break;
				case 11:
					print(morseEncoding[11]);
					break;
				case 12:
					print(morseEncoding[12]);
					break;
				case 13:
					print(morseEncoding[13]);
					break;
				case 14:
					print(morseEncoding[14]);
					break;
				case 15:
					print(morseEncoding[15]);
					break;
				case 16:
					print(morseEncoding[16]);
					break;
				case 17:
					print(morseEncoding[17]);
					break;
				case 18:
					print(morseEncoding[18]);
					break;
				case 19:
					print(morseEncoding[19]);
					break;
				case 20:
					print(morseEncoding[20]);
					break;
				case 21:
					print(morseEncoding[21]);
					break;
				case 22:
					print(morseEncoding[22]);
					break;
				case 23:
					print(morseEncoding[23]);
					break;
				case 24:
					print(morseEncoding[24]);
					break;
				case 25:
					print(morseEncoding[25]);
					break;
				case 26:
					print(morseEncoding[26]);
					break;
				default:
					print("");
					break;
				}
			}else if(Character.isWhitespace(oneChar)){
				println("");
			}
		}
	}
	
	/*
	 * 1. reads string from user 
	 * 2. parse string using string instance methods 
	 * 3. check if letter
	 * 4. make an array of morse encodings
	 * 5. match letter with morse encoding and print to screen
	 * 6. print spaces and other punctutation formatted accordingly*/
}
