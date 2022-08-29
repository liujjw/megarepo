import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.awt.event.*;
import java.awt.*;

public class brainGamesWithColors extends GraphicsProgram{
	public void run(){
		addMouseListeners();
		
		RED = new GLabel("RED");
		RED.setColor(rgen.nextColor());
		add(RED, rgen.nextDouble(0, getWidth()), rgen.nextDouble(0, getHeight()));
		
		ORANGE = new GLabel("ORANGE");
		ORANGE.setColor(rgen.nextColor());
		add(ORANGE, rgen.nextDouble(0, getWidth()), rgen.nextDouble(0, getHeight()));
		
		YELLOW = new GLabel("YELLOW");
		YELLOW.setColor(rgen.nextColor());
		add(YELLOW, rgen.nextDouble(0, getWidth()), rgen.nextDouble(0, getHeight()));
		
		CYAN = new GLabel("CYAN");
		CYAN.setColor(rgen.nextColor());
		add(CYAN, rgen.nextDouble(0, getWidth()), rgen.nextDouble(0, getHeight()));
		
		BLACK = new GLabel("BLACK");
		BLACK.setColor(rgen.nextColor());
		add(BLACK, rgen.nextDouble(0, getWidth()), rgen.nextDouble(0, getHeight()));
		
		GREEN = new GLabel("GREEN");
		GREEN.setColor(rgen.nextColor());
		add(GREEN, rgen.nextDouble(0, getWidth()), rgen.nextDouble(0, getHeight()));
		
		MAGENTA = new GLabel("MAGENTA");
		MAGENTA.setColor(rgen.nextColor());
		add(MAGENTA, rgen.nextDouble(0, getWidth()), rgen.nextDouble(0, getHeight()));
		
	}
	
	public void mousePressed(MouseEvent e){
		gobj = getElementAt(e.getX(), e.getY());
		if(gobj != null){
			if(gobj == RED){
				RED.setColor(Color.RED);
			}
			else if(gobj == YELLOW){
				YELLOW.setColor(Color.YELLOW);
			}
			else if(gobj == GREEN){
				GREEN.setColor(Color.GREEN);
			}
			else if(gobj == CYAN){
				CYAN.setColor(Color.CYAN);
			}
			else if(gobj == BLACK){
				BLACK.setColor(Color.BLACK);
			}
			else if(gobj == ORANGE){
				ORANGE.setColor(Color.ORANGE);
			}
			else if(gobj == MAGENTA){
				MAGENTA.setColor(Color.MAGENTA);
			}
		}
	}
	
	public void mouseReleased(MouseEvent e){
		gobj.setColor(rgen.nextColor());
	}
	
	private RandomGenerator rgen = new RandomGenerator();
	GObject gobj;
	GLabel RED;
	GLabel YELLOW;
	GLabel CYAN;
	GLabel BLACK;
	GLabel GREEN;
	GLabel MAGENTA;
	GLabel ORANGE;
	
}
