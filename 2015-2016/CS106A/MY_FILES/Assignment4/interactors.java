import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class interactors extends ConsoleProgram{
	public void init(){
		check = new JCheckBox("Front");
		check.setSelected(true);
		add(check, SOUTH);
		if(check.isSelected()){
			println("hey");
		}
	}
	
	JCheckBox check;
}
