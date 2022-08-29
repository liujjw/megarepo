import acm.program.*;

public class sixOrSeven extends ConsoleProgram{
	public void run(){
		for(int i = 1; i <= 100; i++){
			if((i % 6 == 0) && (i % 7 == 0)){
				println(i);
			}
		}
	}
}
