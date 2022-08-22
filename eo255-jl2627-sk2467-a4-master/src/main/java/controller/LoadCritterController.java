package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

/**
 * A controller class for the LoadCritter scene. Has little functionality except
 * increasing or decreasing number of critters to spawn when appropriate buttons
 * pressed. Otherwise, contains getters for fields that a more knowledgeable
 * class can add event handlers to.
 */
public class LoadCritterController implements Initializable {
	@FXML
	private Label critterFilename;
	@FXML
	private Button minus;
	@FXML
	private Label numSpawn;
	@FXML
	private Button plus;
	@FXML
	private Button cancel;
	@FXML
	private Button confirm;
	@FXML
	private HBox rowColBox;
	@FXML
	private HBox locationBox;
	@FXML
	private CheckBox locationCheckBox;
	@FXML
	private TextArea rowSpawn;
	@FXML
	private TextArea colSpawn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		numSpawn.setText("1");
		cancel.setCancelButton(true);
		confirm.setDisable(false);
		// force the field to be numeric only
		rowSpawn.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					rowSpawn.setText(newValue.replaceAll("[^\\d]", ""));
				}
				if (getRow() >= 0 && getCol() >= 0)
					confirm.setDisable(false);
			}
		});
		colSpawn.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					colSpawn.setText(newValue.replaceAll("[^\\d]", ""));
				}
				if (getRow() >= 0 && getCol() >= 0)
					confirm.setDisable(false);
			}
		});

	}

	/**
	 * Gets the cancel button.
	 * 
	 * @return cancel button reference
	 */
	public Button getCancel() {
		return cancel;
	}

	/**
	 * Gets the confirmation button.
	 * 
	 * @return confirmation button reference
	 */
	public Button getConfirm() {
		return confirm;
	}

	@FXML
	/**
	 * Represents when the checkbox was checked or not
	 */
	private void checked(ActionEvent ae) {
		if (!locationCheckBox.isSelected()) {
			rowColBox.setDisable(true);
			return;
		}
		rowColBox.setDisable(false);
	}

	@FXML
	/**
	 * Represents the action of the user clicking the plus button. Increases
	 * numSpawn label if appropriate
	 * 
	 * @param ae
	 */
	private void doPlus(ActionEvent ae) {
		int num = getNum() + 1;
		if (num != 1) {
			locationBox.setDisable(true);
			locationCheckBox.setSelected(false);
			rowColBox.setDisable(true);
			confirm.setDisable(false);
		} else {
			locationBox.setDisable(false);
			rowColBox.setDisable(false);
		}

		numSpawn.setText("" + (num));
		if (num == 99)
			plus.setDisable(true);
		if (num > 0)
			minus.setDisable(false);
	}

	@FXML
	/**
	 * Represents the action of the user clicking the minus button. Decreases
	 * numSpawn label if appropriate
	 * 
	 * @param ae
	 */
	private void doMinus(ActionEvent ae) {
		int num = getNum() - 1;
		if (num != 1) {
			locationBox.setDisable(true);
			locationCheckBox.setSelected(false);
			rowColBox.setDisable(true);
			confirm.setDisable(false);
		} else {
			locationBox.setDisable(false);
			rowColBox.setDisable(false);
		}

		numSpawn.setText("" + (num));
		if (num == 0)
			minus.setDisable(true);
		if (num < 99)
			plus.setDisable(false);
	}

	/**
	 * Returns the int stored in the numSpawn label
	 * 
	 * @return int > 0
	 */
	public int getNum() {
		return Integer.parseInt(numSpawn.getText());
	}

	/**
	 * @return whether locationCheckBox is selected and not disabled
	 */
	public boolean isLocation() {
		return !locationCheckBox.isDisable() && locationCheckBox.isSelected();
	}

	/**
	 * Sets the file label text to the given string
	 * 
	 * @param text text to be displayed in file label
	 */
	public void setFileLabel(String text) {
		critterFilename.setText(text);
	}

	/**
	 * Gets the value stored in the rowSpawn label
	 * 
	 * @return value of row
	 */
	public int getRow() {
		if (rowColBox.isDisable() || rowSpawn.getText().isBlank())
			return -1;
		return Integer.parseInt(rowSpawn.getText());
	}

	/**
	 * Gets the value stored in the rowSpawn label
	 * 
	 * @return value of row
	 */
	public int getCol() {
		if (rowColBox.isDisable() || colSpawn.getText().isBlank())
			return -1;
		return Integer.parseInt(colSpawn.getText());
	}

}
