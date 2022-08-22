package model;

import ast.Program;

/** A factory class for retrieving various models */
public class ModelFactory {
	/**
	 * Returns a critter created with given parameters
	 * 
	 * @param species species of critter
	 * @param memory  memory array of critter
	 * @param program program of critter
	 * @return newly constructed critter
	 */
	public static Critter getCritter(String species, int[] memory, Program program) {
		return new CritterImpl(species, memory, program);
	}

	/**
	 * Returns a world with default name and size
	 * 
	 * @return newly constructed world object
	 */
	public static World getDefaultWorld() {
		return new WorldImpl();

	}

	/**
	 * returns a world with given name, width, and height.
	 * 
	 * @param name   name of world
	 * @param width  width of world
	 * @param height height of world
	 * @return newly constructed world
	 */
	public static World getWorld(String name, int width, int height) {
		return new WorldImpl(name, width, height);
	}

}
