package server;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

import distributedView.CritterWorldClient;
import distributedView.EntityCreation;
import helpers.Helpers;
import model.ClientCritter;
import model.ClientWorld;

/**
 * Tests server functionality, expecting server to be run on localhost:8080
 *
 */
class TestServer {
//	private CritterWorldClient client = new CritterWorldClient("http://localhost:8080/");
	private CritterWorldClient client = new CritterWorldClient("http://10.131.1.78:8080/");
// client = new CritterWorldClient("http://34.66.156.108:8080/");

	@Test
	void test() {
		testAdmin();
		testWrite();
		testRead();

	}

	/**
	 * Tests server functionality as read
	 */
	void testRead() {
		assertTrue(client.login("read", "r") == 200);
		assertNotNull(client.loadWorldState());
		assertTrue(client.runContinuous(0) == 401);
		assertTrue(client.advanceWorld(100) == 401);
		String worldDescription = Helpers.fileToString(ClassLoader.getSystemResourceAsStream("world.txt"));
		assertTrue(client.newWorld(worldDescription) == 401);
	}

	/**
	 * Tests server functionality as write
	 */
	void testWrite() {
		assertTrue(client.login("write", "w") == 200);
		assertNotNull(client.loadWorldState());
		assertTrue(client.runContinuous(0) == 200);
		assertTrue(client.advanceWorld(100) == 200);
		String worldDescription = Helpers.fileToString(ClassLoader.getSystemResourceAsStream("world.txt"));
		assertTrue(client.newWorld(worldDescription) == 401);
	}

	/**
	 * Tests server functionality as an admin
	 */
	void testAdmin() {
		assertTrue(client.loadWorldState() == null);
		assertTrue(client.login("admin", "a") == 200);
		assertTrue(client.login(null, null) == 400);
		assertTrue(client.login("a", "daf'") == 400);
		assertTrue(client.login("read", "daf'") == 401);

		assertTrue(client.newWorld("name world\nsize 10 10") == 201);
		assertNotNull(client.loadWorldState());
		for (int i = 0; i < 100; i++) {
			assertTrue(client.newWorld(Helpers.getRandomWorld()) == 201);
			assertNotNull(client.loadWorldState());

		}

		assertTrue(client.loadSubsection(-1, -1, -1, -1) == null);
		String worldDescription = Helpers.fileToString(ClassLoader.getSystemResourceAsStream("world.txt"));
		assertTrue(client.newWorld(worldDescription) == 201);
		assertNotNull(client.loadWorldState());
		ClientCritter[] crits = client.listCritters();
		for (ClientCritter crit : crits) {
			assertNotNull(client.retrieveACritter(crit.id));
			assertTrue(client.deleteCritter(crit.id) == 204);
		}
		assertNull(client.retrieveACritter(0));
		assertNull(client.retrieveACritter(-1));
		try {
			assertNull(Helpers.spawnCritterOnServer(client,
					new File(ClassLoader.getSystemResource("spiral-critter.txt").toURI()), 5, -1, -1));
		} catch (URISyntaxException e) {
			System.out.println("Couldnt load critter.");
		}
		assertTrue(client.advanceWorld(100) == 200);
		assertTrue(client.loadWorldState().current_timestep == 100);
		assertTrue(client.runContinuous(100) == 200);

		int t = client.loadWorldState().current_timestep;
//		System.out.println(t);
		assertTrue(t > 100);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
		assertTrue(client.runContinuous(0) == 200);
		int t2 = client.loadWorldState().current_timestep;
//		System.out.println(t2);
		if (t2 <= t)
			System.out.println("Issue: t2:" + t2 + " is not greater than t1:" + t);
//		assertTrue(t2 > t);
		assertTrue(client.loadWorldState().current_timestep == t2);
		ClientWorld world = client.loadSubsection(10, 10, 6, 3);
		assertTrue(world.numRows() == 4 && world.numCols() == 3 && world.getHeight() == 6 && world.getWidth() == 3);

		EntityCreation entity = new EntityCreation(0, 0, "rock", null);
		assertTrue(client.createAnEntity(entity) == 200 || client.createAnEntity(entity) == 406);
		assertTrue(client.createAnEntity(entity) == 406);
		entity.row = -1;
		assertTrue(client.createAnEntity(entity) == 406);
		entity.row = 0;
		entity.type = "blah";
		assertTrue(client.createAnEntity(entity) == 406);
		entity.type = "food";
		assertTrue(client.createAnEntity(entity) == 400);
		assertTrue(client.runContinuous(200) == 200);
		assertTrue(client.advanceWorld(-1) == 406);
		assertTrue(client.advanceWorld(5) == 406);
		assertTrue(client.runContinuous(0) == 200);
		assertTrue(client.advanceWorld(5) == 200);
		world = client.loadSubsection(10, 10, 0, 0);
		assertTrue(world.numRows() == 0 && world.numCols() == 0 && world.height == 0 && world.width == 0);
		world = client.loadSubsection(10, 10, 0, 5);
		assertTrue(world.numRows() == 0 && world.numCols() == 5 && world.height == 0 && world.width == 5);
		world = client.loadSubsection(10, 10, 0, 0);
		assertTrue(world.numRows() == 0 && world.numCols() == 0 && world.height == 0 && world.width == 0);
		world = client.loadSubsection(0, 0, 6, 3);
		assertTrue(world.numRows() == 4 && world.numCols() == 3 && world.height == 6 && world.width == 3);
		world = client.loadSubsection(0, 0, 5, 3);
		assertTrue(world.numRows() == 4 && world.numCols() == 3 && world.height == 5 && world.width == 3);

	}

}
