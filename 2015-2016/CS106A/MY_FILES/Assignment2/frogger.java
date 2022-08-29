import acm.graphics.*;
import acm.program.*;
import java.awt.event.*;

public class frogger extends GraphicsProgram{
	
	public void run(){
		froggy = new GImage("frog.png");
		froggy.scale(0.08);
		double x = SQSIZE * (NCOLS / 2);
		double y = SQSIZE * (NROWS - 1);
		add(froggy, x, y);
		
		addMouseListeners();
	}
	
	public void mousePressed(MouseEvent e){
		// determine if either x or y is greater or less than the x or y position of the frog
		double xDelta = e.getX() - (froggy.getX() + (froggy.getWidth() / 2));
		double yDelta = e.getY() - (froggy.getY() + (froggy.getHeight() / 2));
		if(Math.abs(xDelta) > Math.abs(yDelta)){
			if(xDelta < 0){
				if(froggy.getX() - SQSIZE > 0){
					froggy.move(-SQSIZE, 0);
				}
			}else{
				if(!(froggy.getX() + froggy.getWidth() + SQSIZE > getWidth())){
					froggy.move(SQSIZE, 0);
				}
			}
		}else{
			if(yDelta < 0){
				if(!(froggy.getY() - SQSIZE < 0)){
					froggy.move(0, -SQSIZE);
				}
			}else{
				if(!(froggy.getY() + froggy.getHeight() + SQSIZE > getHeight())){
					froggy.move(0, SQSIZE);
				}
			}
		}
	}
	
    public static final int SQSIZE = 75;
    public static final int NCOLS = 7;
    public static final int NROWS = 3;
    GImage froggy;
}
