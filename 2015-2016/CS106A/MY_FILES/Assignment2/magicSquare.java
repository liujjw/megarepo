public class magicSquare{
	
	public magicSquare(){
		
	}
	
	public boolean isMagicSquare(int[][] array){
		// one row reference
		int referenceRow = 0;
		for(int i = 0; i < array[0].length; i++){
			referenceRow += array[0][i];
		}
		
		// check var
		int reference = 0;
		// check the rest of the rows against reference
		for(int i = 1; i < array.length; i++){
			reference = 0;
			for(int j = 0; j < array[i].length; j++){
				reference += array[i][j]; 
			}
			if(reference != referenceRow){
				return false;
			}
		}
		
		// check columns against reference
		for(int i = 0; i < array.length; i++){
			reference = 0;
			for(int j = 0; j < array[i].length; j++){
				reference += array[j][i];
			}
			if(reference != referenceRow){
				return false;
			}
		}
		
		// check diagonals
		reference = 0;
		for(int i = 0; i < array.length; i++){
			reference += array[i][i];
		}
		if(reference != referenceRow){
			return false;
		}
		
		reference = 0;
		for(int i = array.length; i > 0; i--){
			reference += array[i - 1][i - 1];
		}
		if(reference != referenceRow){
			return false;
		}
		
		return true;
	}
}

/*Algorithmic design:
 * 1. find the value of a reference row
 * 2. compare that reference row to other rows
 * 3. then compare to columns
 * 4. then compare to diagonals*/
