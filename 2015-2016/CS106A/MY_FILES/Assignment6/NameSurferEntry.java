/*
 * File: NameSurferEntry.java
 * --------------------------
 * This class represents a single entry in the database.  Each
 * NameSurferEntry contains a name and a list giving the popularity
 * of that name for each decade stretching back to 1900.
 */

import acm.util.*;
import java.util.*;

public class NameSurferEntry implements NameSurferConstants {

/* Constructor: NameSurferEntry(line) */
/**
 * Creates a new NameSurferEntry from a data line as it appears
 * in the data file.  Each line begins with the name, which is
 * followed by integers giving the rank of that name for each
 * decade.
 */
	public NameSurferEntry(String line) {
		myLine = line;
		
		int firstSpace = myLine.indexOf(" ");
		name = myLine.substring(0, firstSpace); 
		
		String snippedString = myLine; 
		for(int i = 0; i < NDECADES; i++){ // repeat the number of decades times
			int offset = snippedString.indexOf(" ");  // offset by the first occurrence of a space each time
			snippedString = snippedString.substring(offset + 1); // new string will now start at the beginning of the first occurrence of a space + 1,
																	// when run next time, the next space after this one will start the new string
			int endIndex = snippedString.indexOf(" ");
			String rankString;
			if(endIndex == -1){
				rankString = snippedString.substring(0);
			}else{
				rankString = snippedString.substring(0, endIndex);
			} 
			int intRank = Integer.parseInt(rankString); 
			decadeRank[i] = intRank; 
		}
	}
	
	

/* Method: getName() */
/**
 * Returns the name associated with this entry.
 */
	public String getName() {
		return name;
	}

	
	
/* Method: getRank(decade) */
/**
 * Returns the rank associated with an entry for a particular
 * decade.  The decade value is an integer indicating how many
 * decades have passed since the first year in the database,
 * which is given by the constant START_DECADE.  If a name does
 * not appear in a decade, the rank value is 0.
 */
	public int getRank(int decade) {
		return decadeRank[decade];
	}

	
	
/* Method: toString() */
/**
 * Returns a string that makes it easy to see the value of a
 * NameSurferEntry.
 */
	public String toString() {
		return myLine.toString();
	}
	
	
	
	/* Instance variables for storing the line*/
	private String myLine;
	private String name;
	
	private int[] decadeRank = new int[NDECADES];
}

