import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class testPolygons extends GraphicsProgram{
    private static final double SIDE = 36.0;
	public void run(){
	      GPolygon diamond = new GPolygon();
	      diamond.addVertex(-22, 0);
	      diamond.addVertex(0, 36);
	      diamond.addVertex(22, 0);
	      diamond.addVertex(0, -36);
	      diamond.setFillColor(Color.RED);
	      diamond.setColor(Color.BLUE);
	      diamond.setFilled(true);
	      diamond.scale(5);
	      diamond.rotate(120);
	      add(diamond, 100, 100);
	      
	          GPolygon hex = new GPolygon();
	          hex.setLocation(100, 100);
	          hex.sendToFront();
	          hex.addVertex(-SIDE, 0);
	          int angle = 60;
	          for (int i = 0; i < 6; i++) {
	             hex.addPolarEdge(SIDE, angle);
	 angle -= 60; }
	      

	      
	      
	}
}
