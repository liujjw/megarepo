import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.awt.*;

public class superClassWithProfuseMethods extends ConsoleProgram{
	
	/**********************************************************RUN Methods************************************************************************/

	/*
	public void run(){
		// do stuff
		int day = readInt("Give me a day: ");
		while(day > 31){
			day = readInt("Give me a VALID day: ");
		}
		
		int month = readInt("Give me a month: ");
		while(month > 12){
			month = readInt("Give me a VALID month: ");
		}
		
		int year = readInt("Give me a year: ");
		println(dateString(day, month, year));
	}
	*/
	
	/*
	public void run(){
		
		String stringToCompute = readLine("Give me an all caps word to total: ");
		println(wordPoints(stringToCompute) + "points");
	}
	*/
	
	/*
	public void run(){
		String word1 = "walk";
		println(presentParticiple(word1));
		
		String word2 = "rap";
		println(presentParticiple(word2));
		
		String word3 = "dance";
		println(presentParticiple(word3));
		
		String word4 = "see";
		println(presentParticiple(word4));
		
		String word5 = "mourn";
		println(presentParticiple(word5));
	}
	*/
	/*
	public void run(){
		while(true){
			String s = readLine("What's your secret message? ");
			int n = readInt("How many characters to shift? ");
			println(caesarEncode(s, n));
		}
	}
	*/
	
	/*
    public void run() {
        while (true) {
           String digits = readLine("Enter a numeric string: ");
           if (digits.length() == 0) break;
           println(addCommasToNumericString(digits));
        } 	
     }
     */
	
	public void run(){
		while(true){
			String text = readLine("Is it a palindrome? ");
			println(isPalindrome(text));
		}
	}
	
	/*
	public void run(){
		String s = "Mrco rubio";
		s = s.substring(s.length() - 12);
		println(s);
	}
	*/
	
	/**********************************************************Methods***************************************************************************/
	
	///////////////////////////////////////////////////////// Date generator ///////////////////////////////////////////////////////////////////
	private String dateString(int day, int month, int year){
		String newString = "";
		
		/*Param processing*/
		String monthString = "";
		switch(month){
		case 1: monthString = "Jan"; break;
		case 2: monthString = "Feb"; break;
		case 3: monthString = "Mar"; break;
		case 4: monthString = "Apr"; break;
		case 5: monthString = "May"; break;
		case 6: monthString = "Jun"; break;
		case 7: monthString = "Jul"; break;
		case 8: monthString = "Aug"; break;
		case 9: monthString = "Sep"; break;
		case 10: monthString = "Oct"; break;
		case 11: monthString = "Nov"; break;
		case 12: monthString = "Dec"; break;
		default: monthString = "invalid month"; break;
		}
		
		String hyphen = "-";
		
		String yearString = "" + year;
		if(year > 1000){
			yearString = yearString.substring(2);
		}else{
			yearString = "i only do 20th and 21st century dude";
		}
		
		/**/
		
		newString = day + hyphen + monthString + hyphen + yearString;
		return newString;
	}
	
	//////////////////////////////////////////////////// Scrabble Word points /////////////////////////////////////////////////////
	private int wordPoints(String stringToCompute){
		
		int pointsTotal = 0;
		
		for(int i = 0; i < stringToCompute.length(); i++){
			char eachChar = stringToCompute.charAt(i);
				
			switch(eachChar){
			case 'A': case 'E': case 'I': case 'O': case 'U': case 'L': case 'N': case 'R': case 'S': case 'T':
				pointsTotal += 1;
				break;
			case 'D': case 'G':
				pointsTotal += 2;
				break;
			case 'B': case 'C': case 'M': case 'P':
				pointsTotal += 3;
				break;
			case 'F': case 'H': case 'V': case 'W': case 'Y':
				pointsTotal += 4;
				break;
			case 'K':
				pointsTotal += 5;
				break;
			case 'J': case 'X':
				pointsTotal += 8;
				break;
			case 'Q': case 'Z':
				pointsTotal += 10;
				break;
			default:
				pointsTotal += 0;
				break;
			}
		}
		
		return pointsTotal;
	}
	
	
	/////////////////////////////////////////////////////////My Index Of /////////////////////////////////////////////////////////
	public int myIndexOf(String stringToIndex){ /*Object receiver paradigm? What string object to act on? */
		for(int i = 0; i < objectString.length(); i++/*theoretical object string*/){
			String aChracter = objectString.substring(i, i + 1);
			if(stringToIndex.equals(aCharacter)){
				return i;
			}
		}
		return -1;
	}
	
