import java.awt.Color;

import acm.graphics.*;
import acm.program.*;

public class pumpkinCHP8 extends GraphicsProgram{
	private static final double RADIUS = 200;
	private static final double STEM_WIDTH = 22;
	private static final double STEM_HEIGHT = 40;
	
	public void run(){
		GOval circle = new GOval(getWidth() / 2 - RADIUS, getHeight() / 2 - RADIUS, RADIUS * 1.9, RADIUS * 2);
		circle.setFillColor(Color.CYAN);
		circle.setColor(Color.ORANGE);
		circle.setFilled(true);
		add(circle);
		
		GRect stem = new GRect(STEM_WIDTH, STEM_HEIGHT);
		double x = getWidth() / 2 - (STEM_WIDTH / 2) - 10;
		double y = (getHeight() / 2) - RADIUS - STEM_HEIGHT;
		stem.setLocation(x, y);
		stem.setFillColor(Color.BLACK);
		stem.setColor(Color.ORANGE);
		stem.setFilled(true);
		add(stem);
	}
	

}