import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.*;

public class probabilityBoard extends GraphicsProgram{
	
	private static final int N_GROOVES = 10;
	private static final double PADDING = 10;
	private static final double DIAMETER = PADDING * 0.6;
	private static final int SIMULATIONS = 100;
	
	public void run(){
		setup();
		
		for(int i = 0; i < SIMULATIONS; i++){
			
			int position = simulateDrop();
			
			addMarbleToGroove(position);
			
			addMarblesToScreen(grooves);
		}

	}
	
	/* Draws pegboard grooves. */
	private void setup(){
		for(int i = 0; i <= N_GROOVES; i++){
			double x1 = ((getWidth() - (N_GROOVES * PADDING)) / 2) + (i * PADDING);
			double y1 = getHeight();
			double x2 = x1;
			double y2 = 0.05 * getHeight();
			
			GLine border = new GLine(x1, y1, x2, y2);
			add(border);
		}
	}
	
	private GOval makeMarble(){
		GOval marble = new GOval(DIAMETER, DIAMETER);
		marble.setFillColor(Color.CYAN);
		marble.setColor(Color.CYAN);
		marble.setFilled(true);
		return marble;
	}
	
	private void addMarblesToScreen(GOval[][] array){
		for(int j = 0; j < array.length; j++){
			for(int i = 0; i < array[j].length; i++){
				if(array[j][i] != null){
					double x = ((getWidth() - (N_GROOVES * PADDING)) / 2) + (j * PADDING) + ((PADDING - DIAMETER) / 2);
					double y = (getHeight() - array[j][i].getHeight()) - ((array[j][i].getHeight() * i)) ;
					add(array[j][i], x, y);
				}
			}
		}
	}
	
	private void addMarbleToGroove(int position){
		/* Find an empty space for marble*/
		int emptyIndex = 0;
		for(int i = 0; i < grooves[position].length; i++){
			if(grooves[position][i] == null){
				emptyIndex = i;
				break;
			}
		}
		grooves[position][emptyIndex] = makeMarble();
	}
	
	
	/* Simulates a marble drop through the pegs, which can be enumerated as 1 or 0 if it went left or right.
	 * The total of the 1s and 0s will give us its position. */
	private int simulateDrop(){
		int total = 0;
		for(int i = 0; i < N_GROOVES - 1; i++){
			total += rgen.nextInt(2);
		}
		
		return total;
	}
	
	
	/* Private ivars */
	private RandomGenerator rgen = new RandomGenerator();
	
	GOval[][] grooves = new GOval[N_GROOVES][SIMULATIONS];
	
}
