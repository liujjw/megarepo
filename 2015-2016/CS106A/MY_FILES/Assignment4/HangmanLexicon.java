/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will reimplement for Part III of the assignment.
 */

import acm.util.*;
import java.io.*;
import java.util.*;

public class HangmanLexicon {

/** Initializes reading in of a lexicon text file.*/
	public HangmanLexicon(){
		try{
			BufferedReader lexicon = new BufferedReader(new FileReader("HangmanLexicon.txt"));
			while(lexicon.readLine() != null){
				arrayOfWords.add(lexicon.readLine());
			}
		}catch(Exception e){
			throw new ErrorException("invalid file");
		}
	}
	
/** Returns the number of words in the lexicon. */
	public int getWordCount() {
		return arrayOfWords.size();
	}

/** Returns the word at the specified index. */
	public String getWord(int index) {
		return arrayOfWords.get(index);
	}
	
/** ivars*/
	// Arraylist to store words read in from buffered reader
	ArrayList<String> arrayOfWords = new ArrayList<String>();
}


/*
 * 		switch (index) {
			case 0: return "BUOY";
			case 1: return "COMPUTER";
			case 2: return "CONNOISSEUR";
			case 3: return "DEHYDRATE";
			case 4: return "FUZZY";
			case 5: return "HUBBUB";
			case 6: return "KEYHOLE";
			case 7: return "QUAGMIRE";
			case 8: return "SLITHER";
			case 9: return "ZIRCON";
			default: throw new ErrorException("getWord: Illegal index");
		}
*/