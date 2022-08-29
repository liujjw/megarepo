import acm.program.*;

public class guessNumber extends ConsoleProgram{
	private static final int RANGE = 100;
	
	public void run(){
		println("Think of a number between 1 and 100 and " +
				"I'll guess it!");
		boolean isWin = false;
		
		int upper = RANGE;
		int lower = 1;
		
		while(lower <= upper){
			int mid = (upper + lower) / 2;
			String s1 = readLine("Is it " + mid + "?");
			
			if(s1.equals("yes")){
				println("I guessed it!");
				isWin = true;
				break;
			}else if(s1.equals("no")){
				String s2 = readLine("Is it less than " + mid + "? ");
				if(s2.equals("yes")){
					upper = mid - 1;
				}else if(s2.equals("no")){
					lower = mid + 1;
				}
			}else{
				s1 = readLine("I'm sorry. WHAT? ");
			}
		}
		
		if(!isWin){
			println("Sorry. I would have guessed it if you actually had a number in mind...");
		}
	
	}
}
