import acm.graphics.*;
import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class section7Interactors extends GraphicsProgram{
	public void run(){
		
		add(new JLabel("Name: "), SOUTH);
		
		textBox = new JTextField(25);
		textBox.addActionListener(this);
		add(textBox, SOUTH);
		
		add = new JButton("Add");
		add(add, SOUTH);
		
		remove = new JButton("Remove");
		add(remove, SOUTH);

		clear = new JButton("Clear");
		add(clear, SOUTH);
		
		addActionListeners();
		addMouseListeners();
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == textBox || e.getActionCommand().equals("Add")){
			String s = textBox.getText();
			textbox textRect = new textbox(s);
			double y = (getHeight() - textRect.getHeight()) / 2;
			double x = (getWidth() - textRect.getWidth()) / 2;
			add(textRect, x, y);
		}else if(e.getActionCommand().equals("Remove")){
			if(toBeDragged != null){
				remove(toBeDragged);
			}
		}else if(e.getActionCommand().equals("Clear")){
			removeAll();
		}
	}
	
	public void mousePressed(MouseEvent e){
		toBeDragged = getElementAt(e.getX(), e.getY());
		lastX = e.getX();
		lastY = e.getY();
	}
	
	public void mouseDragged(MouseEvent e){
		if(toBeDragged != null){
			toBeDragged.move(e.getX() - lastX, e.getY() - lastY);
			lastX = e.getX();
			lastY = e.getY();
		}
	}
	
	// Instance variable for fields
	JTextField textBox;
	JButton add;
	JButton remove;
	JButton clear;
	
	// Instance Variables for dragging
	GObject toBeDragged;
	double lastX;
	double lastY;
}
