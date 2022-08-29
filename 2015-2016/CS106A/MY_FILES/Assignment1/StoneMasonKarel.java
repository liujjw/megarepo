/*
 * File: StoneMasonKarel.java
 * --------------------------
 * Karel should solve the "repair the quad"
 * problem from Assignment 1. Karel repairs the
 * columns for any damaged quad.
 */

import stanford.karel.*;

public class StoneMasonKarel extends SuperKarel {
	
	// repair a column, move 4 steps, repeat until wall is reached
	public void run(){
		while(frontIsClear()){
			oneRepair();
			nextColumn();
		}
		// OBOB
		oneRepair();
	}
	
	// preconditions for starting a repair
	private void oneRepair(){
		turnLeft();
		
		// and we haven't reached ceiling
		while(frontIsClear()){
			repairAColumn();
			move();
		}
		// OBOB 
		repairAColumn();
		
		// post, back to starting positions
		turnAround();
		while(frontIsClear()){
			move();
		}
		turnLeft();
		
	}
	
	// scales the avenue to repair
	private void repairAColumn(){
		if(noBeepersPresent()){
			putBeeper();
		}
	}
	
	// // beams are always 3 empty spaces apart
	private void nextColumn(){
		move();
		move();
		move();
		move();
	}
	


}
