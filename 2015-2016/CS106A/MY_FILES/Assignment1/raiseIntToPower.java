/*Write a method raiseIntToPower that takes 
 * two integers, n and k, and returns nk. Use your method to display a table 
 * of values of 2k for all values of k from 0 to 10.*/

import acm.program.*;

public class raiseIntToPower extends ConsoleProgram{
	
	public void run(){
		
		int n = readInt("n: ");
		int k = readInt("k: ");
		
		int power = 1;
		
		for(int i = 0; i < k; i++){
			power *= n;
		}
		
		println("n to the kth power is: " + power);
		println("");
		
		int n1 = 2;
		int k1 = 10;
		
		int power1 = 1;
		
		for(int i = 0; i < k1; i++){
			power1 *= n1;
			println("2 to the " + i + "th power is: " + power1);
		}
	}
}
