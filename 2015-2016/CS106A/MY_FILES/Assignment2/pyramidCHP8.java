import acm.graphics.*;
import acm.program.*;

public class pyramidCHP8 extends GraphicsProgram{
	
	private static final int BRICK_WIDTH = 30;
	private static final int BRICK_HEIGHT = 12;
	private static final int BRICKS_IN_BASE = 14;
	
	public void run(){
		for(int i = 0; i < BRICKS_IN_BASE; i++){
			for(int j = 0; j < BRICKS_IN_BASE - i; j++){
				GRect brick = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
				int x = ((getWidth() - (BRICK_WIDTH * (BRICKS_IN_BASE - i))) / 2) + (BRICK_WIDTH * j);
				int y = getHeight() - (BRICK_HEIGHT * i);
				add(brick, x, y);
			}
		}
	}
}
