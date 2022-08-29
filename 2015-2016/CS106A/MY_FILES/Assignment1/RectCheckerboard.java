/*Checkerboards window with squares 
 * automatically adjusts for size.*/

import acm.program.*;
import acm.graphics.*;

public class RectCheckerboard extends GraphicsProgram {
	
	private static final int ROWS = 8;
	private static final int COLUMNS = 8;
	
	public void run(){

		int square_side = getHeight() / ROWS; // height of window in pixels divided by number of rows to get size of each square, each one of 8 rows is divided among x pixels
		for(int i = 0; i < ROWS; i++){
			for(int j = 0; j < COLUMNS; j++){
				int x_cord = j * square_side;
				int y_cord = i * square_side;
				
				GRect square = new GRect(square_side, square_side, x_cord, y_cord);
				square.setFilled((i + j) % 2 != 0);
				add(square);
			}
		}
		
	}
}
