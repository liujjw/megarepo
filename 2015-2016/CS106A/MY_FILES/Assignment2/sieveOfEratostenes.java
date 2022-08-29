/* A program that generates a list of prime numbers from 2 to 1000 using the Sieve of Eratosthenes algorithm. */
import acm.program.*;
import java.util.*;

public class sieveOfEratostenes extends ConsoleProgram{
	private static final int N = 100000; /* Cannot be less than or equal to 1, 1 does not count as prime or composite.*/
	
	public void run(){
		
		ArrayList<Integer> arrayOfPossibility = new ArrayList<Integer>();
		for(int i = 2; i <= N; i++){
			arrayOfPossibility.add(i);
		}
		
		int prime = arrayOfPossibility.get(0); /* Assuming arraylist auto un-boxes integer objects into integer primitives*/
		
		
		while(true){
			
			for(int i = (arrayOfPossibility.indexOf(prime) + 1); i < arrayOfPossibility.size(); i++){
				if((arrayOfPossibility.get(i) % prime) == 0){
					arrayOfPossibility.remove(i);
				}
			}
			
			try{
				prime = arrayOfPossibility.get((arrayOfPossibility.indexOf(prime)) + 1); 
			}catch(Exception e){
				break;
			}
			
			/* An optimzation would be to start crossing out at the square of the number 
			 * skip numbers, we can apply sieve until the sqrt of N*/
		}
		
		println(arrayOfPossibility.get(arrayOfPossibility.size() - 1));

	}
}

/*
 * Design
 * Development
 * Debugging
 * Deployment
 * 
 * 
 * 
 * Algorithmic design
 * 1. list all values from 2 to n 
 * 2. select 2 
 * 3. remove or disregard all multiples of the number selected 
 * 3. then select the next consecutive number not crossed off and repeat step 3
 * 
 * 1. create an array of N - 1 values to account for not counting the number 1, which is not prime or composite
 * 2. initialize variables at each index by 2 + index
 * 3. arraylist: remove any multiples of two not including two itself
 * 4. now the array is smaller and we can do this for th next consecutive value which will not be a multiple of two 
 * can we use the same primitve array but then intialized to a new array with the multiples removed since efficiency is valued?
 * first lets get array list working
 * */
