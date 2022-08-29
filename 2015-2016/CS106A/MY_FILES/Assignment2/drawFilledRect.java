/*
Program to draw rectangles on the canvas
* File: DrawRectangle.java
 * ------------------------
 * This program allows users to create rectangles on the canvas
 * by clicking and dragging with the mouse.
 */

import java.awt.event.*;
import acm.graphics.*;
import acm.program.*;
import acm.util.*;

/** This class allows users to drag rectangles on the canvas */
public class drawFilledRect extends GraphicsProgram {

	/** Runs the program */
   public void run() {
      addMouseListeners();
   }

   /** Called on mouse press to record the starting coordinates */
   public void mousePressed(MouseEvent e) {
      existingRect = getElementAt(e.getX(), e.getY());
      if(existingRect == null){
    	  startX = e.getX();
          startY = e.getY();
          currentRect = new GRect(startX, startY, 0, 0);
          currentRect.setColor(rgen.nextColor());
          currentRect.setFilled(true);
          add(currentRect); 
      }else{
    	  lastX = e.getX();
    	  lastY = e.getY();
      }
   }

   /** Called on mouse drag to reshape the current rectangle */
   public void mouseDragged(MouseEvent e) {
	  if(existingRect != null){
		  existingRect.sendToFront();
		  
		  existingRect.move(e.getX() - lastX, e.getY() - lastY);
		  
		  // we want delta x and y in terms of last location of mouse, not values of objects since only the top left corners can be used to determine location
		  lastX = e.getX();
		  lastY = e.getY();
		  // lastX and lastY must capture the last x and y positions, ie getX and y must be more recent to have a difference
	  }else{
		  double x = Math.min(e.getX(), startX);
	      double y = Math.min(e.getY(), startY);
	      double width = Math.abs(e.getX() - startX);
	      double height = Math.abs(e.getY() - startY);
	      currentRect.setBounds(x, y, width, height);
	  }

}

   /* Private state */
   private GRect currentRect;
   private GObject existingRect;
   private double startX;
   private double startY;
   private double lastX;
   private double lastY;
/* The current rectangle         */
/* The initial mouse X position  */
/* The initial mouse Y position  */
   
   
   private RandomGenerator rgen = new RandomGenerator();
}