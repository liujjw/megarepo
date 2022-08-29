
/* Class: signalTowers */

/**
 * File: signalTowers.java
 * -----------------------
 * This class creates an instance of a "signal tower", which can propogate a message to other instances
 * that are linked through a copied reference variable. This class extends GCompound, so all add methods stick objects on the Gcompound.
 */

import acm.graphics.*;
import java.awt.*;
import java.awt.event.*;

public class signalTowers extends GCompound{
	
	/*Constructor: signlaTowers(length, width)*/
	/**
	 * Creates a signalTower made of 3 GRects in a pyramid-esque formation, and it may contain a reference to another signal tower through
	 * its variable, wherein one instance may invoke and change another instance.
	 * 
	 * @param length The length of the bottom rectangle.
	 * @param width The width of the bottom rectangle.
	 * @param name The name of the current tower instance.
	 * @param link The reference variable of another instance.
	 * */
	public signalTowers(double length, double width, String name, signalTowers link){
		GRect base = new GRect(length, width);
		GRect uppOne = new GRect(0.75 * length, width);
		uppTwo = new GRect(0.5 * length, width);
		
		add(base, 0, width * 3);
		add(uppOne, 0.25 * length / 2,width * 2);
		add(uppTwo, 0.5 * length / 2, width * 1);
		
		GLabel displayName = new GLabel(name);
		add(displayName, (length - displayName.getWidth()) / 2, (width - displayName.getAscent()) / 2);
		
		myName = name;
		otherInstance = link;
		
	}
	
	/*Method: lightThisTower()*/
	/**
	 * "Lights" the tower pointed to by changing the tip to red. 
	 */
	public void lightThisTower(){
		uppTwo.setFillColor(Color.RED);
		uppTwo.setFilled(true);
		pause(1500);
		uppTwo.setFilled(false);
		pause(1500);
		otherInstance.lightThisTower();
	}
	
	
	
	/**
	 * Private instance variables holding name and link.
	 */
	String myName;
	signalTowers otherInstance;
	GRect uppTwo;
}
