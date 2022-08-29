import acm.graphics.*;
import acm.program.*;

public class squareGen extends GraphicsProgram{
	private static final int SQROOT = 6;
	private static final int LENGTH = 100;
	private static final int WIDTH = 100;
	private static final int PADDING = 100;
	public void run(){
		/*builds the square for any positive integer number, tells if a perfect square*/
		for(int i = 0; i < SQROOT; i++){
			for(int j = 0; j < SQROOT; j++){
				GRect aSquare = new GRect(LENGTH, WIDTH);
				int x = ((getWidth() - (LENGTH * SQROOT)) / 2) + (j * LENGTH);
				int y = ((getHeight() - (i * WIDTH))) - PADDING;
				add(aSquare, x, y);
			}
		}
	}
}
