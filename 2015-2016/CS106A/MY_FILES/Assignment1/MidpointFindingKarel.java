/*
 * File: MidpointFindingKarel.java
 * -------------------------------
 * When you finish writing it, the MidpointFindingKarel class should
 * leave a beeper on the corner closest to the center of 1st Street
 * (or either of the two central corners if 1st Street has an even
 * number of corners).  Karel can put down additional beepers as it
 * looks for the midpoint, but must pick them up again before it
 * stops.  The world may be of any size, but you are allowed to
 * assume that it is at least as tall as it is wide.
 */

import stanford.karel.*;

public class MidpointFindingKarel extends SuperKarel {

	public void run(){
		// 1 column worlds is just the one corner
		if(frontIsClear()){
			layDownBeepers();
			// ^ ends up at corner before wall
			// algorithm that picks one beeper from both ends of the line of beepers from ^ until leaving only one beeper
			pickBeeperEnds();
			// when ^ ends and leaves a beeper at midpoint 
			moveToMid();
		}else{
			putBeeper();
		}
	}
	
	// lays down beepers until end is reached
	private void layDownBeepers() {
		while(frontIsClear()){
			putBeeper();
			move();
		}
		// OBOB
		putBeeper();
	}
	
	private void pickBeeperEnds(){
		// closes in, so to speak, on a single midpoint by taking away the beepers on the outsides until left with center
		while(beepersPresent()){
			turnAround();
			pickBeeper();
			move();
			while(beepersPresent() && frontIsClear()){
				move();
			}
			// goes back a corner because ^ it checks if a beeper is present, if yes it will move forward, but 
			// wont check if corner moved forward on has a beeper
			if(noBeepersPresent()){
				turnAround();
				move();
				turnAround();
				// checks if only one beeper is left, and therefore not eat it
				midPointCheck();
			}
			// recursion, goes through the method until no beepers are present, need to find way to only have one beeper left
			// there is end condition of no more beepers present
			pickBeeperEnds();
		}
		
	}
	
	private void midPointCheck(){
		// if one beeper left, turns around and checks adjacent corner 
		turnAround();
		move();
		// stay on no beepers present so pickBeeperEnds() will end
		if(noBeepersPresent()){
			turnAround();
		}else{
			// else go back like nothing happened
			turnAround();
			move();
		}
	}
	
	// moves forward one to reach midooint beeper
	private void moveToMid(){
		move();
	}
	
}



