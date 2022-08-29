/*Different from Karel example..?*/
/*only theoretical move classes, idk where to actaully implement*/

public class rightHandAlgorithm {
	
	private void righthandRule(){
		while(true){ // goes on forever
			while(frontIsClear()){ // move forward and turn right while thr front of us is still clear
				moveForward();
				turnRight();
			}
			turnLeft(); // means not clear anymore, so turn back opposite right, undo turn by turning left, then move and turn right to try again
		}
	}

}
