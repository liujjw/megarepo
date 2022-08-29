import acm.program.*;
import acm.graphics.*;
import java.awt.event.*;

public class towers extends GraphicsProgram{
	public void run(){
		signalTowers halifax = new signalTowers(150, 50, "Halifax", null);
		signalTowers rohan = new signalTowers(200, 30, "Rohan", halifax);
		signalTowers minasTirith = new signalTowers(500, 200, "Minas Tirith", rohan);
		
		add(halifax, (getWidth() - halifax.getWidth()) / 2, (getHeight() - halifax.getHeight()) / 2);
		add(rohan, 500, 500);
		
		addMouseListeners();
	}
	
	public void mouseClicked(MouseEvent e){
		GCompound tower = getElementAt(e.getX(), e.getY());
		if(tower != null){
			tower.lightThisTower();
		}
	}
}
