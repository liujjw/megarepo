import acm.graphics.*;
import acm.program.*;

public class peaceCHP8 extends GraphicsProgram{
	
	
	public void run(){
		double x = getWidth() / 2;
		double y = getHeight() / 2;
		double radius = 250;
		addPeaceSymbol(x, y, radius);
	}
	
	private void addPeaceSymbol(double x, double y, double radius){
		GLine disarmament = new GLine(x, (y - radius), x, y + radius);
		add(disarmament);
		
		GOval circle = new GOval((x - radius), (y - radius), radius * 2, radius * 2);
		add(circle);
		
		GLine nuclear1 = new GLine(x, y + (0.1 * radius), (x - (0.67 * radius)), (y + (0.75 * radius)));
		add(nuclear1);
		
		GLine nuclear2 = new GLine(x, y + (0.1 * radius), (x + (0.67 * radius)), (y + (0.75 * radius)));
		add(nuclear2);
	}
}