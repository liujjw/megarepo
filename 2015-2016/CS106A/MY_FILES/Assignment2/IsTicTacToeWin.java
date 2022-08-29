import acm.program.*;

public class IsTicTacToeWin extends ConsoleProgram{
    
	public void run(){
		println("This program tests the isWinningPosition method. Enter the state of the board, row by row.");
		char[][] board = new char[3][3];
		
		String playerString = readLine("Which player to check?");
		char player = playerString.charAt(0);
		player = Character.toUpperCase(player);

		while(player != 'O' && player != 'X'){
			println("invalid");
			playerString = readLine("Which player to check?");
			player = playerString.charAt(0);
			player = Character.toUpperCase(player);
		}
		
		
		println("Enter board state now: ");
		
		for(int i = 0; i < board.length; i++){
			String oneRow = readLine();
			for(int k = 0; k < board.length; k++){
				board[i][k] = oneRow.charAt(k);
			}
		}
		
		if(isWinningPosition(board, player)){
			println(player + " has won.");
		}else{
			println(player + " has not won");
		}
	}
	
	
	private boolean isWinningPosition(char[][] board, char player){
		/* x's win conditions, i.e. we can enumerate x's 8 win conditions manually and do the same for y, == 16 * 3 lines to write ;(((((*
		 * better what? */
		
		for(int i = 0; i < board.length; i++){
			for(int k = 0; k < board[i].length; k++){
				board[i][k] = Character.toUpperCase(board[i][k]);
			}
		}
		
		if(Character.toUpperCase(player) == 'X'){
			/*XXX
			 *XX
			 *X X*/
			if(board[0][0] == 'X'){
				if(board[0][1] == 'X'){
					if(board[0][2] == 'X'){
						return true;
					}
				}else if(board[1][0] == 'X'){
					if(board[2][0] == 'X'){
						return true;
					}
				}else if(board[1][1] == 'X'){
					if(board[2][2] == 'X'){
						return true;
					}
				}
			}
			/*  X
			 *  X
			 *XXX*/
			if(board[2][2] == 'X'){
				if(board[2][1] == 'X'){
					if(board[2][0] == 'X'){
						return true;
					}
				}else if(board[1][2] == 'X'){
					if(board[0][2] == 'X'){
						return true;
					}
				}
			}
		}else if(Character.toUpperCase(player) == 'O'){
			/*XXX
			 *XX
			 *X X*/
			if(board[0][0] == 'O'){
				if(board[0][1] == 'O'){
					if(board[0][2] == 'O'){
						return true;
					}
				}else if(board[1][0] == 'O'){
					if(board[2][0] == 'O'){
						return true;
					}
				}else if(board[1][1] == 'O'){
					if(board[2][2] == 'O'){
						return true;
					}
				}
			}
			/*  X
			 *  X
			 *XXX*/
			if(board[2][2] == 'O'){
				if(board[2][1] == 'O'){
					if(board[2][0] == 'O'){
						return true;
					}
				}else if(board[1][2] == 'O'){
					if(board[0][2] == 'O'){
						return true;
					}
				}
			}
		}

		
		return false;


	}
}

/* 
 * Tic tac toe winning positions:
 * a predicate method that returns true or false given a board state array
 * 1. print info
 * 2. enter state of board row by row
 * 3. initialize board as 2d array of chars 
 * 3. capture one row as readline
 * 4. explode into chars and store each in a cell of array
 * 5. check array for possible wins 
				
		for(int i = 0; i < board.length; i++){
			// iterate over columns 
			for(int k = 0; k < board[i].length; k++){
				
			}
		
		}
 * */
