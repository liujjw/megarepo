import acm.graphics.*;
import acm.program.*;

public class flipImage extends GraphicsProgram{
	public void run(){
		GImage froggy = new GImage("frog.png");
		froggy = flipAnImage(froggy);
		froggy.scale(0.1);
		add(froggy, 300, 300);
		
	}
	
	private GImage flipAnImage(GImage image){
		int[][] array = image.getPixelArray();
		int width = array[0].length;
		int height = array.length;	
		
		for(int row = 0; row < height; row++){
			for(int pixel1 = 0; pixel1 < width / 2; pixel1++){
				int pixel2 = width - pixel1 - 1;
				int temp = array[row][pixel1];
				array[row][pixel1] = array[row][pixel2];
				array[row][pixel2] = temp;
			}
		}
		
		return new GImage(array);
	}
}
