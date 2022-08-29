import acm.graphics.*;
import acm.program.*;

public class LineGraph extends GraphicsProgram{
	private static final double POINT_DIAM = 3;
	private static final double OFFSET = 5;
	private static final double X_SCALE = 100;
	private static final double Y_SCALE = 100;
	
	public void run(){
		double[] xCoords = {
			0.0,
			0.4,
			0.8,
			1.2,
			1.6,
			2.0,
			2.4,
			2.8,
			3.2,
			3.6
		};
		
		double[] yCoords = {
				0.67,
				0.68,
				0.71,
				0.86,
				0.86,
				1.04,
				1.30,
				1.81,
				1.46,
				1.86
		};
		
		drawLineGraph(xCoords, yCoords);
	}
	
	public void drawLineGraph(double[] xCoords, double[] yCoords){
		for(int i = 0; i < xCoords.length; i++){ /*x and y must have same number of values*/
			
			double x = (xCoords[i] * X_SCALE) + OFFSET;
			double y = getHeight() - (yCoords[i] * Y_SCALE);
			GOval point = new GOval(POINT_DIAM, POINT_DIAM);
			point.setFilled(true);
			add(point, x, y);
			
			double lineX1 = x + (POINT_DIAM / 2);
			double lineY1 = y + (POINT_DIAM / 2);
			double lineX2 = 0;
			double lineY2 = 0;
			try{
				lineX2 = (xCoords[i + 1] * X_SCALE) + OFFSET + (POINT_DIAM / 2);
			}catch(Exception e){
				lineX2 = (xCoords[i] * X_SCALE) + OFFSET + (POINT_DIAM / 2);
			}
			try{
				lineY2 = getHeight() - (yCoords[i + 1] * Y_SCALE) + (POINT_DIAM / 2);
			}catch(Exception e){
				lineY2 = getHeight() - (yCoords[i] * Y_SCALE) + (POINT_DIAM / 2);
			}
			
			add(new GLine(lineX1, lineY1, lineX2, lineY2));
		}
	}
	
}

/*
 * Line graph is made of filled govals and glines connecting them
 * 1. make govals
 * 2. connect with glines
 * */
