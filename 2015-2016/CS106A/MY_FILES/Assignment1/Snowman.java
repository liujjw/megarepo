import acm.graphics.*;
import acm.program.*;

public class Snowman extends GraphicsProgram {
	public void run(){
		add(new GOval(100,100), 100, 0);
		add(new GOval(190,190), 56, 101);
		add(new GOval(300,300),0, 292);
		
		add(new GOval(20,20), 120, 20);
		add(new GOval(20,20), 160, 20);
		
		add(new GLabel("V"), 146, 50);

	}
}
