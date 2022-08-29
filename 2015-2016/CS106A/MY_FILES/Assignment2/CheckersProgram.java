import acm.program.*;
import acm.graphics.*;

public class CheckersProgram extends GraphicsProgram{
	public void run(){
		Checkers checkerObject = new Checkers();
		GObject[][] array = checkerObject.initCheckerboard();

		for(int i = 0; i < array.length; i++){
			for(int k = 0; k < array[i].length; k++){
				add(array[i][k], k * array[i][k].getWidth() , i * array[i][k].getHeight());
			}
		}
		
	}
		
}
