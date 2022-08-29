import acm.program.*;

public class nOddIntegers extends ConsoleProgram{
	public void run(){
		int numberOfOddInts = readInt("How many odd integers: ");
		int odds = 1;
		int sum = 0;
		for(int i = 0; i < numberOfOddInts; i++){
			sum += odds;
			odds += 2;
		}
		println(sum);
	}


}
