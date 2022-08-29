/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;

public class Hangman extends ConsoleProgram {

	public void init(){
		canvas = new HangmanCanvas();
		add(canvas);
	}
	
    public void run() {
    	println("Welcome to HANGMAN...where you watch a stick figure gradually come into being hanged");
    	playInConsole();	
	}
    
    private void playInConsole(){
		
    	canvas.reset(); // resets the canvas into being 
    	
		/* Get a secret word, make sure its uppercase */
    	String secretWord = getWord();
    	if(secretWord.equals(INTERNAL_ERROR)){
    		println(INTERNAL_ERROR);
    	}else{
        	
        	/* Number of guesses */
        	int guesses = MAX_GUESSES;
        	
        	/* What the player sees */
    		String hiddenWord = underScorealize(secretWord);
    		println(hiddenWord);
    		
    		/* Win or lose */
    		boolean isWin = false;
    		
        	/* Loops as long as there are guesses left*/
        	while(guesses > 0){
        		
        		/* Guesses left */
        		if(guesses == 1){
        			println(guesses + " guess left!!!!");
        		}else{
        			println(guesses + " guesses left");
        		}
        		
        		/* Ask user for a guess at the word*/
        		char userGuess = getGuess();
        		
        		/* Update word based on if the guess was right or not*/
        		for(int i = 0; i < secretWord.length(); i++){
        			if(secretWord.charAt(i) == userGuess){
        				println("Yep!");
        				hiddenWord = updateHiddenWord(hiddenWord, userGuess, secretWord);
        				canvas.displayWord(hiddenWord);
        				break;
        			}else if(i == secretWord.length() - 1){
        	    		/* Assumed to be not in word, user loses a guess*/
        	    		println("Nope. Lose a guess!");
        	    		guesses--;
        	    		canvas.noteIncorrectGuess(userGuess);
        	    		canvas.displayWord(hiddenWord);
        			}
        		}
        		
        		/* The word now looks like this */
        		println(hiddenWord);
        		
        		/* if hidden word is correct now*/
        		if(hiddenWord.equals(secretWord)){
        			println("You guessed the word: " + secretWord + "!");
        			println("You win...for now");
        			isWin = true;
        			break;
        		}
        	}
        	
        	/* gg */
        	if(!(isWin)){
            	println("The word was: " + secretWord + ".");
            	println("GAME OVER, LOSER");
        	}
    	}


    }
    
	/* Takes the current hidden word and applies the userguess to it if it matches the secretword*/
	private String updateHiddenWord(String hiddenWord, char userGuess, String secretWord){
		String newString = hiddenWord;
		
		for(int i = 0; i < secretWord.length(); i++){
			if(userGuess == secretWord.charAt(i)){
				String start = newString.substring(0, i);
				String end = newString.substring(i + 1);
				newString = start + userGuess + end;
			}
		}
		
		return newString;
	}
    
    private String underScorealize(String s){
    	String underScores = "";
    	for(int i = 0; i < s.length(); i++){
    		underScores += "-";
    	}
    	return underScores;
    }
    
    private char getGuess(){
    	String myGuess = readLine("Your guess: ");
    	while(myGuess.length() > 1 || !(Character.isLetter(myGuess.charAt(0))) ){
    		myGuess = readLine("Only one character please. Try again: ");
    	}
    	myGuess = myGuess.toUpperCase();
    	return myGuess.charAt(0);
    	
    }
    
    private String getWord(){      
    	String secretMessage = lexicon.getWord(rgen.nextInt(lexicon.getWordCount()));
    	secretMessage = secretMessage.toUpperCase();
    	if(secretMessage.contains(" ")){
    		return INTERNAL_ERROR;
    	}
    	return secretMessage;
    }
    
    // Private iVars //
    HangmanLexicon lexicon = new HangmanLexicon();
    private RandomGenerator rgen = new RandomGenerator();
    private static final int MAX_GUESSES = 8;
    private final static String INTERNAL_ERROR = "Internal Error";
    
    private HangmanCanvas canvas;

}

