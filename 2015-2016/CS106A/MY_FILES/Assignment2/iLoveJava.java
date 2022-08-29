import acm.graphics.*;
import java.awt.*;

import acm.program.*;
import acm.util.*;

public class iLoveJava extends GraphicsProgram{
	
	public void run(){
		waitForClick();
		love = new GImage("overly_attached_computer.jpg", 0, 50);
		love.scale(0.35);
		this.setBackground(Color.RED);
		
		// love.setFont("Serif-38");
		add(love, getWidth() / 2, getHeight() / 2);
		while(true){
			boundaryChange();
			love.move(DELTA_X, DELTA_Y);
			love.setVisible(false);
			pause(15);
			love.setVisible(true);
			pause(SLEEP);
		}
	}
	
	private void boundaryChange(){
		if(love.getX() >= getWidth()){
			DELTA_X = -DELTA_X;
			double rand = rgen.nextDouble(0.7, 1.4);
			if(DELTA_Y < 0){
				DELTA_Y = -8.0;
			}else{
				DELTA_Y = 8.0;
			}
			DELTA_Y *= rand;
		}
		if(love.getX() <= 0){
			DELTA_X = -DELTA_X;
			double rand = rgen.nextDouble(0.9, 1.5);
			if(DELTA_Y < 0){
				DELTA_Y = -8.0;
			}else{
				DELTA_Y = 8.0;
			}
			DELTA_Y *= rand;
		}
		if(love.getY() >= getHeight()){
			DELTA_Y = -DELTA_Y;
			double rand = rgen.nextDouble(0.7, 1.35);
			if(DELTA_X < 0){
				DELTA_X = -6.5;
			}else{
				DELTA_X = 6.5;
			}
			DELTA_X *= rand;
		}
		if(love.getY() <= 0){
			DELTA_Y = -DELTA_Y;
			double rand = rgen.nextDouble(0.6, 1.35);
			if(DELTA_X < 0){
				DELTA_X = -6.5;
			}else{
				DELTA_X = 6.5;
			}
			DELTA_X *= rand;
		}
		
	}
	
	double DELTA_X = 6.5;
	double DELTA_Y = 6.0;
	GImage love;
	int SLEEP = 10;
	
    private RandomGenerator rgen = new RandomGenerator();
}
