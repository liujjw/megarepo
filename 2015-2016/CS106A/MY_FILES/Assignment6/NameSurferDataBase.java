import java.io.*;
import java.util.*;

/*
 * File: NameSurferDataBase.java
 * -----------------------------
 * This class keeps track of the complete database of names.
 * The constructor reads in the database from a file, and
 * the only public method makes it possible to look up a
 * name and get back the corresponding NameSurferEntry.
 * Names are matched independent of case, so that "Eric"
 * and "ERIC" are the same names.
 */

public class NameSurferDataBase implements NameSurferConstants {
	
/* Constructor: NameSurferDataBase(filename) */
/**
 * Creates a new NameSurferDataBase and initializes it using the
 * data in the specified file.  The constructor throws an error
 * exception if the requested file does not exist or if an error
 * occurs as the file is being read.
 */
	public NameSurferDataBase(String filename) {
		try{
			namesFile = new BufferedReader(new FileReader(filename));
			String rd = namesFile.readLine();
			while(rd != null){
				String keyString = rd.substring(0, rd.indexOf(" "));
				keyString = keyString.toUpperCase(); // make it all upper case
				namesMap.put(keyString, rd);
				rd = namesFile.readLine();
			}
			namesFile.close();
		}catch(IOException ex){
			throw new Error("IO Error");
		}
	}
	
/* Method: findEntry(name) */
/**
 * Returns the NameSurferEntry associated with this name, if one
 * exists.  If the name does not appear in the database, this
 * method returns null.
 */
	public NameSurferEntry findEntry(String name){
		name = name.toUpperCase(); // make it all upper case, see above comment, so case-insensitive
		String entryLine = namesMap.get(name);
		if(entryLine == null){ // if name is not found, return null pointer
			return null;
		}
		return (new NameSurferEntry(entryLine));
	}
	
	/* Instance variables*/
	private HashMap<String, String> namesMap = new HashMap<String, String>();
	BufferedReader namesFile;
}

