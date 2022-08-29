import acm.graphics.*;
import acm.program.*;

public class CheckboardCircles extends GraphicsProgram{
	private static final int ROWS = 8;
	private static final int COLUMNS = 8;
	private static final int WIDTH = 50;
	private static final int LENGTH = 50;
	private static final int OVAL_LENGTH = 40;
	private static final int OVAL_WIDTH = 40;
	
	public void run(){
		 for(int i = 0; i < ROWS; i++){
			 for(int j = 0; j < COLUMNS; j++){
				 int x = WIDTH * j;
				 int y = LENGTH * i;
				 add(new GRect(x, y, WIDTH, LENGTH));
				 
				 if((i + j) % 2 != 0){
					 int Ovalx = ((x + WIDTH) - OVAL_WIDTH) - 5;
					 int Ovaly = ((y + LENGTH) - OVAL_LENGTH) - 5;
					 add(new GOval(Ovalx, Ovaly, OVAL_WIDTH, OVAL_LENGTH));
				 }
			 }
		 }
	 }

}
