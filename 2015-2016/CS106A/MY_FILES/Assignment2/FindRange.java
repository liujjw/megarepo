/*
 * File: FindRange.java
 * Name: 
 * Section Leader: 
 * --------------------
 * This file is the starter file for the FindRange problem.
 */

import acm.program.*;

public class FindRange extends ConsoleProgram {
	
	private static final int SENTINEL = 0;
	public void run() {
		println("This program finds the smallest and largest of a set of values, input 0 to calculate.");
		int largest, smallest;
		largest = smallest = readInt("?");
		if(largest == SENTINEL){
			println("No values entered");
		}else{
			while(true){
				int value = readInt("?");
				if(value == SENTINEL) break;
				if(value > largest) largest = value;
				if(value < smallest) smallest = value;
			}
			println("Smallest: " + smallest);
			println("Largest: " + largest);
		}

	}
}

