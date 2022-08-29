import acm.program.*;

public class prime extends ConsoleProgram{
	
	public void run(){
		int n = readInt("Is this number prime: ");
		
		if(isPrime(n)){
			println("Yes.");
		}else{
			println("Nope.");
		}
	}
	
	private boolean isPrime(int n){
		
		/*for(int i = 2; i < n; i++){ // brute force, goes through all numbers not 1 and itself and checks of n % i will divide evenly
			if(n % i == 0){ // which would mean that n is not prime
				return false;
			}
		}*/
		
		if(n % 2 != 0){ // if n is not divisble by two, means we can skip even numbers, but if true, means we already have non-prime
			for(int i = 3; i < n; i += 2){ // try all odd numbers
				if(n % i == 0){
					return false; // if one works, then means false not prime
				}
			}
			// if all odd numbers and also even don't work, then number must be true its a prime
			return true;
		}else{
			return false; // if number is divisble by 2, then false not prime
		}

	}
}

/* 
	improvements to brute force:
	
	if not divisible by 2, then not by 4, 6, 8 and so on
		likewise for 3s, 5s, 7s, multiples'
	but we'll need to find and eliminate these numbers in the pool of possible divisors
		ie arrays? we dont have acess to those yet
	or maybe something like a switch
	!! we could increment differently!, ie by 2s,3s and so on to skip
	we might skip threes tho, so increment plus 1 then by 2s to if 2, then plus 1, to skips 4, 6, 8
	
	Euclid style by breaking down problem into smaller
	
*/	