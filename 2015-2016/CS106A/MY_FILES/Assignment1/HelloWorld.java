import acm.program.*;
import acm.graphics.*;

public class HelloWorld extends GraphicsProgram {
	public void run(){
		add(new GLabel("I love Java."), 100, 75);
		add(new GLabel("Jackie Liu"), 1000, 700);
	}
}
