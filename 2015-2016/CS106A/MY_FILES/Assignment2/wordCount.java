import acm.program.*;
import java.io.*;

public class wordCount extends ConsoleProgram{
	public void run(){
		int lines = 0;
		int chars = 0;
		int words = 0;
		
		BufferedReader fileBuffer = openFile();
		
		try{
			while(true){
				String s = fileBuffer.readLine();
				if(s == null) break;
				lines++;
				
				chars += s.length();
				words += calculateWords(s);
			}
			
			println("Lines: " + lines);
			println("Words: " + words);
			println("Chars: " + chars);
		}catch(IOException ex){
			println("IOException occured");
		}
	}
	
	private BufferedReader openFile(){
		BufferedReader rd = null;
		while(rd == null){
			String filename = readLine("Give me a filename: ");
			try{
				rd = new BufferedReader(new FileReader(filename));
			}catch(IOException e){
				println("Could not open that file");
			}
		}
		return rd;
	}
	
	private int calculateWords(String s){
		int numberOfWords = 0;
		
		boolean inWord = false;
		
		for(int i = 0; i < s.length(); i++){
			if(Character.isLetterOrDigit(s.charAt(i))){
				inWord = true;
			}else{
				if(inWord) numberOfWords++;
				inWord = false;
			}
		}
		if(inWord) numberOfWords++;
		return numberOfWords;
	}
}