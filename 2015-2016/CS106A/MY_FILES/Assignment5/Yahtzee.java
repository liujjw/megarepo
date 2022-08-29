/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}

	private void playGame() {
		display.printMessage("Welcome to Yahtzee"); // welcome message
		usedCategories = new int[17 + 1][nPlayers + 1]; // initializes array after getting number of players
		initializeCategoryArray(usedCategories); // makes sure an array used to keep track of categories used is all false
		totals = new int[nPlayers][1];
		
		for(int i = 0; i < 13; i++){ // 13 rounds
			for(int j = 1; j <= nPlayers; j++){ // n player number of player turns each round, start at 1 index for methods 	
				
				display.printMessage("Roll your dice.");
				display.waitForPlayerToClickRoll(j); // at each round, each player gets a turn to roll dice and get their points
				
				rollDice(); // rolls dice once
				display.displayDice(diceArray); // updates display of dice
				
				for(int m = 0; m < 2; m++){ // 2 chances to improve roll
					display.printMessage("Select dice to re-roll and roll again.");
					display.waitForPlayerToSelectDice(); 
					for(int z = 0; z < N_DICE; z++){ // checks to see whether a die was selected for re roll
						if(display.isDieSelected(z)){
							rerollDice(z);
						}
					}
					display.displayDice(diceArray); // redisplays dice
				}
				
				display.printMessage("Choose a category for your dice values.");
				int category = determineCategory(j - 1); // select a category for which to assign points
				int score = calculateScore(category, diceArray); // finds the score, error checking for category, determines if the category us met with the dice array
				display.updateScorecard(category, j, score); // updates the score
				totals[j - 1][0] += score; // updates totals
				display.updateScorecard(TOTAL, j, totals[j - 1][0]);
			}
		}
		
		int highestScore = 0;
		int highestScoringPlayer = 0;
		for(int i = 1; i < nPlayers + 1; i++){
			// upper score, upper bonus
			int upperScore = computeUpperScore(usedCategories, i);
			display.updateScorecard(UPPER_SCORE, i, upperScore);
			
			// lower score
			int lowerScore = computeLowerScore(usedCategories, i);
			display.updateScorecard(LOWER_SCORE, i, lowerScore);
			
			// final
			int finalScore = upperScore + lowerScore;
			usedCategories[TOTAL][i] = finalScore;
			display.updateScorecard(TOTAL, i, finalScore);
			
			if(usedCategories[TOTAL][i] > highestScore){
				highestScore = usedCategories[TOTAL][i];
				highestScoringPlayer = i - 1;
			}
		}
		
		display.printMessage(playerNames[highestScoringPlayer] + " has won!");
		
	}
	
	private int computeUpperScore(int[][] usedCategories, int player){
		int totalScore = 0;
		for(int i = 1; i <= SIXES; i++){
			totalScore += usedCategories[i][player - 1];
		}
		if(totalScore >= 63){
			display.updateScorecard(UPPER_BONUS, player, 35);
			totalScore += 35;
		}else{
			display.updateScorecard(UPPER_BONUS, player, 0);
		}
		return totalScore;
	}
	
	private int computeLowerScore(int[][] usedCategories, int player){
		int totalScore = 0;
		for(int i = THREE_OF_A_KIND; i <= CHANCE; i++){
			totalScore += usedCategories[i][player - 1];
		}
		return totalScore;
	}
	
	/* Simulates rolling of N (5) dice and stores results in dice array*/
	private void rollDice(){
		for(int i = 0; i < N_DICE; i++){
			int roll = rgen.nextInt(1,6);
			diceArray[i] = roll;
		}
	}
	
	/* Re-rolls a single die for a single dice array index */
	private void rerollDice(int index){
		int reroll = rgen.nextInt(1,6);
		diceArray[index] = reroll;
	}
	
	private int determineCategory(int player){
		int category = display.waitForPlayerToSelectCategory();
		
		while(category == UPPER_SCORE || category == UPPER_BONUS || category == LOWER_SCORE|| 
				category == TOTAL || usedCategories[category][player] != 0){ // while the category the user picked is already used or invalid
			display.printMessage("Invalid category!!");
			category = display.waitForPlayerToSelectCategory();
		}
		usedCategories[category][player] = calculateScore(category, diceArray); // used categories for this category filled in 
		return category;
	}
	
	/* Initializes all booleans in a boolean array to false*/
	private void initializeCategoryArray(int[][] usedCategories){
		for(int i = 0; i < usedCategories.length; i++){
			for(int j = 0; j < usedCategories[i].length; j++){
				usedCategories[i][j] = 0;
			}
		}
	}
	
	/* Determines score for dice array and category*/
	private int calculateScore(int category, int[] diceArray){
		int score = 0;
		
		if(category == ONES || category == TWOS || category == THREES
				|| category == FOURS || category == FIVES || category == SIXES){ // 1-6s condition, adds all the 1,2,3... or 6s
			for(int i = 0; i < diceArray.length; i++){
				if(diceArray[i] == category){
					score += category; // add up category scores
				}
			}
		}else if(category == THREE_OF_A_KIND){ 	
			// no need to check last 2 because can't be 3 of a kind since the first 3 will check against them
			for(int i = 0; i < diceArray.length - 2; i++){ 
				int counter = 1; // one match with itself initially
				
				for(int j = i + 1; j < diceArray.length; j++){ // will check against all five values though, starts at where i left off plus one, since it will not match with i
					if(diceArray[i] == diceArray[j]){
						counter++; // counts the number of times the value at dice array[i] shows up in the rest of the values starting at that i plus 1
					}
				}
				
				if(counter >= 3){ // if that amount is 3 or more then award points
					for(int f = 0; f < diceArray.length; f++){
						score += diceArray[f];
					}
				}
			}	
		}else if(category == FOUR_OF_A_KIND){ 
			for(int i = 0; i < diceArray.length - 3; i++){ // no need to check the last 3 bc by design they will not match the above, and 3 will never give four of a kind
				int counter = 1; 
				
				for(int j = i + 1; j < diceArray.length; j++){ 
					if(diceArray[i] == diceArray[j]){
						counter++; 
					}
				}
				
				if(counter >= 4){ 
					for(int f = 0; f < diceArray.length; f++){
						score += diceArray[f];
					} 
				}
			}	
		}else if(category == YAHTZEE){
			int counter = 1; 
			
			for(int j = 1; j < diceArray.length; j++){ 
				if(diceArray[0] == diceArray[j]){ // just checks the first value against all the rest
					counter++; 
				}
			}
			
			if(counter >= 5){ 
				score = 50;
			}
		}else if(category == FULL_HOUSE){  
			for(int i = 0; i < diceArray.length - 2; i++){ // look for three of a kind
				int counter = 1;
				
				for(int j = i + 1; j < diceArray.length; j++){ 
					if(diceArray[i] == diceArray[j]){
						counter++; 
					}
				}
				
				if(counter >= 3){ // if yes, then look for two of a kind
					for(int p = 0; p < diceArray.length - 1; p++){ 
						int counter2 = 1;
						
						for(int j = p + 1; j < diceArray.length; j++){ 
							if(diceArray[p] == diceArray[j]){
								counter2++; 
							}
						}
						
						if(counter2 >= 2){
							score = 25;
						}
					}
				}
			}
		}else if(category == CHANCE){
			for(int i = 0; i < diceArray.length; i++){
				score += diceArray[i];
			}
		}else if(category == SMALL_STRAIGHT){ 
			if(contains(3, diceArray)){
				if(contains(4, diceArray)){
					if(contains(5, diceArray)){
						if(contains(6, diceArray)){
							score = 30;
						}else if(contains(2, diceArray)){
							score = 30;
						}
					}else if(contains(1, diceArray)){
						score = 30;
					}
				}
			}
		}else if(category == LARGE_STRAIGHT){ 
			if(contains(2, diceArray)){
				if(contains(1, diceArray)){
					if(contains(3, diceArray)){
						if(contains(4, diceArray)){
							if(contains(5, diceArray)){
								score = 40;
							}
						}
					}
				}else if(contains(6, diceArray)){
					if(contains(3, diceArray)){
						if(contains(4, diceArray)){
							if(contains(5, diceArray)){
								score = 40;
							}
						}
					}
				}
			}
		}
		
		return score; // return the accumulation of scores of the conditions above
	}
	
	/* Checks to see if array contains specified value */
	private boolean contains(int value, int[] diceArray){
		for(int i = 0; i < diceArray.length; i++){
			if(diceArray[i] == value){
				return true;
			}
		}
		
		return false;
	}
		
/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private int[] diceArray = new int[N_DICE];
	private int[][] usedCategories;
	private int[][] totals;
	private RandomGenerator rgen = new RandomGenerator();

}


