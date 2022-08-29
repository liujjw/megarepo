/*
 * File: Pyramid.java
 * Name: crimson_black
 * Section Leader: MEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
 * ------------------
 * This file is the starter file for the Pyramid problem.
 * It includes definitions of the constants that match the
 * sample run in the assignment, but you should make sure
 * that changing these values causes the generated display
 * to change accordingly.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Pyramid extends GraphicsProgram {

/** Width of each brick in pixels */
	private static final int BRICK_WIDTH = 30;

/** Width of each brick in pixels */
	private static final int BRICK_HEIGHT = 12;

/** Number of bricks in the base of the pyramid */
	private static final int BRICKS_IN_BASE = 45;
	
	public void run(){
		for(int i = 0; i < BRICKS_IN_BASE; i++){
			buildARow(i);
		}
	}
	
	private void buildARow(int row){
		int numberOfBricks = BRICKS_IN_BASE - row;
		for(int i = 0; i < numberOfBricks; i++){
			GRect aBrick = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
			int y = getHeight() - (row * BRICK_HEIGHT);
			int x = ((getWidth() - (numberOfBricks * BRICK_WIDTH)) / 2) + (i * BRICK_WIDTH);
			add(aBrick, x, y);
		}
	}
}

