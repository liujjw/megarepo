/*
 * File: CollectNewspaperKarel.java
 * --------------------------------
 * Karel walks to the door of its house, picks up the
 * newspaper (represented by a beeper, of course), and then returns
 * to its initial position in the upper left corner of the house.
 */

import stanford.karel.*;

public class CollectNewspaperKarel extends SuperKarel {
	
	// top level methods, self-explanatory
	public void run() {
		moveToNewspaper();
		pickUp();
		returnHome();
	}
	
	// moves to the newspaper
	private void moveToNewspaper(){
		turnRight();
		move();
		turnLeft();
		move();
		move();
		move();
	}
	
	// picks up newspaper beeper
	private void pickUp(){
		pickBeeper();
	}
	
	// returns home
	private void returnHome(){
		turnAround();
		move();
		move();
		move();
		turnRight();
		move();
		turnRight();
	}
	

}
