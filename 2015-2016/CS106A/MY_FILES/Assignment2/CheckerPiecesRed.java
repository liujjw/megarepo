import acm.graphics.*;
import java.awt.*;

public class CheckerPiecesRed extends GCompound{
	
	private static final int DIMENSIONS = 50;
	
	public CheckerPiecesRed(){
		GRect square = new GRect(DIMENSIONS, DIMENSIONS);
		square.setFillColor(Color.LIGHT_GRAY);
		square.setFilled(true);
		int x = 0;
		int y = 0;
		add(square, x, y);
		
		GOval checkerPiece = new GOval(DIMENSIONS * 0.6, DIMENSIONS * 0.6);
		checkerPiece.setFillColor(Color.RED);
		checkerPiece.setFilled(true);
		add(checkerPiece, (DIMENSIONS - (DIMENSIONS * 0.6)) / 2, (DIMENSIONS - (DIMENSIONS * 0.6)) / 2);
	}
}