	////////////////////////////////////////////////////////Regular Plural Form of word////////////////////////////////////////////////////////
	private String regularPluralForm(String word){

		int endingIndex = word.length() - 1;
		
		String twoEndingCharacters = word.substring(endingIndex - 1);
		String oneEndingCharacter = word.substring(endingIndex);
		
		
		/*a. If the word ends in s, x, z, ch, or sh, add es to the word.*/
		if(twoEndingCharacters.equalsIgnoreCase("ch") || twoEndingCharacters.equalsIgnoreCase("sh")
				|| oneEndingCharacter.equalsIgnoreCase("s") || oneEndingCharacter.equalsIgnoreCase("x")
				|| twoEndingCharacters.equalsIgnoreCase("z")){
			String pluralWord = word;
			return pluralWord + "es";
		}
		
		/*b. If the word ends in y and the y is preceded by a consonant, change the y to ies.*/
		if(oneEndingCharacter.equalsIgnoreCase("y")){
			String precedingYCharacter = word.substring(endingIndex - 1, endingIndex);
			if(precedingYCharacter.equalsIgnoreCase("a") || precedingYCharacter.equalsIgnoreCase("e") ||
					precedingYCharacter.equalsIgnoreCase("i") || precedingYCharacter.equalsIgnoreCase("o") ||
					precedingYCharacter.equalsIgnoreCase("u")){
				String pluralWord = word;
				return pluralWord + "s";
			}else{
				String pluralWord = word;
				pluralWord = pluralWord.substring(0, endingIndex);
				return pluralWord + "ies";
			}
		}
		
		/*c. In all other cases, add just an s.*/
		String pluralWord = word;
		return pluralWord + "s";
	}

	///////////////////////////////////////////////Present participle of a verb/////////////////////////////////////////////////////////////
	private String presentParticiple(String verb){
	
	int endIndex = verb.length() - 1;
	String endingChar = verb.substring(endIndex);
	String endingTwoChars = verb.substring(endIndex - 1);
	String secondToLastChar = verb.substring(endIndex - 1, endIndex);	
	
	/*If the word ends in an e preceded by a consonant, take the e away before adding the ing suffix. 
	 * Thus, move should become moving. If the e is not preceded by a consonant, it should remain in place, so that see becomes seeing.
	 */
	if(endingChar.equals("e")){
		if(secondToLastChar.equalsIgnoreCase("a") || secondToLastChar.equalsIgnoreCase("e") ||
				secondToLastChar.equalsIgnoreCase("i") || secondToLastChar.equalsIgnoreCase("o") ||
				secondToLastChar.equalsIgnoreCase("u")){
			return verb + "ing";
		}else{
			String newVerb = verb;
			newVerb = newVerb.substring(0, endIndex); /*non-inclusive of last index specified*/
			return newVerb + "ing";
		}
	}
		
	/*If the word ends in a consonant preceded by a vowel, insert an extra copy of that consonant before adding the ing 
	 * suffix. Thus, jam should become jamming. If, however, there is more than one consonant at the end of the word, 
	 * no such doubling takes place, so that walk becomes walking.
	 */
	else if(!endingChar.equalsIgnoreCase("a") || !endingChar.equalsIgnoreCase("e") ||
			!endingChar.equalsIgnoreCase("i") || !endingChar.equalsIgnoreCase("o") ||
			!endingChar.equalsIgnoreCase("u")){
		if(secondToLastChar.equalsIgnoreCase("a") || secondToLastChar.equalsIgnoreCase("e") ||
				secondToLastChar.equalsIgnoreCase("i") || secondToLastChar.equalsIgnoreCase("o") ||
				secondToLastChar.equalsIgnoreCase("u")){
			String newVerb = verb;
			newVerb = newVerb + endingChar;
			return newVerb + "ing";
		}else{
			return verb + "ing";
		}
	}
		
	/*In all other circumstances, simply add the ing suffix.*/
	return verb + "ing";
	
	}
	
	///////////////////////////////////////////////////Ordinal Form of a positive counting integer////////////////////////////////////////////////////////
	private String ordinalForm(int n){
		/*Error checking*/
		if(n < 0){
			return "Only positive integers";
		}
		
		/*Numbers ending in the digit 1, 2, and 3, take the suffixes "st", "nd", and "rd", respectively, 
		 * unless the number ends with the two- digit combination 11, 12, or 13. Those numbers, and 
		 * any numbers not ending with a 1, 2, or 3, take the suffix "th".
		 */
		// type casting into a char for use with char methods in char class, however we dont want it to be translated from a character constant,
		// ie only char representation of an int
		// or just as a string, more versatile
		String number = "" + n;
		String endingInt = number.substring(number.length() - 1);
		
		if(endingInt.equals("1")){
			if(number.length() >= 2){
				String secondToLastInt = number.substring(number.length() - 2, number.length() -1);
				if(secondToLastInt.equals("1")){
					return number + "th";
				}
			}
			return number + "st";
		}
		else if(endingInt.equals("2")){
			if(number.length() >= 2){
				String secondToLastInt = number.substring(number.length() - 2, number.length() -1);
				if(secondToLastInt.equals("1")){
					return number + "th";
				}
			}
			return number + "nd";
		}
		else if(endingInt.equals("3")){
			if(number.length() >= 2){
				String secondToLastInt = number.substring(number.length() - 2, number.length() -1);
				if(secondToLastInt.equals("1")){
					return number + "th";
				}
			}
			return number + "rd";
		}else{
			return number + "th";
		}
	}
	
