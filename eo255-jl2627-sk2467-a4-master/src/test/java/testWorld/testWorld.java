package testWorld;

import static model.Constants.HEIGHT;
import static model.Constants.WIDTH;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStreamReader;

import controller.Controller;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ast.Program;
import controller.ControllerImpl;
import parse.ParserImpl;

/** A class to test the World (and therefore all other models) */
public class testWorld extends WorldImpl {

	World world;
	Critter a, b, d;

	@Test
	void testGridGeneration() {
		HexGrid h = new HexGrid(6, 10);
		assertFalse(h.inBounds(new Vector(2, 6)));
		assertFalse(h.inBounds(new Vector(4, 8)));
		assertFalse(h.inBounds(new Vector(6, 8)));
		assertFalse(h.inBounds(new Vector(5, 2)));
		assertTrue(h.inBounds(new Vector(2, 3)));
		assertTrue(h.inBounds(new Vector(1, 4)));
		assertTrue(h.inBounds(new Vector(3, 6)));
		assertTrue(h.inBounds(new Vector(5, 5)));

		HexGrid h2 = new HexGrid(WIDTH, HEIGHT);
		assertFalse(h.inBounds(new Vector(50, 6)));
		assertFalse(h.inBounds(new Vector(49, 2)));
	}

	@Test
	void testGetTerrainInfo() {
		World w = new WorldImpl("foo", 6, 10);
		assertTrue(w.getTerrainInfo(2, 6) == -1);
		assertTrue(w.getTerrainInfo(4, 8) == -1);
		assertTrue(w.getTerrainInfo(6, 8) == -1);
		assertTrue(w.getTerrainInfo(5, 2) == -1);

		assertTrue(world.getTerrainInfo(1, 0) == -1);
		assertTrue(world.getTerrainInfo(4, 13) == -1);
		assertTrue(world.getTerrainInfo(-4, -13) == -1);
	}

	@Test
	void actionCostEnergy() throws IOException {
		world.passTime();
		assertTrue(world.getNumberOfAliveCritters() == 3);
		assertTrue(a.getMemory(4) == 349);
		assertTrue(b.getMemory(4) == 349);
		assertTrue(d.getMemory(4) == 349);
	}

	@BeforeEach
	void prepExampleWorld() {
		world = new WorldImpl("small world", 10, 15);
		world.setRock(2, 2);
		world.setRock(3, 6);
		world.setRock(9, 10);
		world.setFood(new Vector(4, 4), 500);
		world.setFood(new Vector(1, 3), 1000);
		ControllerImpl c = new ControllerImpl();
		// Critter a = c.getCritter("files/example-critter.txt");
		int[] mem = { 9, 2, 3, 1, 500, 0, 17 };
		ParserImpl p = new ParserImpl();
		Program pra = p
				.parse(new InputStreamReader(ClassLoader.getSystemResourceAsStream("example-critter-onlyrules.txt")));

		a = new CritterImpl("example", mem, pra);
		b = new CritterImpl("example", mem, (Program) pra.clone());
		d = new CritterImpl("example", mem, (Program) pra.clone());
		world.setNewlyCreatedCritter(2, 5, 3, a);
		world.setNewlyCreatedCritter(4, 3, 1, b);
		world.setNewlyCreatedCritter(4, 4, 2, d);
	}

	@Test
	// enable breakpoints in critter actions to inspect
	void examineWorldStateTransition() {
		int i = 0;
		while (i++ < 100) {
			world.passTime();
		}
	}

	@Disabled
    @Test
    void testController() {
        ControllerImpl ci = new ControllerImpl();
        ci.loadWorld("src/test/resources/world.txt");
        world = ci.world;
        world.passTime();
        ci.printWorld(System.out);
        ReadOnlyCritter c = world.getReadOnlyCritter(4,3);
        int[] a = c.getMemory();
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }

    private static final String pre = "C:\\Users\\jackj\\IdeaProjects\\eo255-jl2627-sk2467-a4\\src\\test\\resources\\";
    @Test
	void testMate() {
		Controller c = new ControllerImpl();
		c.loadWorld(pre + "world.txt");
		c.loadOneCritter_t(pre + "mate-critter.txt", 6,6, 1);
		c.loadOneCritter_t(pre + "mate-critter.txt", 7,7,4);
		c.advanceTime(1);
		assertTrue(c.getReadOnlyWorld().getNumberOfAliveCritters() == 3);
	}



}
