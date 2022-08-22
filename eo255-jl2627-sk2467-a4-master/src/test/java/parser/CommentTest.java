package parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import ast.Program;
import helpers.TestHelpers;

/** A class to test the parser's response on in-line comments */
public class CommentTest extends TestHelpers {
	@Test
	void test() {
		TestOnlyComments();
		TestEOLComments();
	}

	/**
	 * Tests the results of parser on a string that consists entirely of comments
	 */
	private void TestOnlyComments() {
		String s = "// --> \n// -->;;;[]mem{1223} mem[4]\n";
		Program prog = getProgram(s);
		assertNull(prog);
//		assertEquals(prog.getChildren().size(), 0);
//		assertNull(prog.getParent());
	}

	/**
	 * Tests the results of parser on a String that contains end of line comments
	 */
	private void TestEOLComments() {
		String s = "mem[mem[3] + 10] mod 6 <= 3 --> right; //--> left :=  mem[10] \n mem[mem[3] + 10] mod 6 <= 3 --> right; //hiiiii";
		Program prog = getProgram(s);
		assertNotNull(prog);
		// Make sure there's exactly two children (rules) in the given program
		assertEquals(prog.getChildren().size(), 2);
	}
}
