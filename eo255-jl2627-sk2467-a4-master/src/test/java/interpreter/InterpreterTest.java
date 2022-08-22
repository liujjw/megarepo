package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import ast.Node;
import ast.Program;
import helpers.TestHelpers;
public class InterpreterTest extends TestHelpers {
	@Test
	void test() {
		// Test condition evaluation of interpreter class..
		String[] conditions = { "(4 * 5) > (3 * (4-3))", "432- 341 < 3312441 or 1242 > 123",
				"43 > 1 and 43 < (3)*(3+2)" };
		int[] expval = { 1, 1, 0 };
		int i = 0;

		for (String c : conditions) {
			Program testProg = getProgram(c + " --> POSTURE := 17;");
			List<Node> kids = testProg.getChildren();

			Node condition = kids.get(0);

			condition = (condition.getChildren()).get(0);

			System.out.println(condition);
			int a = condition.getVal(null);

			System.out.println("EXPECTED: " + expval[i]);
			assertEquals(expval[i], a);

			i = i + 1;
		}
	}

}
