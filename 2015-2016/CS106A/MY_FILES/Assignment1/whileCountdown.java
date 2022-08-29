import acm.program.*;
public class whileCountdown extends ConsoleProgram{
	public void run(){
		int i = 10;
		while(i > 0){
			println(i);
			i--;
		}
	}
}
