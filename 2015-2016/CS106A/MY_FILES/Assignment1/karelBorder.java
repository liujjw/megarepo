import stanford.karel.*;

public class karelBorder extends SuperKarel{
	
	public void run(){
		firstBeeper();
		
		for(int i = 0; i < 4; i++){
			oneSideOfBorder();	
		}
	
	}
	
	private void oneSideOfBorder(){
		if(facingEast()){
			while(frontIsClear()){
				move();
				putBeeper();
			}
			turnAround();
			pickBeeper();
			move();
			turnRight();
		}else if(facingWest()){
			while(frontIsClear()){
				move();
				putBeeper();
			}
			turnAround();
			pickBeeper();
			move();
			turnRight();
		}else if(facingNorth()){
			while(frontIsClear()){
				move();
				putBeeper();
			}
			turnAround();
			pickBeeper();
			move();
			turnRight();
		}else if(facingSouth()){
			while(frontIsClear()){
				move();
				while(!beepersPresent()){
					putBeeper();
				}
			}
			turnAround();
			pickBeeper();
			move();
			turnRight();
		}
	}
	
	private void firstBeeper(){
		move();
		turnLeft();
		move();
		turnRight();
		putBeeper();
	}
}

/**
 * Karel faces east on 1,1
 * karel moves forward once and up once and puts a beeper, this happens in every sized world > 3
 * while the front is clear, place a beeper and move
 * if the front is not clear anymore, collect the beeper and move back
 * turn left and put beepers using the same pattern
 * do until loops back around, then while there are not beepers present, add beepers
 * */
