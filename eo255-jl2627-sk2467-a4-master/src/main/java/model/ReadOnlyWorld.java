package model;

/**
 * Read-only state interface that allows the course staff to query your critter
 * world.
 *
 * <p>
 * NEVER remove or change any methods in this file except reformatting. Feel
 * free to add additional methods in this file. It might be helpful in later
 * assignments.
 */
public interface ReadOnlyWorld {

	/** @return number of steps */
	int getSteps();

	/** @return number of alive critters. */
	int getNumberOfAliveCritters();

	/**
	 * @param c column id.
	 * @param r row id.
	 * @return the critter at the specified hex, {@code null} if there is no critter
	 *         there. Out of bound hexes should also return null.
	 */
	ReadOnlyCritter getReadOnlyCritter(int c, int r);

	/**
	 * @param c column id.
	 * @param r row id.
	 * @return 0 is empty, -1 is rock, -X is (X-1) food. Treat out-of-bound hex as
	 *         rock.
	 * @throws IllegalArgumentException if the hex is occupied by a critter.
	 */
	int getTerrainInfo(int c, int r);

	/****************************************
	 * We added the methods below this line *
	 ****************************************/

	/**
	 * Returns the direction of the critter at location (c,r)
	 * 
	 * @param c column id.
	 * @param r row id.
	 * @return direction of critter (int in range 0,5) or -1 if no critter
	 */
	int getCritterDirection(int c, int r);

	/**
	 * Gets the number of rows in the world's hex representation.
	 * 
	 * @return number of rows in the world
	 */
	int numRows();

	/**
	 * Gets the number of columns in the world's hex representation.
	 * 
	 * @return number of columns in the world
	 */
	int numCols();

	/**
	 * Gets the height of the world's hex representation
	 * 
	 * @return height of board
	 */
	int getHeight();

	/**
	 * Gets the width of the world's hex representation
	 * 
	 * @return width of board
	 */
	int getWidth();

	/**
	 * Whether a given coordinate is in bounds of the world.
	 * */
	boolean inBounds(WorldImpl.Vector v);
}
