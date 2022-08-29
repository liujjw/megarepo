import java.awt.event.*;

import acm.graphics.*;
import acm.program.*;

public class drawingLines extends GraphicsProgram{
	public void init(){
		addMouseListeners();
	}
	
	public void mousePressed(MouseEvent e){
		startX = e.getX();
		startY = e.getY();
		myLine = new GLine(startX, startY, startX, startY);
		add(myLine);
		
	}
	
	public void mouseDragged(MouseEvent e){
		myLine.setEndPoint(e.getX(), e.getY());
	}
	
	private double startX;
	private double startY;
	GLine myLine;
}
