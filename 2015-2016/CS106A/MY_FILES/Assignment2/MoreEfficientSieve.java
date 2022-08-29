/* A program that generates a list of prime numbers from 2 to N using the Sieve of Eratosthenes algorithm. */
import acm.program.*;
import java.util.*;

public class MoreEfficientSieve extends ConsoleProgram{
	private static final int N = 100000; /* Cannot be less than or equal to 1, 1 does not count as prime or composite.*/
	
	public void run(){
		
		ArrayList<Integer> arrayOfPossibility = new ArrayList<Integer>();
		for(int i = 2; i <= N; i++){
			arrayOfPossibility.add(i);
		}
		
		int prime = arrayOfPossibility.get(0);/* first prime is always 2*/
		
		
		while(prime <= Math.sqrt(N)){ /* Only need to check primes up to the sqrt of N*/
			
			for(int i = (arrayOfPossibility.indexOf(prime * prime)) /* Start checking from the square of the prime*/
					; i < arrayOfPossibility.size(); i++){
				if((arrayOfPossibility.get(i) % prime) == 0){
					arrayOfPossibility.remove(i);
				}
			}
			
			prime = arrayOfPossibility.get((arrayOfPossibility.indexOf(prime)) + 1); /* Non-prime multiples of previous primes should be gone, so next consecutive should be prime*/
		}
		
		println(arrayOfPossibility.get(arrayOfPossibility.size() - 1));

	}
}
