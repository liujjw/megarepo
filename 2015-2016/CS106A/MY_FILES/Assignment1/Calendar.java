import acm.program.*;
import acm.graphics.*;

public class Calendar extends GraphicsProgram{
	
	/* The number of days in the month */
    private static final int DAYS_IN_MONTH = 28;
    /* The day of the week on which the month starts */
    /* (Sunday = 0, Monday = 1, Tuesday = 2, and so on) */
    private static final int DAY_MONTH_STARTS = 1;
    /* The width in pixels of a day on the calendar */
    private static final int DAY_WIDTH = 64;
    /* The height in pixels of a day on the calendar */
    private static final int DAY_HEIGHT = 64;
    
    
	public void run(){
		makeGrid();
		populateDates();
	}
	
	private void makeGrid(){
		int rows = DAYS_IN_MONTH / 7; // how many rows
		rows = (DAYS_IN_MONTH % 7 != 0)? rows + 1: rows + 0; // adds 1 row if more than 28 days per month
		if(DAY_MONTH_STARTS >= 5){ // if day month starts + days in month exceeds rows, add another
			rows++;
		}

		for(int i = 0; i < rows; i++){
			for(int j = 0; j < 7; j++){
				int x = j * DAY_WIDTH;
				int y = i * DAY_HEIGHT;
				GRect cell = new GRect(x, y, DAY_WIDTH, DAY_HEIGHT);
				add(cell);
			}
		}
		
		
	}
	
	private void populateDates(){
		int rows = DAYS_IN_MONTH / 7; // how many rows
		rows = (DAYS_IN_MONTH % 7 != 0)? rows + 1: rows + 0; // adds 1 row if more than 28 days per month
		if(DAY_MONTH_STARTS >= 5){ // if day month starts + days in month exceeds rows, add another
			rows++;
		}
		
		int date = 0;
		
		for(int i = 0; i < rows; i++){ // x rows in calendar
			for(int j = 0; j < 7; j++){
				int x = (j * DAY_WIDTH) + 7;
				int y = (i * DAY_HEIGHT) + 20;
				date++;
				int offset = DAY_MONTH_STARTS * DAY_WIDTH;
				if(i == 0){
					x += offset;
				}
				if(x > DAY_WIDTH * 7){
					date--;
					break;
				}
				if(date <= DAYS_IN_MONTH){
					GLabel Date = new GLabel("" + date, x, y);
					add(Date);
				}
			}
		}
	}
	
}