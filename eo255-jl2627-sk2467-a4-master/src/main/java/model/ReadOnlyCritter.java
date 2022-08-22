package model;

/**
 * Read-only critter interface that allows the course staff to query your
 * critter.
 *
 * <p>
 * NEVER remove or change any methods in this file except reformatting. Feel
 * free to add additional methods in this file. It might be helpful in later
 * assignments.
 */
public interface ReadOnlyCritter {

	/**
	 * Gets the critter's ID as created by the server
	 * 
	 * @return ID
	 */
	int getID();

	/** @return critter species. */
	String getSpecies();

	/**
	 * Gets the critter's current orientation.
	 * 
	 * @return direction corresponding to the critter's orientation, between 0 and 5
	 *         inclusive
	 */
	int getDirection();

	/**
	 * Gets the memory value at a certain index
	 * 
	 * @param index index of memory to be returned
	 * @return value of memory at given index
	 */
	int getMemory(int index);

	/**
	 * Hint: you should consider making an defensive copy of the array.
	 *
	 * @return an array representation of critter's memory.
	 */
	int[] getMemory();

	/** @return current program string of the critter. */
	String getProgramString();

	/**
	 * @return last rule executed by the critter, {@code null} if it has not
	 *         executed any.
	 */
	String getLastRuleString();

	/**
	 * @return index of last rule executed in critter's program. TODO: UNTESTED
	 */
	int getLastRuleIndex();

	/**
	 * For testing: return value of last smell.
	 *
	 * @return integer value of last smell
	 */
	int getLastSmell();
}
