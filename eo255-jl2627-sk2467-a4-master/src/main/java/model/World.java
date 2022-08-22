package model;

import java.util.Map;

import model.WorldImpl.Vector;

/**
 * An interface defining the full extensibility of World classes so as to
 * decouple World's implementation and Controller
 */
public interface World extends ReadOnlyWorld {

	/**
	 * @return name of world
	 */
	String getName();

	/**
	 * Removes a critter at given location, if applicable
	 * 
	 * @param v vector of critter location
	 * @return whether or not there was a critter to be removed. Does not add
	 *         critter to dead critter set
	 */
	boolean removeCritter(Vector v);

	/**
	 * Adds ID to critter entry pair to the critterID map
	 * 
	 * @param ID      ID of critter
	 * @param critter critter
	 */
	public void addCritID(int ID, Critter critter);

	/**
	 * Gets the Critter corresponding to the given ID
	 * 
	 * @param ID ID of critter
	 * @return Critter corresponding to ID or null if not found
	 */
	public Critter getCritterByID(int ID);

	/**
	 * @return map of all ids and their corresponding critters
	 */
	public Map<Integer, Critter> getCritterIDMap();

	/**
	 * Sets the given hex location (c,r) to a rock
	 * 
	 * @param c column
	 * @param r row
	 */
	void setRock(int c, int r);

	/**
	 * Sets the given hex location (c,r) to contain the given critter facing in
	 * direction d.
	 * 
	 * @param c  column
	 * @param r  row
	 * @param d  direction of orientation
	 * @param cr Critter to be placed at location
	 */
	void setNewlyCreatedCritter(int c, int r, int d, Critter cr);

	/**
	 * Increases the amount of food at a given hex location (c,r) by amount amt
	 * 
	 * @param c   column
	 * @param r   row
	 * @param amt amount of food to be added
	 */
	void changeFood(int c, int r, int amt);

	/**
	 * Passes one timestep on the world.
	 */
	void passTime();

	/**
	 * Returns the location of random empty tile in the world board, (-1,-1) if
	 * couldn't find one in a reasonable amount of time.
	 * 
	 * @return Vector containing the row and col of this empty location.
	 */
	Vector getRandomLocation();

	/**
	 * @param coord whether the given coordinate is within the bounds of a world
	 */
	boolean inBounds(WorldImpl.Vector coord);

	/**
	 * Kills a critter at a give tile.
	 * 
	 * @param critterTile
	 */
	void killCritter(WorldImpl.CritterTile critterTile);

	/**
	 * Get a tile from the world.
	 *
	 * @param at coordinate to get tile
	 * @return a tile, rocktile if out of bounds
	 */
	HexTile getTile(WorldImpl.Vector at);

	/**
	 * @param v   index to set food to, must be empty and in bounds
	 * @param amt amount of food to set
	 */
	void setFood(WorldImpl.Vector v, int amt);

	/**
	 * Moves critters around the hex grid. Requires parameters be valid.
	 *
	 * @param crtTile the critter tile to move
	 * @param from    move a critter from here
	 * @param dest    to there
	 */
	void moveCritter(WorldImpl.CritterTile crtTile, WorldImpl.Vector from, WorldImpl.Vector dest);
}
