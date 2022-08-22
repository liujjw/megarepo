package model;

import java.io.PrintStream;

import controller.ControllerImpl;

/**
 * Represents the Server model in the simulation. Has appropriate fields and
 * functionality for our server to use
 */
public class ServerModel extends ControllerImpl {
	/**
	 * Constructs a ServerModel object
	 */
	public ServerModel() {
		super();
	}

	/**
	 * Constructs a ServerModel object with the given printstream
	 * 
	 * @param p printstream to print error messages to
	 */
	public ServerModel(PrintStream p) {
		super(p);
	}

	/**
	 * Removes the specified critter
	 * 
	 * @param critter to be removed from world
	 * @return whether or not it was successfully removed
	 */
	public boolean removeCritter(Critter critter) {
		return world.removeCritter(critter.getLocation());
	}
}
