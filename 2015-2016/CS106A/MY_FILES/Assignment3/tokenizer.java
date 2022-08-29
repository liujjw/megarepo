import java.util.*;
import acm.program.*;

public class tokenizer extends ConsoleProgram{
	public void run(){
		String s = readLine("Gimme it: ");
		tokenize(s);
	}
	
	private void tokenize(String s){
		StringTokenizer token = new StringTokenizer(s, ", ");
		while(token.hasMoreTokens()){
			println(token.nextToken());
		}
	}
	
}
