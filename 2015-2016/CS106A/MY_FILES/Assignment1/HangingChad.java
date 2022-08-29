
import stanford.karel.*;

public class HangingChad extends SuperKarel{
	public void run(){
		while(frontIsClear()){
			move();
			if(noBeepersPresent()){
				cleanChad();
			}
		}
	}
	
	private void cleanChad(){
		turnLeft();
		move();
		while(beepersPresent()){
			pickBeeper();
		}
		turnAround();
		move();
		move();
		while(beepersPresent()){
			pickBeeper();
		}
		turnAround();
		move();
		turnRight();
		
	}
}
