package model;

import ast.Program;
import ast.Rule;
import model.WorldImpl.Vector;

/** An interface holding and documenting all possible Critter methods */
public interface Critter extends ReadOnlyCritter {
	/**
	 * Gets the program
	 * 
	 * @return value is the program data structure
	 */
	Program getProgram();

	/**
	 * Gets the critter's location on the world map
	 * 
	 * @return critter's location
	 */
	Vector getLocation();

	/**
	 * @param lastExecutedRule set the rule that was executed last
	 */
	void setLastExecutedRule(Rule lastExecutedRule);

	/**
	 * Set the value of the critter's memory at the given index to the given value
	 * 
	 * @param index index of memory to be set
	 * @param val   value of given memory index to be set
	 */
	void setMemFromRule(int index, int val);

	/**
	 * Sets critter pass. Can only be used by interpreter
	 * 
	 * @param val to set pass to
	 */
	void setPass(int val);

	/**
	 * Gets the value of ahead[index] corresponding to the current critter and world
	 * state.
	 * 
	 * @param index direction to be indexed
	 * @return value computed by expression ahead[index]
	 */
	int getAhead(int index);

	/**
	 * Gets the value of nearby[index] corresponding to the current critter and
	 * world state.
	 * 
	 * @param index direction to be indexed
	 * @return value computed by expression nearby[index]
	 */
	int getNearby(int index);

	/**
	 * Let's the Critter know about the world it is in. Guaranteed to be set when
	 * interpreter runs.
	 *
	 * @param w Critter's world when it is placed in a CritterTile.
	 */
	void setWorld(World w);

	/**
	 * @return whether this wants to get their groove on
	 */
	boolean isLusty();

	/**
	 * Use to keep track of how much energy was subtracted, before things like size
	 * changes, which would affect future energy costs but not when the size was not
	 * changed yet.
	 *
	 * @return energy removed in a currently unsuccessful mate
	 */
	int subtractedEnergy();

	/**
	 * Get the appearance of this relative to an orientation of another critter.
	 *
	 * @param observer the orientation of an observing critter
	 * @return the calculated appearance of the critter
	 */
	int getAppearance(int observer);

	/**
	 * Let's the Critter know about its Critter Tile context. Guaranteed to be set
	 * when interpreter runs.
	 */
	void setCritterTile(WorldImpl.CritterTile crtTile);

	/**
	 * @param by critter energy amount to change; + integer for increase, - integer
	 *           for decrease
	 * @return successful or not
	 */
	boolean changeEnergy(int by);

	/**
	 * @return if critter has made a turn yet in a time step (reset every time step)
	 */
	boolean hadTurn();

	/**
	 * @param b set whether a critter has had a turn in a given time step
	 */
	void setHadTurn(boolean b);

	/**********************
	 * For each of the following methods, some or all of the specifications
	 * described in the project specs have been inserted to best describe
	 * functionality.
	 *********************/

	/**
	 * Instructs the critter to move forwards using energy. If invalid move, critter
	 * will still use energy but the move will fail.
	 */
	void moveForward();

	/**
	 * Instructs the critter to move backwards using energy. If invalid move,
	 * critter will still use energy but the move will fail.
	 */
	void moveBackwards();

	/**
	 * Instructs the critter to wait in its location and absorb solar energy. Could
	 * not be called wait() as that is an Object method.
	 */
	void waiting();

	/**
	 * Instructs the critter to eat some food at the hex in front of it, gaining the
	 * same amount of energy as the food it consumes. When there is more food
	 * available on the hex than the critter can absorb, the remaining food is left
	 * on the hex.
	 */
	void eat();

	/**
	 * Rotates the critter 60 degrees to the left, consuming energy.
	 */
	void turnLeft();

	/**
	 * Rotates the critter 60 degrees to the left, consuming energy.
	 */
	void turnRight();

	/**
	 * Instructs the critter to serve, converting some of its own energy into food
	 * added to the hex in front of it, if that hex is either empty or already
	 * contains some food.
	 */
	void serve(int exprVal);

	/**
	 * Instructs the critter to attack, removing an amount of energy from the
	 * attacked critter that is determined by the size and offensive ability of the
	 * attacker and the defensive ability of the victim.
	 */
	void attack();

	/**
	 * Instructs the critter to grow by one unit, consuming energy.
	 */
	void grow();

	/**
	 * Instructs a critter to bud, using a large amount of its energy to produce a
	 * new, smaller critter behind it with the same genome (possibly with some
	 * random mutations).
	 */
	void bud();

	/**
	 * Instructs the critter to attempt to mate with another critter in front of it.
	 * For this to be successful, the critter in front must also be facing toward it
	 * and attempting to mate in the same time step. If mating is successful, both
	 * critters use energy to create a new critter of size 1 whose genome is the
	 * result of merging the genomes of its parents. Unsuccessful mating uses little
	 * energy.
	 */
	void mate();

	/**
	 * Instructs critter to locate the closest food in its hex grid environment,
	 * returning a numerical score representing how far it is.
	 *
	 * @return score of the closest food, 1000000 if none
	 */
	int smell();

	/** Get complexity of critter. */
	int getComplexity();

	/**
	 * For testing: returns the value of the last smell done by critter.
	 * 
	 * @return integer value of last smell
	 */
	int getLastSmell();
}
