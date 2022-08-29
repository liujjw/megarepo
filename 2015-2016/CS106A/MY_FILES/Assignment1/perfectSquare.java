import acm.program.*;

public class perfectSquare extends ConsoleProgram{
	public void run(){
		int number = readInt("This program checks if a number is a perfect square: ");
		if(isPerfectSquare(number)){
			println("Yes");
		}else{
			println("No");
		}
	}
	
	private boolean isPerfectSquare(int number){
		double square_root = Math.sqrt(number);
		if(square_root - (int) square_root == 0){
			return true;
		}else{
			return false;
		}
	}
}
