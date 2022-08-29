/*
 * File: CheckerboardKarel.java
 * ----------------------------
 * When you finish writing it, the CheckerboardKarel class should draw
 * a checkerboard using beepers, as described in Assignment 1.  You
 * should make sure that your program works for all of the sample
 * worlds supplied in the starter folder.
 */

import stanford.karel.*;

public class CheckerboardKarel extends SuperKarel {

	// top level
	public void run(){
		// check if front is clear, then continue placing beepers
		if(frontIsClear()){
			while(frontIsClear()){
				doOneStreet();
				moveUp();
			}
		}else{
			// only one column
			oneColumn();
		}
	}
	
	// checkerboard one street
	private void doOneStreet(){
		// on beeper first always true
		putBeeper();
		// while front clear
		while(frontIsClear()){
			// move once
			move();
			// if front is still clear, move again and place a second beeper, eg if we put down a beeper
			// then no more space in front, while loop ends, or put down a beeper and only one space is left,
			// ie no space for next checkerboard beeper, just move forwrd to end and not execute whats in the if body
			if(frontIsClear()){
				move();
				putBeeper();
			}
		}
	}
	
	// moves up
	// accommodates odd number of avenues on world:
	// well hey, we need just to have every other corner from next street, relative to the current, checkerbaorded,
	// so we'll move back from end of this street one corner, move up, and then well start from this point in row
	private void moveUp(){
		// a beeper is here on last corner
		if(beepersPresent()){
			
			if(facingEast()){
				// turn around and move back, if front is clear, move up from this column
				turnAround();
				move();
				turnRight();
				if(frontIsClear()){
					move();
					turnLeft();
				}
			// ie facing west
			}else{
				// different move sequence, if we move as directed above, then we'll have the same patterns
				turnLeft();
				if(frontIsClear()){
					move();
					turnRight();
				}
			}
		
		// no beeper on last corner
		}else{
			
			// add another move sequence, ie if there is not a beeper present and just empty space
			if(facingEast()){
				turnLeft();
				if(frontIsClear()){
					move();
					turnLeft();
				}
			}else{
				turnRight();
				if(frontIsClear()){
					move();
					turnRight();
				}
			}
		
		}
	}
	
	// algorithm for doing 1 column
	private void shimmy(){
		while(frontIsClear()){
			move();
			if(frontIsClear()){
				move();
				putBeeper();
			}


		}
	}
	
	private void oneColumn(){
		// if front is not clear, eg only 1 avenue, turn up and check to do only a column if there is one
		turnLeft();
		if(frontIsClear()){
			// starting beeper
			putBeeper();
			// shimmy
			shimmy();
		}else{
			// put a beeper on spot regardless, ie for 1x1
			putBeeper();
		}
	}
	

}
