/*
 * File: ProgramHierarchy.java
 * Name: 
 * Section Leader: 
 * ---------------------------
 * This file is the starter file for the ProgramHierarchy problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class ProgramHierarchy extends GraphicsProgram {	
	
	private static final int BOX_WIDTH = 150;
	private static final int BOX_HEIGHT = 60;
	private static final int BRANCHES = 3;
	
	public void run() {
		drawRoot();
		for(int i = 0; i < BRANCHES; i++){
			drawBranch(i);
		}
	}
	
	private void drawRoot(){
		
		GRect root = new GRect(BOX_WIDTH, BOX_HEIGHT);
		double xBox = (getWidth() - BOX_WIDTH) / 2;
		double yBox = (((getHeight() - BOX_HEIGHT) / 2) / 2) - (root.getHeight() / 2);
		add(root, xBox, yBox);
		
		GLabel nameProgram = new GLabel("Program");
		double xLabel = (((xBox + root.getWidth()) - (nameProgram.getWidth())) + xBox) / 2;
		double yLabel = (((yBox + (yBox + root.getHeight())) / 2) + (nameProgram.getAscent()) / 2);
		add(nameProgram, xLabel, yLabel);
	}
	
	private void drawRootToBranch(double x, double y){
		double x1 = 1;
		double y1 = 1;
		double x2 = x;
		double y2 = y;
		add(new GLine(x1, y1, x2, y2));
	}
	
	private void drawBranch(int branchNumber){
		int arbritrarySpace = 20;
		GRect aBranch = new GRect(BOX_WIDTH, BOX_HEIGHT);
		int x = (getWidth() - (BRANCHES * BOX_WIDTH)) / 2 + (branchNumber * BOX_WIDTH) - arbritrarySpace + (arbritrarySpace * branchNumber);
		int y = ((getHeight() - BOX_HEIGHT) / 2) - (arbritrarySpace * 2);
		add(aBranch, x, y);
		
		switch(branchNumber){
		case 0:
			GLabel nameConsole = new GLabel("ConsoleProgram");
			double consoleLabelX = (((x + aBranch.getWidth()) - (nameConsole.getWidth())) + x) / 2;
			double consoleLabelY = (((y + (y + aBranch.getHeight())) / 2) + (nameConsole.getAscent()) / 2);
			add(nameConsole, consoleLabelX, consoleLabelY);
			
			double halfWayX = (consoleLabelX * 2 + aBranch.getWidth()) / 2;
			double justY = consoleLabelY;
			drawRootToBranch(halfWayX, justY);
			
			break;
		case 1:
			GLabel nameDialog = new GLabel("DialogProgram");
			double dialogLabelX = (((x + aBranch.getWidth()) - (nameDialog.getWidth())) + x) / 2;
			double dialogLabelY = (((y + (y + aBranch.getHeight())) / 2) + (nameDialog.getAscent()) / 2);
			add(nameDialog, dialogLabelX, dialogLabelY);
			
		    halfWayX = (dialogLabelX * 2+ aBranch.getWidth()) / 2;
			justY = dialogLabelY;
			drawRootToBranch(halfWayX, justY);
			
			break;
		case 2:
			GLabel nameGraphics = new GLabel("GraphicsProgram");
			double graphicsLabelX = (((x + aBranch.getWidth()) - (nameGraphics.getWidth())) + x) / 2;
			double graphicsLabelY = (((y + (y + aBranch.getHeight())) / 2) + (nameGraphics.getAscent()) / 2);
			add(nameGraphics, graphicsLabelX, graphicsLabelY);
			
			halfWayX = (graphicsLabelX * 2 + aBranch.getWidth()) / 2;
			justY = graphicsLabelY;
			drawRootToBranch(halfWayX, justY);
			
			break;
		default:
			GLabel defaultName = new GLabel("null");
			double defaultX = (((x + aBranch.getWidth()) - (defaultName.getWidth())) + x) / 2;
			double defaultY = (((y + (y + aBranch.getHeight())) / 2) + (defaultName.getAscent()) / 2);
			add(defaultName, defaultX, defaultY);
			
			halfWayX = (defaultX * 2 + aBranch.getWidth()) / 2;
			justY = defaultY;
			drawRootToBranch(halfWayX, justY);
			
			break;
		}
	}
}

