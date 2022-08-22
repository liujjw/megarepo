package parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ast.Node;
import ast.Program;
import helpers.TestHelpers;
import parse.Parser;
import parse.ParserFactory;

/** This class contains tests for the Critter parser. */
class ParserTest extends TestHelpers {

	@Test
	void test() {
		for (String loc : samplePrograms) {
			testLinks(loc);
			testCloneLinks(loc);
			testNodeAt(loc);
			testClone(loc);
		}

		testIllegalPrograms();
	}

	/**
	 * Tests whether a clone of the parsed program pretty-prints the same program as
	 * the original
	 * 
	 * @param loc location of program to be parsed
	 */
	private void testClone(String loc) {
		Program prog = ParserFactory.getParser().parse(getReader(loc));
		Node clone = prog.clone();
		assertEquals(prog.toString(), clone.toString());
	}

	/**
	 * Tests whether the links in a cloned version of a given program are correct
	 * 
	 * @param loc location of file to be parsed and cloned
	 */
	private void testCloneLinks(String loc) {
		Program prog = ParserFactory.getParser().parse(getReader(loc));
		Node n = prog.clone();
		testChildren(n);
	}

	/**
	 * Tests various illegal programs to ensure that parse throws an error and
	 * returns null
	 */
	private void testIllegalPrograms() {
		String s1 = "(3*4))= 12 --> bud;"; // no open parenthesis
		String s2 = "((3*4) = 12 --> bud;"; // no close parenthesis
		String s3 = "(3*4) = 12--> ;"; // no command
		String s4 = "(3*4) = 12 --> 4;"; // command is just an expr
		String s5 = "(3*4) = 12 --> bud"; // no semicolon
		String s6 = "(3*4) = 12 -> bud;"; // arrow is not complete
		String s7 = "bud --> grow"; // condition cannot be action
		String s8 = "{3 + 4} --> serve[3]"; // 3 + 4 is not a condition since there is no rel
		String s9 = "{1=1 or 2=2 --> bud;"; // no close brace
		String s10 = "{1=1 or and 2=2} --> bud;"; // two relational operators in a row
		String s11 = ""; // Empty string
		String[] progStrings = { s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11 };
		for (String s : progStrings) {
			assertNull(getProgram(s));
		}
	}

	/**
	 * Checks that a valid critter program located at s is not {@code null} when
	 * parsed, and returnes parsed program.
	 * 
	 * @param r reader to be read from
	 * @return Parsed program if not null
	 */
	@Test
	private Program testProgramIsNotNull(Reader r) {
		Parser parser = ParserFactory.getParser();
		Program prog = parser.parse(r);
		assertNotNull(prog, "A valid critter program should not be null.");
		return prog;
	}

	/**
	 * Tests that AbstractNode's nodeAt() returns a distinct node for each index in
	 * range [0, parser.size), and that any index outside this range throws an
	 * IndexOutOfBoundsException
	 * 
	 * @param loc location of file to be read
	 */
	private void testNodeAt(String loc) {
		Program prog = ParserFactory.getParser().parse(getReader(loc));
		List<Node> passedNodes = new ArrayList<Node>();
		for (int i = 0; i < prog.size(); i++) {
			final int j = i;
			assertThrows(IndexOutOfBoundsException.class, () -> {
				prog.nodeAt(-1 * (j + 1));
			});
			assertThrows(IndexOutOfBoundsException.class, () -> {
				prog.nodeAt(j + prog.size());
			});
			Node node = prog.nodeAt(i);
			assertFalse(passedNodes.contains(node));
			passedNodes.add(node);
		}
	}

	/**
	 * Checks that the AST created from a critter program has valid links, i.e. all
	 * children of a given node have that node as parent
	 * 
	 * @param loc location of file to be read
	 */
	@Test
	private void testLinks(String loc) {
		Reader r = getReader(loc);
		Program p = testProgramIsNotNull(r);
		testChildren(p);
	}

	/**
	 * Tests that the children of a given node have valid
	 * 
	 * @param root
	 */
	@Test
	private void testChildren(Node root) {
		if (root == null)
			return;
		for (Node child : root.getChildren()) {
			assertTrue(hasValidChildren(child));
			testChildren(child);
		}
	}

	/**
	 * Tests whether a given node has children who all point to itself as their
	 * parent
	 * 
	 * @param node node to be tested
	 * @return whether all children point back to the node as their parent
	 */
	private boolean hasValidChildren(Node node) {
		if (node == null)
			return true;
		for (Node child : node.getChildren()) {
			if (child.getParent() == null || child.getParent() != node)
				return false;
		}
		return true;
	}

}
