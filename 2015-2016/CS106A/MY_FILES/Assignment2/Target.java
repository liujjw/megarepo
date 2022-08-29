/*
 * File: Target.java
 * Name: 
 * Section Leader: 
 * -----------------
 * This file is the starter file for the Target problem.
 * 
 * This figure is simply three GOval objects, two red and one white, drawn in the correct order. The outer circle 
 * should have a radius of one inch (72 pixels), the white circle has a radius of 0.65 inches, 
 * and the inner red circle has a radius of 0.3 inches. The figure should be centered in the window of a GraphicsProgram subclass.
 * 
 * PPI of my computer screen must be different, so ratio between inches to pixels will be different.
 * With a resolution of 1440x900 in 13.3 inch diagonally screen, 128 PPI, so one inch on my screen is 128 pixels, so the radius of 
 * the outer circle should be 128 pixels, middle radius should be in ratio of 1:128, so 83, inner radius should be 38.
 * 
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Target extends GraphicsProgram {	
	private static final int OUTER_RADIUS_PX = 128;
	private static final int MIDDLE_RADIUS_PX = 83;
	private static final int INNER_RADIUS_PX = 38;
	
	public void run() {
		/* Draws the three circles for a Target logo. */
		outerCircle();
		middleCirlce();
		innerCircle();
	}
	
	private void outerCircle(){
		GOval oneInchRadius = new GOval(OUTER_RADIUS_PX * 2, OUTER_RADIUS_PX * 2);
		oneInchRadius.setFillColor(Color.RED);
		oneInchRadius.setFilled(true);
		int x = (getWidth() - OUTER_RADIUS_PX * 2) / 2;
		int y = (getHeight() - OUTER_RADIUS_PX * 2) / 2;
		add(oneInchRadius, x, y);
				
	}
	
	private void middleCirlce(){
		GOval middleRadius = new GOval(MIDDLE_RADIUS_PX * 2, MIDDLE_RADIUS_PX * 2);
		middleRadius.setFillColor(Color.WHITE);
		middleRadius.setFilled(true);
		int x = (getWidth() - MIDDLE_RADIUS_PX * 2) / 2;
		int y = (getHeight() - MIDDLE_RADIUS_PX * 2) / 2;
		add(middleRadius, x, y);
	}
	
	private void innerCircle(){
		GOval innerRadius = new GOval(INNER_RADIUS_PX * 2, INNER_RADIUS_PX * 2);
		innerRadius.setFillColor(Color.RED);
		innerRadius.setFilled(true);
		int x = (getWidth() - INNER_RADIUS_PX * 2) / 2;
		int y = (getHeight() - INNER_RADIUS_PX * 2) / 2;
		add(innerRadius, x, y);
	}
}
