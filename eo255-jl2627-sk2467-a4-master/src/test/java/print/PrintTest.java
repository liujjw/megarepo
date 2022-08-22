package print;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ast.Program;
import helpers.TestHelpers;
import parse.ParserFactory;

/** A class to test the pretty-print ability of our AST */
public class PrintTest extends TestHelpers {

	@Test
	void test() {
		testParsePrintEquality();
		testNegatives();
		testParentheses();
		testBrackets();
	}

	/**
	 * Tests whether the AST of a program is the same after two parses and prints.
	 * It does this by testing is whether parsing, printing, parsing, and printing
	 * again prints the same exact string both times.
	 */
	private void testParsePrintEquality() {
		for (String loc : samplePrograms) {
			Program prog = ParserFactory.getParser().parse(getReader(loc));
			String progString = prog.toString();
			assertEquals(progString, getProgram(progString).toString());
		}
	}

	/**
	 * Tests whether a program with a variable number of brackets {} surrounding an
	 * expression will pretty print the same number of parentheses back
	 */
	private void testBrackets() {
		for (int i = 0; i < 7; i++) {
			String s = "{ ".repeat(i);
			s += "10 = 10 and 3 = 3";
			s += " }".repeat(i);
			s += " --> bud;";
			assertEquals(getProgram(s).toString().replaceAll("\\s+", ""), s.replaceAll("\\s+", ""));
		}
	}

	/**
	 * Tests whether a program with a variable number of parentheses () surrounding
	 * an expression will pretty print the same number of parentheses back
	 */
	private void testParentheses() {
		for (int i = 0; i < 7; i++) {
			String s = "( ".repeat(i);
			s += "(10 * 3)";
			s += " )".repeat(i);
			s += "= 30 --> bud;";
			assertEquals(getProgram(s).toString().replaceAll("\\s+", ""), s.replaceAll("\\s+", ""));
		}
	}

	/**
	 * Tests whether a program with a variable number of negatives '-' surrounding
	 * an expression will pretty print the same number of parentheses back
	 */
	private void testNegatives() {
		for (int i = 0; i < 7; i++) {
			String s = "-".repeat(i);
			s += "(10 * 3) = 30 --> bud;";
			assertEquals(getProgram(s).toString().substring(0, i), "-".repeat(i));
		}
	}
}
