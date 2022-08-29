import acm.program.*;

public class selectionSort extends ConsoleProgram{
	
	public void run(){
		alphabetize(d);
		
		for(int i = 0; i < d.length; i++){
			println(d[i]);
		}
	}
	
	private void alphabetize(String[] stringArray){
		for(int i = 0; i < stringArray.length; i++){
			
			int largestDifference = 0;
			int index = 0;
			
			for(int j = i; j < stringArray.length; j++){
				int comparison = stringArray[i].compareTo(stringArray[j]);
				
				if(comparison > 0){
					if(comparison > largestDifference){
						largestDifference = comparison;
						index = j;
					}
				}
			}
			
			if(largestDifference != 0){
				String tmp = stringArray[index];
				stringArray[index] = stringArray[i];
				stringArray[i] = tmp;
			}
		}
	}
	
	
	
	String[] d = {
			"market",
			"flames",
			"claim",
			"me",
			"death",
			"aatrox",
	};
	
}
