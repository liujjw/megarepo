package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import model.ReadOnlyWorld;

/** A class to test the Controller implementation */
class TestController {

	@Test
	void test() {
//		testNewWorld();
		testExampleWorld();
	}

	/**
	 * Tests the controller's new world implementation
	 */
	private void testNewWorld() {
		Controller controller = new ControllerImpl();
		assertNull(controller.getReadOnlyWorld());
		controller.newWorld();
		assertNotNull(controller.getReadOnlyWorld());
		controller.printWorld(System.out);
	}

	/**
	 * Tests methods on a world loaded from the example world file
	 * 
	 */
	private void testExampleWorld() {
		Controller controller = new ControllerImpl();
		assertNull(controller.getReadOnlyWorld());
		assertFalse(controller.loadWorld("idonotexist"));
		assertFalse(controller.loadWorld("src/resources/example-critter.txt"));
		assertTrue(controller.loadWorld("src/test/resources/world.txt"));
		assertTrue(controller.loadCritters("src/test/resources/example-critter.txt", 5));
		assertTrue(controller.loadCritters("src/test/resources/example-critter.txt", 0));
		assertFalse(controller.loadCritters("src/test/resources/example-critter.txt", -1));
		System.out.println("\n\nWorld with 5 additional critters:");
		controller.printWorld(System.out);
		controller.advanceTime(1000);
		System.out.println("\nAfter 1000 time steps");
		controller.printWorld(System.out);
		ReadOnlyWorld world = controller.getReadOnlyWorld();
		// Test rocks are still rocks
		assertEquals(world.getTerrainInfo(2, 2), -1);
		assertEquals(world.getTerrainInfo(3, 6), -1);
		assertEquals(world.getTerrainInfo(9, 10), -1);

		controller.advanceTime(0);
		controller.advanceTime(-1);

	}

}
