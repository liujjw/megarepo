/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 2;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
	
/** y location of ball as a ratio of paddle y offset and height combined*/
	private static final double RATIO_TO_PADDLE = 5;

/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		/* Wait for user */
		GLabel start = new GLabel("Start?");
		start.setFont("Times New Roman-42");
		add(start, (getWidth() - start.getWidth()) / 2, getHeight() * 0.5);
		waitForClick();
		removeAll();
		pause(1000);
		
		/* Setup */
		setup();
		
		/* Gameplay */
		begin();
	}
	
	private void setup(){
		
		/* Sounds */
		bounceClip = MediaTools.loadAudioClip("bounce.au");
		
		/* A score counter */
		doScores();
		
		/* Sets up the bricks */
		int colorCounter = 0;
		
		for(int i = 0; i < NBRICK_ROWS; i++){
			
			if(colorCounter > 9){
				colorCounter = 0;
			}
			
			for(int k = 0; k < NBRICKS_PER_ROW; k++){
				GRect brick = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
				double x = ((BRICK_WIDTH * k) + (BRICK_SEP * k)) + (BRICK_SEP / 2); 
				double y = (BRICK_HEIGHT * i) + BRICK_Y_OFFSET + (BRICK_SEP * i);
				
			/*The color of the bricks remain constant for two rows and run in the following rainbow-like sequence: RED, ORANGE, YELLOW, GREEN, CYAN.*/
				switch(colorCounter){
				case 0: case 1:
					brick.setFillColor(Color.RED);
					brick.setColor(Color.RED);
					brick.setFilled(true);
					break;
				case 2: case 3:
					brick.setFillColor(Color.ORANGE);
					brick.setColor(Color.ORANGE);
					brick.setFilled(true);
					break;
				case 4: case 5:
					brick.setFillColor(Color.YELLOW);
					brick.setColor(Color.YELLOW);
					brick.setFilled(true);
					break;
				case 6: case 7:
					brick.setFillColor(Color.GREEN);
					brick.setColor(Color.GREEN);
					brick.setFilled(true);
					break;
				case 8: case 9:
					brick.setFillColor(Color.CYAN);
					brick.setColor(Color.CYAN);
					brick.setFilled(true);
					break;
				default:
					brick.setFilled(false);
					break;
				}
				
				add(brick, x, y);
			}
			
			colorCounter++;
		}
		
		/* Sets up the paddle */
		paddle = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		double x = (getWidth() - PADDLE_WIDTH) / 2;
		double y = getHeight() - PADDLE_Y_OFFSET;
		add(paddle, x, y);
		
		addMouseListeners();
		
	}
	
	/* Mouse tracker for paddle in method: setup() */
	public void mouseMoved(MouseEvent e){
		double DELTA_X = e.getX() - (paddle.getX() + (PADDLE_WIDTH / 2));
		if((paddle.getX() + PADDLE_WIDTH + DELTA_X > getWidth()) || (paddle.getX() + DELTA_X < 0)){
			DELTA_X = 0;
		}
		paddle.move(DELTA_X, 0);
		
	}
	
	/* Begins the game after setup*/
	private void begin(){
		
		/* Instantiates a ball*/
		ball = new GOval(BALL_RADIUS * 2, BALL_RADIUS * 2);
		double x = (getWidth() - (BALL_RADIUS * 2)) / 2;
		double y = getHeight() - ((PADDLE_Y_OFFSET + PADDLE_HEIGHT) * RATIO_TO_PADDLE);
		ball.setFilled(true);
		add(ball, x, y);
		
		/* Movement of ball */
		vx = rgen.nextDouble(2.0, 5.0);
		if(rgen.nextBoolean(0.5)) vx = -vx;
		vy = 6.0;
		
		while(true){
			
			ball.move(vx, vy);
			
			if(ball.getX() + (BALL_RADIUS * 2) > getWidth() || ball.getX() < 0) vx = -vx;
			if(ball.getY() < 0) vy = -vy;
			
			/* Game over */
			if(ball.getY() > getHeight()){
				messageLose();
				break;
			}
			
			/* Collisions */
			GObject object = getCollisionObject();
			if(object != null){
				if(object == paddle){
					vy = -vy;
					bounceClip.play();
					counterToIncreaseSpeed++;
					counterToRemoveLabel++;
				}else if (object != nextLevel && object != scoreCounter){
					vy = -vy;
					remove(object);
					brickCounter--;
					bounceClip.play();
					doScores();
				}
			}
			
			/* Win condition */
			if(brickCounter == 0) {
				messageWin();
				break;
			}
			
			/* Remove the label */
			if(counterToRemoveLabel == 1){
				if(nextLevel != null){
					remove(nextLevel);
				}

			}
				
			/* Increase velocities */
			if(counterToIncreaseSpeed >= 5){
				counterToIncreaseSpeed = 0;
				counterToRemoveLabel = 0;

				if(vy < 0){
					vy -= 1;
				}else{
					vy += 1;
				}
				if(vx < 0){
					vx -= 3;
				}else{
					vx += 3;
				}
				nextLevel = new GLabel("Faster!");
				nextLevel.setFont("Times New Roman-40");
				add(nextLevel, (getWidth() - nextLevel.getWidth()) / 2, (getHeight() - nextLevel.getAscent()) / 2);
			}
			
			pause(25);
		}
	}
	
	/* Returns an object if any at an x,y */
	private GObject getCollisionObject(){
		if(getElementAt(ball.getX(), ball.getY()) != null){
			
			return getElementAt(ball.getX(), ball.getY());
		
		}else if(getElementAt(ball.getX() + (2 * BALL_RADIUS), ball.getY()) != null){
			
			return getElementAt(ball.getX() + (2 * BALL_RADIUS), ball.getY());
		
		}else if(getElementAt(ball.getX(), ball.getY() + (2 * BALL_RADIUS)) != null){
			
			return getElementAt(ball.getX(), ball.getY() + (2 * BALL_RADIUS));
			
		}else if(getElementAt(ball.getX() + (2 * BALL_RADIUS), ball.getY() + (2 * BALL_RADIUS)) != null){
			
			return getElementAt(ball.getX() + (2 * BALL_RADIUS), ball.getY() + (2 * BALL_RADIUS));
		
		}else{
			
			return null;
		}
	}
	
	private void doScores(){
		if(scoreCounter != null){
			remove(scoreCounter);
		}
		score++;
		scoreCounter = new GLabel("" + score);
		scoreCounter.setColor(Color.GRAY);
		scoreCounter.setFont("Times New Roman-32");
		add(scoreCounter, (getWidth() - scoreCounter.getWidth()) / 2, getHeight() * 0.7);
	}
	
	private void messageWin(){
		removeAll();
		GLabel win = new GLabel("You win");
		win.setFont("Times New Roman-60");
		add(win, (getWidth() - win.getWidth()) / 2, 0.4 * getHeight());
	}
	private void messageLose(){
		removeAll();
		GLabel lose = new GLabel("You Lose");
		lose.setFont("Times New Roman-60");
		add(lose, (getWidth() - lose.getWidth()) / 2, 0.4 * getHeight());
	}
	
	/* Private instance variables */
	GRect paddle;
	
	GOval ball;
	private double vx, vy;
	
	int brickCounter = NBRICK_ROWS * NBRICKS_PER_ROW;
	
	AudioClip bounceClip;
	
	int counterToIncreaseSpeed;
	
	int counterToRemoveLabel;
	
	GLabel nextLevel;
	
	int score = -1;
	GLabel scoreCounter;
	
	private RandomGenerator rgen = new RandomGenerator();
	

}
