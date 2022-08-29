/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;

public class HangmanCanvas extends GCanvas {

/** Resets the display so that only the scaffold appears */
	public void reset() {
		removeAll();
		
		// Pole
		double px1 = (getWidth() / 2) - BEAM_LENGTH;
		double py1 = 0.6 * getHeight();
		double px2 = px1;
		double py2 = py1 - SCAFFOLD_HEIGHT;
		GLine gallowPole = new GLine(px1, py1, px2, py2);
		add(gallowPole);
		
		// Beam
		double bx1 = px1;
		double by1 = py2;
		bx2 = bx1 + BEAM_LENGTH;
		double by2 = by1;
		GLine gallowBeam = new GLine(bx1, by1, bx2, by2);
		add(gallowBeam);
		
		// Rope 
		double rx1 = bx2;
		double ry1 = by2;
		double rx2 = bx2;
		ry2 = ry1 + ROPE_LENGTH;
		GLine gallowRope = new GLine(rx1, ry1, rx2, ry2);
		add(gallowRope);
	}

/**
 * Updates the word on the screen to correspond to the current
 * state of the game.  The argument string shows what letters have
 * been guessed so far; unguessed letters are indicated by hyphens.
 */
	public void displayWord(String word) {
		secretWord = new GLabel(word);
		secretWord.setFont("Times New Roman-36");
		add(secretWord, 0.2 * getWidth(), 0.75 * getHeight());
	}

/**
 * Updates the display to correspond to an incorrect guess by the
 * user.  Calling this method causes the next body part to appear
 * on the scaffold and adds the letter to the list of incorrect
 * guesses that appears at the bottom of the window.
 */
	public void noteIncorrectGuess(char letter) {
		
		// Concat letter to string of wrong guesses
		if(!(wrongGuesses.contains(letter + ""))){
			reset();
			wrongGuesses = wrongGuesses + letter;
			GLabel wrongGuessesLabel = new GLabel(wrongGuesses);
			double x = 0.2 * getWidth(); 
			double y = 0.8 * getHeight();
			add(wrongGuessesLabel, x, y);
		}
		
		if(wrongGuesses.length() >= 1){
			// draw head
			GOval head = new GOval(HEAD_RADIUS * 2, HEAD_RADIUS * 2);
			double hx = (getWidth() - head.getWidth()) / 2;
			double hy = ry2; /*Uses the rope y2 position, declared as ivar*/
			add(head, hx, hy);
			
			if(wrongGuesses.length() > 1){
				// draw body
				double bdx1 = bx2; /*Uses beam x2 position declared as an ivar*/
				double bdy1 = hy + HEAD_RADIUS * 2;
				double bdx2 = bdx1;
				double bdy2 = bdy1 + BODY_LENGTH;
				GLine body = new GLine(bdx1, bdy1, bdx2, bdy2);
				add(body);
				
				if(wrongGuesses.length() > 2){
					// draw left arm
					double lx1 = bdx1; 
					double ly1 = bdy1 + ARM_OFFSET_FROM_HEAD;
					double lx2 = lx1 - UPPER_ARM_LENGTH;
					double ly2 = ly1;
					GLine leftArm = new GLine(lx1, ly1, lx2, ly2);
					add(leftArm);
					
					if(wrongGuesses.length() > 3){
						// draw right arm
						double rx1 = bdx1; 
						double ry1 = bdy1 + ARM_OFFSET_FROM_HEAD;
						double rx2 = lx1 + UPPER_ARM_LENGTH;
						double ry2 = ly1;
						GLine RightArm = new GLine(rx1, ry1, rx2, ry2);
						add(RightArm);
						
						if(wrongGuesses.length() > 4){
							// draw left leg
							double leglx1 = bdx1; 
							double legly1 = bdy1;
							double leglx2 = lx1 - HIP_WIDTH * 2;
							double legly2 = bdy2 + LEG_LENGTH;
							GLine LeftLeg = new GLine(leglx1, legly1, leglx2, legly2);
							add(LeftLeg);
							
							if(wrongGuesses.length() > 5){
								// draw right leg
								double legrx1 = bdx1; 
								double legry1 = bdy1;
								double legrx2 = lx1 + HIP_WIDTH * 2;
								double legry2 = bdy2 + LEG_LENGTH;
								GLine RightLeg = new GLine(legrx1, legry1, legrx2, legry2);
								add(RightLeg);
								
								if(wrongGuesses.length() > 6){
									// draw left foot
									
									if(wrongGuesses.length() > 7){
										// draw right foot
										
									}
								}
							}
						}
					}
				}
			}
		}
		
	}

/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;
	
	private String wrongGuesses = "";
	double ry2;
	double bx2;
	
	GLabel secretWord;
}
