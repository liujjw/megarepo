/*
 * File: Hailstone.java
 * Name: 
 * Section Leader: 
 * --------------------
 * This file is the starter file for the Hailstone problem.
 */

import acm.program.*;

public class hailstoneSequence extends ConsoleProgram{
	
	public void run(){
		
		/*Pick some positive integer n*/
		int someN = readInt("Pick a positive integer: ");		
		
		/*if even, divide by 2, if odd, multiply by 3 and add 1, do until  is 1*/
		while(true){
			if(someN % 2 == 0){
				int temp = someN;
				someN /= 2;
				println(temp + " is even, so I take half: " + someN);
				if(someN == 1) break;
			}else{
				int temp = someN;
				someN = (someN * 3) + 1;
				println(temp + " is odd, so I make 3n + 1: " + someN);
				if(someN == 1) break;
			}
		}
	
	}	
	
}

