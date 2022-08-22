package controller;

import java.io.PrintStream;
import java.util.List;

import model.ReadOnlyWorld;

/**
 * Controller interface that allows the course staff to test your critter world
 * implementation.
 *
 * <p>
 * NEVER remove or change any methods in this file except reformatting. Feel
 * free to add additional methods in this file. It might be helpful in later
 * assignments.
 *
 * <p>
 * You have to provide one implementation of {@code ConsoleController} to {@code
 * ControllerFactory}.
 */
public interface Controller {
	/** @return the readonly world. */
	ReadOnlyWorld getReadOnlyWorld();

	/**
	 * Sets the PrintStream of the controller to p
	 * 
	 * @param p PrintStream to print errors to, if any
	 */
	void setPrintStream(PrintStream p);

	/** Starts new random world simulation. */
	void newWorld();

	/**
	 * Spawns a singular critter stored in specified file to the given location
	 * 
	 * @param filename name of critter file
	 * @param row      row to be spawned
	 * @param col      col to be spawned
	 * @return whether critter is successfully loaded
	 */
	boolean loadOneCritter(String filename, int row, int col);

	/**
	 * Starts new simulation with world specified in filename.
	 *
	 * @param filename name of the world file.
	 * @return whether the world is successfully loaded.
	 */
	boolean loadWorld(String filename);

	/**
	 * Starts new simulation with world specified in string parameter.
	 * 
	 * @param s string of world file
	 * @return whether the world is successfully loaded
	 */
	boolean loadWorldFromString(String s);

	/**
	 * Loads one critter into the world at given location from a string program
	 * 
	 * @param species   species string
	 * @param mem       mem array of critter
	 * @param program   program string
	 * @param row       row of critter placement
	 * @param col       col of critter placement
	 * @param direction direction of critter to spawn. If -1, chooses random
	 *                  direction
	 * @return critter id or -1 if unable to create critter
	 */
	public int loadOneCritterFromString(String species, int[] mem, String program, int row, int col, int direction);

	/**
	 * Loads {@code num} critters from string program with given memory array and
	 * species name
	 * 
	 * @param species species name
	 * @param mem     mem array
	 * @param program program string
	 * @param num     number of critters to spawn
	 * @return a list of all created critter IDs, or null if unable to spawn
	 *         critters
	 */
	public List<Integer> loadCrittersFromString(String species, int[] mem, String program, int num);

	/**
	 * Loads critter definition from filename and randomly places n critters with
	 * that definition into the world.
	 *
	 * @param filename name of the critter spec file.
	 * @param n        number of critter to add.
	 * @return whether all critters are successfully loaded.
	 */
	boolean loadCritters(String filename, int n);

	/**
	 * Advances the world by n time steps.
	 *
	 * @param n number of steps.
	 * @return false if the world has not been initialized or n is negative, true
	 *         otherwise.
	 */
	boolean advanceTime(int n);

	/**
	 * Print the world to the specified stream.
	 *
	 * @param out the stream to print the world.
	 */
	void printWorld(PrintStream out);

	/*
	 * FOR TESTING, NOT PART OF CLIENT INTERFACE.
	 */

	/**
	 * For testing from a resources folder.
	 * 
	 * @param dir direction to load critter with
	 */
	boolean loadOneCritter_t(String filename, int row, int col, int dir);
//	/**
//	 *  For testing from a resources folder.
//	 *  */
//	boolean loadWorld_t(String filename);
}
