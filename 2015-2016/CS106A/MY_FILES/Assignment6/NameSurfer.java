/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class NameSurfer extends Program implements NameSurferConstants {

/* Method: init() */
/**
 * This method has the responsibility for reading in the data base
 * and initializing the interactors at the bottom of the window.
 */
	public void init() {
		/*Adding the interactors*/
		textfield = new JTextField(MAX_LENGTH);
		textfield.addActionListener(this); // for enter key in text field
		graphButton = new JButton("Graph");
		clearButton = new JButton("Clear");

	    add(new JLabel("Name"), SOUTH);
		add(textfield, SOUTH);
		add(graphButton, SOUTH);
		add(clearButton, SOUTH);
		
		addActionListeners(); // for buttons
		
		/* Initializing database */
		namesDataBase = new NameSurferDataBase(NAMES_DATA_FILE);
		
		/* Adding the graph*/
		namesGraph = new NameSurferGraph();
		add(namesGraph);
	}

/* Method: actionPerformed(e) */
/**
 * This class is responsible for detecting when the buttons are
 * clicked, so you will have to define a method to respond to
 * button actions.
 */
	public void actionPerformed(ActionEvent e) {
		String text = textfield.getText();
		Object source = e.getSource();
		
		if(source == textfield || source == graphButton){
			NameSurferEntry entry = namesDataBase.findEntry(text);
			if(entry == null){
				//notfiyError();
			}else{
				namesGraph.addEntry(entry);
			}
		}else if(source == clearButton){
			namesGraph.clear();
		}
	}
	
/**
 * Instance variables*/
	private JTextField textfield;
	private int MAX_LENGTH = 15;
	private JButton graphButton;
	private JButton clearButton;
	
/**
 * NameSurfer facilities*/
	private NameSurferDataBase namesDataBase;
	private NameSurferGraph namesGraph;
}
