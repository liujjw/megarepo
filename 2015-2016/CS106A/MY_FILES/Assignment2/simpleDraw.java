/*This class implements a simple drawing interface for
 * rectangles, ovals, and lines.
 * */

import acm.graphics.*;
import acm.program.*;
import java.awt.event.*;
import acm.util.*;

public class simpleDraw extends GraphicsProgram{
	private static final int FILLED_RECT = 1;
	
	/* Mouse listeners to determine what the user has selected to draw.*/
	public void run(){
		addMouseListeners();
		
		/* Separate GCompounds to encapsulate the selectors.*/
		initInterface();
		
	}
	
	/* Select appropriate method on canvas.*/
	public void mouseClicked(MouseEvent e){
		/* If mouse is within the filled rect button, draw filled rects. */
		if(filledRect.contains(e.getX(), e.getY())){
			currentState = FILLED_RECT;
		}
	}
	
	/* For drawing shapes, determines what to draw depending on currentState */
   public void mousePressed(MouseEvent e) {
	   
	   /* Draws filled ractangles */
	   if(currentState == FILLED_RECT){
		   // existing rectangle will be element at mouse click
		   existingRect = getElementAt(e.getX(), e.getY());
		   
		   // generic pointer to the object selected, ie retrieved from get element at
		   thisObject = existingRect;
		   
		   // if there is no object there
		   if(existingRect == null){
			   // startx and starty for locations of new rectangle, no size yet, very important for being the relative point to which 
			   // rectangle resizes
			   startX = e.getX();
			   startY = e.getY();
			   currentRect = new GRect(startX, startY, 0, 0);
			   // colors and fill
	           currentRect.setColor(rgen.nextColor());
	           currentRect.setFilled(true);
	           add(currentRect);
		   }else{
			   // if an object is there, then get the locations of mouse
			   lastX = e.getX();
			   lastY = e.getY();
		   }
		   
	   }	   
   }
   public void mouseDragged(MouseEvent e){
	   /* Only for filled rect selected*/
	   if(currentState == FILLED_RECT){
		   // if the generic object pointer does point to something, send object to front and allow dragging
		   if(thisObject != null){
			   thisObject.sendToFront();
			   thisObject.move(e.getX() - lastX, e.getY() - lastY);
			   lastX = e.getX();
			   lastY = e.getY();
		   }else{
			   // resizing the rectangle
			   // changing upper left coordinate for location based on whether the new mouse coordinate or the old coordinate is less 
			   // less means top left in x and y axis
			   double x = Math.min(e.getX(), startX);
			   double y = Math.min(e.getY(), startY);
			   // changing width and length which is just difference between new mouse coords and old rect coords
			   double length = Math.abs(e.getX() - startX);
			   double width = Math.abs(e.getY() - startY);
			   currentRect.setBounds(x, y, length, width);
			   
		   }
	   }
   }
	
	/* Adds the "buttons" to the screen */
	private void initInterface(){
		/* Filled rectangle selector*/
		filledRect = new filledRect(45, 30);
		add(filledRect, 5, 100);
	}
		
	/* Private instance variables */
	GCompound filledRect;
	
	int currentState;
	
	GObject existingRect;
	GRect currentRect;
	double startX;
	double startY;
	double lastX;
	double lastY;
	
	GObject thisObject;
	
	RandomGenerator rgen = new RandomGenerator();
	
	/*
	 * Notes:
	 * when we add mouse listeners, they never end when the run method ends, they always listen, so they are like infinite loops, asynchronous ones at that
	 * */
}
