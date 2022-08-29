/*ATTENTION!!!!!
 * PASCALS TRIANGLE IS DEFINED AS NUMBERS ABOVE SUM TO NUMBER BELOW, NOT NECESSARILY IN COUNTING PATTERN, BUT AS AN INSTNACE MAY WORK
 * */


import acm.graphics.*;
import acm.program.*;

public class pascalTriangle extends GraphicsProgram{
	private static final int ROWS = 8;
	private static final int HEIGHT_OF_ROWS = 35;
	private static final int WIDTH_OF_ROWS = 15;
	
	public void run(){
		int counterBeginningValue = 0;
		for(int i = 0; i < ROWS; i++){ // makes n rows of pascal's
			int counterRow = 0; // a counter, starts at 0 for every row
			for(int j = 0; j < i + 1; j++){ // j determines that we'll add a number i + 1 times for each row, ie the nth row we at, we have n + 1 values
				addNumber(i, j, counterRow, counterBeginningValue);
				counterRow++; // add counter to each value in a row, ie each subsequent value in row is + 1 greater than preceding
			}
			counterBeginningValue++; // each new row starting a value is + 1 greater than preceding 
		}
	}
	
	private void addNumber(int i, int j, int counterRow, int counterBeginningValue){
		int value = ((j - 1) + counterRow + counterBeginningValue);
		GLabel label = new GLabel("" + value);
		
		int x = calcLocationX(i, j);
		int y = calcLocationY(i);
		label.setLocation(x, y);
		
		add(label);
		
	}
	
	/* 
	 * each value will be at number of values minus total x value
	 * total x value will be number of items times width
	 * must be centered, so will be at getwidth of entire box + number of items times x width
	 * center start + ith item in total number of values in row
	 * */
	private int calcLocationX(int i, int j){ 
		int centerStart = (getWidth() - (WIDTH_OF_ROWS * (i + 1))) / 2;
		return (centerStart + (j * WIDTH_OF_ROWS));
	}
	
	private int calcLocationY(int i){ // height of each row, where each individual value in row will have same		                
		return ((i + 1) * HEIGHT_OF_ROWS); // height as row, as height of row product of row number
	}
}	


/*		int count = i; // start counting from i
		
		for(int a = 0; a < j; a++){ // count up j times
			
			GLabel label = new GLabel("" + count); // first set pre-increment count to glabel
			label.setLocation(j + a * 100, i * 50);
			add(label);
			
			count++; // count up by 1
		}
		
*/