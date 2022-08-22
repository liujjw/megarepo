package model;

import java.io.IOException;
import java.io.OutputStream;

import javafx.scene.control.Label;

/** Represents an outputstream that prints to a JavaFX label */
public class LabelOutputStream extends OutputStream {

	private Label label; // Holds the label

	/**
	 * Constructs a labeloutputstream that will write to the given label
	 * 
	 * @param l label to be written to
	 */
	public LabelOutputStream(Label l) {
		label = l;
	}

	@Override
	public void write(int b) throws IOException {
		String s = String.valueOf((char) b);
		label.setText(label.getText() + s);

	}

}
