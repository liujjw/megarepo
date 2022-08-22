package writtenA5;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import controller.Controller;
import controller.ControllerImpl;

/** A class to test the written problems */
class TestWritten {

	@Test
	void test() {
		testSpiral();
		System.out.println("\n*********\n–––––––––––––––––––––––\n*********\n");
		testEatAndBud();
	}

	/**
	 * Tests the spiral critter in a large empty world
	 */
	private void testSpiral() {
		Controller controller = new ControllerImpl();
		assertTrue(controller.loadWorld("src/test/resources/spiralworld.txt"));
		controller.printWorld(System.out);
		for (int i = 0; i < 50; i++) {
			System.out.println();
			controller.advanceTime(2);
			controller.printWorld(System.out);

		}
	}

	/**
	 * Tests the eat and bud critter on small empty world
	 */
	private void testEatAndBud() {
		Controller controller = new ControllerImpl();
		assertTrue(controller.loadWorld("src/test/resources/eat-and-bud_world.txt"));
		controller.printWorld(System.out);
		for (int i = 0; i < 10; i++) {
			System.out.println();
			controller.advanceTime(1);
			controller.printWorld(System.out);

		}
		System.out.println();
		controller.printWorld(System.out);
	}

}
