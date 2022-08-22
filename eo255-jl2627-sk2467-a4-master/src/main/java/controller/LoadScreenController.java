package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

/**
 * A controller object for the loading screen (choosing to upload a world or
 * start a new one) Performs no logic itself, used only to get button references
 * so that the main application can determine functionality.
 */
public class LoadScreenController implements Initializable {

	@FXML
	private VBox loadWorld;
	@FXML
	private VBox newWorld;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	/**
	 * Gets the loadWorld VBox so that action eventhandlers can be added from the
	 * application thread.
	 * 
	 * @return loadWorld VBox reference
	 */
	public VBox getLoadWorld() {
		return loadWorld;
	}

	/**
	 * Gets the newWorld VBox so that action eventhandlers can be added from the
	 * application thread.
	 * 
	 * @return newWorld VBox reference
	 */
	public VBox getNewWorld() {
		return newWorld;
	}

}
