import stanford.karel.*;

public class betterKarelBorder extends SuperKarel{
	public void run(){
		moveToPosition();
		for(int i = 0; i < 4; i++){
			doASide();
		}
	}
	
	private void moveToPosition(){
		move();
		turnLeft();
		move();
		turnRight();
	}
	
	private void doASide(){
		while(frontIsClear()){
			if(!beepersPresent()){
				putBeeper();
			}
			move();
		}
		
		turnAround();
		move();
		turnRight();
		move();
	}
}
