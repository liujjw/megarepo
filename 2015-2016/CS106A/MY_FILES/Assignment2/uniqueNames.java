import acm.program.*;
import java.util.*;

public class uniqueNames extends ConsoleProgram{
	/*
	 * Reads in a list of names 
	 * Read into an arraylist
	 * Print out array list, but not duplicates */
	public void run(){
		while(true){
			String input = readLine("Name: ");
			if(input.equals("")) break;
			
			addToArray(input);
		}
		
		println("Names in this list include: ");
		for(int i = 0; i < names.size(); i++){
			println(names.get(i));
		}
	}
	
	private void addToArray(String s){
		if(names.indexOf(s) == -1){
			names.add(s);
		}
	}
	
	ArrayList<String> names = new ArrayList<String>();
}
