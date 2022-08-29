import java.util.Arrays;

import acm.program.*;

public class Dutch_National_Flag extends ConsoleProgram{
	
	public void run(){
		
		println(Arrays.toString(jumbledColors));
		
		sortBlues();
		sortReds();
		
		println(Arrays.toString(jumbledColors));
		
	}
	
	private void sortBlues(){
		int upperBound = jumbledColors.length - 1;
		int indexB = findColor(jumbledColors, 'B', upperBound);
		int end = scoutIndex(jumbledColors, 'B');
		
		while(indexB != -1){
			swap(jumbledColors, indexB, endIndex);
			indexB = findColor(jumbledColors, 'B', upperBound);
			upperBound--;
			end = scoutIndex(jumbledColors, 'B');
		}
	}
	
	private void sortReds(){
		int lowerBound = 0;
		int indexR = findColor(jumbledColors, 'R', lowerBound);
		int front = scoutIndex(jumbledColors, 'R');
		
		while(indexR != -1){
			swap(jumbledColors, indexR, front);
			indexR = findColor(jumbledColors, 'R', lowerBound);
			lowerBound++;
			front = scoutIndex(jumbledColors, 'R');
		}
	}
	
	private void swap(char[] jumbledColors, int ind1, int ind2){
		char tmp = jumbledColors[ind1];
		jumbledColors[ind1] = jumbledColors[ind2];
		jumbledColors[ind2] = tmp;
	}
	
	private int findColor(char[] jumbledColors, char color, int bound){
		if(color == 'B'){
			for(int i = 0; i < bound; i++){
				if(jumbledColors[i] == color){
					return i;
				}
			}
			return -1;
		}else if(color == 'R'){
			for(int i = bound; i < jumbledColors.length; i++){
				if(jumbledColors[i] == color){
					return i;
				}
			}
		}
		
		return -1;
	}
	
	private int scoutIndex(char[] array, char chr){
		if(chr == 'B'){
			int index = array.length - 1;
			while(array[index] == chr){
				if(index - 1 < 2){
					return -1;
				}
				index--;
			}
			return index;
		}else if(chr == 'R'){
			int index = 0;
			while(array[index] == chr){
				if(index + 1 > array.length - 1){
					return -1;
				}
				index++;
			}
			return index;
		}
		return -1;
	}
	
	/*
	 * 
	 * intial state: given
	 * 1: finds b and swaps with w at the end
	 * 2. finds another b and swaps at end - 1
	 * 3. finds anotehr b and swaps at end -3
	 * 4. finds r and swaps with begining + 1
	 * 5. finds the last b and swaps with the end - 4
	 * 6. finds another r and swaps with begin + 2
	 * 7. finds last r and sticks it in
	 * 8. w will already be ordered once both ends are ordered!
	 * */

	private char[] jumbledColors = {
			'R',
			'B',
			'R',
			'W',
			'R',
			'W',
			'B',
			'R',
			'B',
			'W',
			'B',
			'R',
			'W',
			'R',
			'B',
			'W',
			'W',
			'B',
			'W',
			'B',
			'B',
	};
}
