import acm.program.*;

public class raiseToRealPower extends ConsoleProgram{
	private static final double PI = 3.1415926;
	public void run(){
		double n = readDouble("real number n: ");
		int k = readInt("integer k power: ");
		
		double power = 1.0;
		
		if(k < 0){
			for(int i = 0; i < -(k); i++){
				power *= n;
			}
			power = (1 / power);
		}else{
			for(int i = 0; i < k; i++){
				power *= n;
			}
		}
		/* Implement your method so that it can correctly calculate the result when k is negative, using the relationship
			x^(-k) = 1/ x^(k) */
		println(power);
		println("");
		

		int k1 = 4;
		
		for(int i = -4; i <= k1; i++){		
			double power1 = 1;
			if(i < 0){
				for(int j = 0; j < -(i); j++){
					power1 *= PI;
				}
				power1 = 1 / power1;
			}else{
				for(int l = 0; l < i; l++){
					power1 *= PI;
				}
			}
			println("Pi to the " + i + "th power is: " + power1);
		}
		
	}
}
