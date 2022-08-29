import java.awt.Color;

import acm.graphics.*;
import acm.program.*;

public class heartCHP8 extends GraphicsProgram{
	private static final double SIZE = 150;
	private static final double PADDING = 18;
	private static final double SEMI_CIRCLE_DIAMETER = 195;
	private static final double LENGTH = 275;
	
	public void run(){
		drawPolygon();
		drawLeftArc();
		drawRightArc();
	}
	
	private void drawPolygon(){
		GPolygon square = new GPolygon();
		square.addVertex(0, -(LENGTH / 2));
		square.addVertex(LENGTH / 2, 0);
		square.addVertex(0, LENGTH / 2);
		square.addVertex(-(LENGTH / 2), 0);
		square.rotate(1);
		square.setFillColor(Color.RED);
		square.setFilled(true);
		add(square, getWidth() / 2, getHeight() / 2 + 16);
	}
	
	private void drawLeftArc(){
		double xRect = (getWidth() / 2) - SIZE - PADDING + 2;
		double yRect = getHeight() / 2 - SIZE;
		
		double width = SEMI_CIRCLE_DIAMETER;
		double height = SEMI_CIRCLE_DIAMETER;
		
		double start = 45;
		double sweep = 180;
		
		
		add(new GArc(xRect, yRect, width, height, start, sweep));
	}
	
	private void drawRightArc(){
		double xRect = (getWidth() / 2) - PADDING - 13;
		double yRect = getHeight() / 2 - SIZE;
		
		double width = SEMI_CIRCLE_DIAMETER;
		double height = SEMI_CIRCLE_DIAMETER;
		
		double start = -45;
		double sweep = 180;
		
		add(new GArc(xRect, yRect, width, height, start, sweep));
	}
}
