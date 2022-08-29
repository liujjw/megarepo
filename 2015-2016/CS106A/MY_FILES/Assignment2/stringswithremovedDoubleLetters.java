import acm.program.*;

public class stringswithremovedDoubleLetters extends ConsoleProgram{
	public void run(){
		String s = readLine("?");
		println(removeDoubledLetters(s));
	}
	public String removeDoubledLetters(String s){
		String newS = s;
		for(int i = 0; i < newS.length(); i++){
			if(!(i + 1 > newS.length() - 1)){
				if(newS.charAt(i) == newS.charAt(i + 1)){
					String beg = newS.substring(0, i + 1);
					String end = newS.substring((i + 1) + 1);
					newS = beg + end;
				}
			}
		}
		return newS;
	}
	
	/*Concatenating to result if not equal to repvious result, i - 1 works because if i is 0, the condtional will already be true, ie only evaulate the i == 0 part and not the other part so i - 1 will occur when i is never 0*/
	public String removeVERSIONTWO(String s){
		String result = "";
		for(int i = 0; i < s.length(); i++){
			char character = s.charAt(i);
			if(i == 0 || character != s.charAt(i - 1)){
				result += character;
			}
		}
		return result;
	}
}
