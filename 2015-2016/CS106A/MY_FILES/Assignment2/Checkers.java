import acm.graphics.*;
import acm.program.*;

public class Checkers{
	
	public Checkers(){
		
	}
	
	public GObject[][] initCheckerboard(){
		GObject[][] initBoard = new GObject[8][8];
		for(int i = 0; i < initBoard.length; i++){
			for(int k = 0; k < initBoard[i].length; k++){
				initBoard[i][k] = new CheckerPiecesRed();
			}
		}
		return initBoard;
	}

}
