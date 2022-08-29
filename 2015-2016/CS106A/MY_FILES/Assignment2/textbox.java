import acm.graphics.*;

public class textbox extends GCompound{
	public textbox(String s){
		GRect rect = new GRect(BOX_WIDTH, BOX_HEIGHT);
		double x = 0;
		double y = 0;
		add(rect, x, y);
		
		GLabel label = new GLabel(s);
		double x1 = (BOX_WIDTH - label.getWidth()) / 2; 
		double y1 = (BOX_HEIGHT - label.getAscent()) / 2;
		add(label, x1, y1);
	}
	
	private static final double BOX_WIDTH = 120;
    private static final double BOX_HEIGHT = 50;
}
