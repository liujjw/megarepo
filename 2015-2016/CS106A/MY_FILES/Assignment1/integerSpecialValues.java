import acm.program.*;

public class integerSpecialValues extends ConsoleProgram{
	public void run(){
		int largest = 0;
		int secondLargest = 0;
		int i = 0;
		
		while(true){
			i = readInt("?");
			if(i == 0) break;
			while(i < 0){
				i = readInt("Only positive intgers: ");
			}
			if(i > secondLargest){
				if(i > largest){
					largest = i;
				}else{
					secondLargest = i;
				}
			}
		}
		
		println("Largest is " + largest);
		println("Second largest is " + secondLargest);
	}
}
