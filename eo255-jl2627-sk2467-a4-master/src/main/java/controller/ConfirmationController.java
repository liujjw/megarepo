package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

/** A class controlling the confirmation UI */
public class ConfirmationController implements Initializable {

	@FXML
	private Label successLabel;
	@FXML
	private Label errorLabel;
	@FXML
	private ScrollPane errorStuff;
	@FXML
	private HBox confirmationStuff;
	@FXML
	private Label errorDescriptionLabel;
	@FXML
	private Button okButton;

	private boolean isError;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	/**
	 * Decides whether to show error or confirmation materials
	 * 
	 * @param b whether or not this window represents an error
	 */
	public void setError(boolean b) {
		isError = b;
		if (b) {
			successLabel.setText("");
			successLabel.setPrefWidth(0);
			confirmationStuff.setPrefWidth(0);
			confirmationStuff.setVisible(false);

		} else {
			errorLabel.setText("");
			errorLabel.setPrefWidth(0);
			errorStuff.setPrefWidth(0);
			errorStuff.setVisible(false);
		}
	}

	/**
	 * Checks whether this confirmation is an error or not
	 * 
	 * @return whether or not error
	 */
	public boolean isError() {
		return isError;
	}

	/**
	 * Gets the label which would display all possible error messages
	 * 
	 * @return JavaFX Label reference
	 */
	public Label getErrorDescriptionLabel() {
		return errorDescriptionLabel;
	}

	/**
	 * Gets the button with the intention of adding an event handler
	 * 
	 * @return ok button
	 */
	public Button getButton() {
		return okButton;
	}

}
