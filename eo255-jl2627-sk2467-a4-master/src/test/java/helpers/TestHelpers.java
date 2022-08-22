package helpers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import ast.Program;
import parse.Parser;
import parse.ParserFactory;

/** A class to hold all necessary and reused methods in other test classes */
public abstract class TestHelpers {
	public final String[] samplePrograms = {"parser/mutated_critter_1.txt", "parser/mutated_critter_2.txt",
            "parser/mutated_critter_3.txt", "parser/mutated_critter_4.txt", "parser/mutated_critter_5.txt",
            "parser/mutated_critter_6.txt", "parser/draw_critter.txt", "parser/example-rules.txt"};
	// Holds the locations of all the files containing sample programs

	/**
	 * Gets a given reader from a file at the given location
	 * 
	 * @param loc file location
	 * @return reader
	 */
	public Reader getReader(String loc) {
		InputStream in = ClassLoader.getSystemResourceAsStream(loc);
		Reader r = new BufferedReader(new InputStreamReader(in));
		return r;
	}

	/**
	 * Returns the program resulting from parsing the given string
	 * 
	 * @param s string to be parsed
	 * @return parsed program
	 */

	public Program getProgram(String s) {
		InputStream in = new ByteArrayInputStream(s.getBytes());
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser parser = ParserFactory.getParser();
		Program prog = parser.parse(r);
		return prog;
	}
}
