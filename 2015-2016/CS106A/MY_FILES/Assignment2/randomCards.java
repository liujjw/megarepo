import acm.program.*;
import acm.util.*;

public class randomCards extends ConsoleProgram{
	
	public void run(){
		
		// intro
		println("This program returns a random card in a standard deck of 52.");
		
		// suit
		int suitNumber = rgen.nextInt(1, 4);
		String suitName; 
		switch(suitNumber){
		case 1: 
			suitName = "Spades";
			break;
		case 2:
			suitName = "Hearts";
			break;
		case 3:
			suitName = "Diamonds";
			break;
		case 4:
			suitName = "Clubs";
			break;
		default:
			suitName = null;
			break;
		}
		
		// rank
		int rankNumber = rgen.nextInt(1, 13);
		String rankName;
		switch(rankNumber){
		case 1:
			rankName = "Ace";
			println(rankName + " of " + suitName);
			break;
		case 11:
			rankName = "Jack";
			println(rankName + " of " + suitName);
			break;
		case 12:
			rankName = "Queen";
			println(rankName + " of " + suitName);
			break;
		case 13:
			rankName = "King";
			println(rankName + " of " + suitName);
			break;
		default:
			println(rankNumber + " of " + suitName);
			break;
		}
	}
	
	private RandomGenerator rgen = new RandomGenerator();
	
}
