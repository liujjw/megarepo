/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas
	implements NameSurferConstants, ComponentListener {

	/**
	* Creates a new NameSurferGraph object that displays the data.
	*/
	public NameSurferGraph() {
		addComponentListener(this);
	}
	
	/**
	* Clears the list of name surfer entries stored inside this class.
	*/
	public void clear() { 
		entryArray.clear();
		update();
	}
	
	/* Method: addEntry(entry) */
	/**
	* Adds a new NameSurferEntry to the list of entries on the display.
	* Note that this method does not actually draw the graph, but
	* simply stores the entry; the graph is drawn by calling update.
	*/
	public void addEntry(NameSurferEntry entry) {
		entryArray.add(entry);
		update();
	}
	
	
	/**
	* Updates the display image by deleting all the graphical objects
	* from the canvas and then reassembling the display according to
	* the list of entries. Your application must call update after
	* calling either clear or addEntry; update is also called whenever
	* the size of the canvas changes.
	*/
	public void update() {
		removeAll();
		createVerticalBars();
		createHorizontalBars();
		createDecadeNames();
		createEntries();
	}
	
	/* Adds vertical bars for each decade */
	private void createVerticalBars(){
		double barSpace = getWidth() / NDECADES; // space between each vertical bar 
		for(int i = 0; i < NDECADES; i++){
			double x1 = barSpace * i;
			double y1 = 0;
			double x2 = x1;
			double y2 = getHeight();
			add(new GLine(x1, y1, x2, y2));
		}
	}
	
	/* Adds horizontal bars*/
	private void createHorizontalBars(){	
		add(new GLine(0, GRAPH_MARGIN_SIZE, getWidth(), GRAPH_MARGIN_SIZE));
		add(new GLine(0, getHeight() -  GRAPH_MARGIN_SIZE, getWidth(), getHeight() - GRAPH_MARGIN_SIZE));
	}
	
	/* Adds in the decade names for each bar*/
	private void createDecadeNames(){
		double offset = 2; // tiny number of pixels away from the vertical bar
		for(int i = 0; i < NDECADES; i++){
			int decadeIteration = START_DECADE + (i * 10);
			GLabel decade = new GLabel("" + decadeIteration);
			double y = getHeight() - offset;
			double x = offset + (i * (getWidth() / NDECADES));
			add(decade, x, y);
		}
	}
	
	/* Adds the entries to the graph from the entry array*/
	private void createEntries(){
		Iterator<NameSurferEntry> it = entryArray.iterator();
		int colorCycle = 0; // random variable for cycling colors
		while(it.hasNext()){ // take each name surfer entry
			if(colorCycle > 3){
				colorCycle = 0;
			}
			
			NameSurferEntry entry = it.next();
			
			String name = entry.getName(); // take its name
			for(int i = 0; i < NDECADES; i++){
				int rank = entry.getRank(i); // take its rank
				
				GPoint pointCoord = determineCoords(i, rank); // determine coordinates
				addLabel(pointCoord, name, rank, colorCycle);
				
				if(i < NDECADES - 1){ // do this one less time
					GPoint nextCoord = determineCoords(i + 1, entry.getRank(i + 1)); // next rank 
					connectPoints(pointCoord, nextCoord);
				}
			}
			colorCycle++;
		}
	}
	
	/* Returns a GPoint given a rank and i */
	private GPoint determineCoords(int i, double rank){
		double x = (double) (i * (getWidth() / NDECADES));
		double y = GRAPH_MARGIN_SIZE + ((rank / 1000.0) * (getHeight() - (GRAPH_MARGIN_SIZE * 2.0))); // the graph height is height minus margin size, but plus margin size on top too	
		if(rank == 0){
			y = getHeight() - GRAPH_MARGIN_SIZE;
		}
		return new GPoint(x, y);
	}
	
	/* Adds a label to the given coordinate GPoint */ 
	private void addLabel(GPoint coordinate, String name, int rank, int colorCycle){
		String text = name + " " + rank;
		if(rank == 0){
			text = name + " *";
		}
		GLabel label = new GLabel(text);
		switch(colorCycle){
		case 0:
			label.setColor(Color.BLACK);
			COLOR = Color.BLACK;
			break;
		case 1:
			label.setColor(Color.RED);
			COLOR = Color.RED;
			break;
		case 2:
			label.setColor(Color.BLUE);
			COLOR = Color.BLUE;
			break;
		case 3:
			label.setColor(Color.MAGENTA);
			COLOR = Color.MAGENTA;
			break;
		default:
			label.setColor(Color.BLACK);
			COLOR = Color.BLACK;
			break;
		}
		add(label, coordinate.getX(), coordinate.getY());
	}
	
	/* Draws a line segment between two points */
	private void connectPoints(GPoint coord1, GPoint coord2){
		GLine segment = new GLine(coord1.getX(), coord1.getY(), coord2.getX(), coord2.getY());
		segment.setColor(COLOR); // an instance color shared with the name label's color
		add(segment);
	}
	
	/* Instance variables */
	private ArrayList<NameSurferEntry> entryArray = new ArrayList<NameSurferEntry>();
	private Color COLOR;
	
	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentResized(ComponentEvent e) { update(); }
	public void componentShown(ComponentEvent e) { }
}
