import acm.graphics.*;

public class filledRect extends GCompound{
	/* Draws a filed GRect inside another GRECT */
	public filledRect(double width, double height){
		GRect largerRect = new GRect(width, height);
		add(largerRect);
		
		GRect smallerRect = new GRect(0.6 * width, 0.4 * height);
		smallerRect.setFilled(true);
		add(smallerRect, (largerRect.getWidth() - smallerRect.getWidth()) / 2, (largerRect.getHeight() - smallerRect.getHeight()) / 2);
	}
}
