import acm.graphics.*;
import acm.program.*;

public class initialsCHP8 extends GraphicsProgram{
	public void run(){
		
		drawJ J = new drawJ();
		drawL L = new drawL();
		
		J.scale(5);
		L.scale(5);
		
		add(J, getWidth() / 2 - J.getWidth(), getHeight() / 2 - (J.getHeight() / 2));
		add(L, getWidth() / 2, getHeight() / 2 - (L.getHeight() / 2));
	}
}
