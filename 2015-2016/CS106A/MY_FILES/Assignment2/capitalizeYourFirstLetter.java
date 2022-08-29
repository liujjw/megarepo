import acm.program.*;

public class capitalizeYourFirstLetter extends ConsoleProgram{

	public void run(){
		String myString = readLine("Give me a string to capitalize the first letter: ");/*Should take in any sentence or single word and
		 																				capitalize the first OCCURENCE of a char in the string
		 																				making the rest lower-case.*/
		String capitalizedString = capitalize(myString);
		println(capitalizedString);
	}
	
	private String capitalize(String myString){
		String newString = makeNewString(findFirstLetter(myString), myString);
		return newString;
	}
	
	private int findFirstLetter(String myString){
		int indexOfFirstString = 0;
		
		for(int i = 0; i < myString.length(); i++){
			char myChar = myString.charAt(i);
			if((myChar >= 'A' && myChar <= 'Z') || (myChar >= 'a' && myChar <= 'z')){
				indexOfFirstString = i;
				break;
			}
		}
		
		return indexOfFirstString;
		
	}
	
	private String makeNewString(int indexOfFirstString, String myString){
		String newStringUpper = myString.substring(indexOfFirstString, indexOfFirstString + 1);
		String newStringLower = myString.substring(indexOfFirstString + 1);
		
		newStringUpper = newStringUpper.toUpperCase();
		newStringLower = newStringLower.toLowerCase();
		
		String newString = newStringUpper + newStringLower;
		return newString;
		
	}
	
}