	//////////////////////////////////////////////Caesar Cipher/////////////////////////////////////////////////////////////////
	/**
	 * Implements a Caesar Cipher message encoder, which shifts each letter by some amount. 
	 * Usage: pass a string message "message" to encode by "shift" amount of characters to return an encoded message.
	 * @param message
	 * @param shift
	 * 
	 * Only letters are shifted, and all non-alphabetic characters remain unchanged.
	 */
	
	private String caesarEncode(String message, int shift){
		String encodedMessage = ""; /*Returns an encoded message*/
		
		for(int i = 0; i < message.length(); i++){ /*Loops through for each character in message.*/
			
			char ithCharacter = message.charAt(i); /*Takes the ith character. */
			
			if((ithCharacter >= 'A' && ithCharacter <= 'Z') || (ithCharacter >= 'a' && ithCharacter <= 'z')){ /*Only if the char is alphabetic.*/
				
				/*A Caesar cipher is cyclic in the sense that any operations take shift a letter 
				 * beyond Z simply circle back to the beginning and start over again with A.
				 * 				-Art and Science of Java Exercise Specification
				 * */
				
				int calculatedShift = 0; /*Variable to hold calculated shift amount, accounting for cyclic nature.*/
				
				if(ithCharacter <= 'Z' && ithCharacter >= 'A'){ /*For upper-case characters*/
					
					int howFarFromA = ithCharacter - 'A'; /*How far the capital letter is from A, eg B is 66, so 1 away from A which is 65*/
					int numberOfAlphas = ('Z' - 'A') + 1; /*Number of alphabets is difference between z and a, which is 26*/
					int shiftWithinNumberOfAlphas = shift % numberOfAlphas; /*remainder the shift by 26 to get a shift within 26*/
					
					calculatedShift = (((howFarFromA + shiftWithinNumberOfAlphas) % numberOfAlphas) + numberOfAlphas) % numberOfAlphas;
					
					/*eg: B is 1 from A, add a shift of 29 % 26 to get a shift of 3, because ultimately the 3 is just gonna go to the next cycle 
					 * of shifting, we can only shift by 26 before we just loop back and shift, now we have 4, which is how far from a we are now
					 * (the shift plus 1 from B is preserved), we remainder by 26 to keep within a total shift of less than 26, add 26 and finally 
					 * remainder by 26 to get the remaining shift difference from a, we add 26 and then mod because if we have a negative shift
					 * that means we loop back, and say for example we have total negative shift of 25 from a posotion of A, that means we loop to Z 
					 * and go back 25 from there, which is just 26- 25 =1 which means B*/
					
					ithCharacter = (char) ('A' + calculatedShift); /*ithCharacter is now shifted by the calculated shift from a*/
					
				}else{ /*Lower case*/
					int howFarFroma = ithCharacter - 'a'; 
					int numberOfAlphas = ('z' - 'a') + 1; 
					int shiftWithinNumberOfAlphas = shift % numberOfAlphas; 
					
					calculatedShift = (((howFarFroma + shiftWithinNumberOfAlphas) % numberOfAlphas) + numberOfAlphas) % numberOfAlphas;
					
					ithCharacter = (char) ('a' + calculatedShift); 
				}
				
				encodedMessage += ithCharacter; /*Add each char to encoded message*/
			
			}else{ /*If not a alphabet, do nothing*/
				encodedMessage += ithCharacter;
			}
		
		}
		
		return encodedMessage;
	
	}
	
	///////////////////////////////////////////////////3 Digit Comma Separator/////////////////////////////////////////////////////
    private String addCommasToNumericString(String digits){
    	
    	if(digits.length() <= 3){
    		return digits;
    	}else{
        	String addedCommas = "";

    		int i = 0;
        	for(i = digits.length(); i > 3; i -= 3){ // start i index from end of digit string, stops iterating once there are less than 3 digits left
    			int ithIndex = i - 1; // index is current i index minus 1 to account for zero indexing
    			// take the substring from the i index until the i index (substring param2 is non-inclusive) which makes a 3s grouping and add a comma before it
    			// add that before the previous substring 
    			addedCommas = ("," + digits.substring(ithIndex - 2, ithIndex + 1)) + addedCommas; 
    		}
        	// take the remaining substring not in a group of 3 and append to new substring (i cancels out: param2 is non-inclusive, i - 1 is actual index
        	// so effect is last digit i is included)
    		addedCommas = digits.substring(0, i) + addedCommas;
    		
    		return addedCommas;
    	}	
    }
    
    ///////////////////////////////////////////////////////Is Palindrome predicate method///////////////////////////////////////////////////
    private boolean isPalindrome(String text){
    	String reversed = "";
    	
    	for(int i = text.length(); i > 0; i--){
    		int index = i - 1;
    		char aCharacter = text.charAt(index);
    		if(Character.isLetter(aCharacter)){
        		reversed += text.substring(index, index + 1);
    		}
    	}
    	
    	String allLetterText = "";
    	for(int i = 0; i < text.length(); i++){
    		char aCharacter = text.charAt(i);
    		if(Character.isLetter(aCharacter)){
    			allLetterText += text.substring(i, i + 1);
    		}
    	}
    	
    	if(reversed.equalsIgnoreCase(allLetterText)){
    		return true;
    	}else{
    		return false;
    	}
    }
}
