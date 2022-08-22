package controller;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/** A controller for the LoginScreen scene */
public class LoginScreenController implements Initializable {
	@FXML
	private VBox admin;
	@FXML
	private VBox write;
	@FXML
	private VBox read;
	@FXML
	private PasswordField password;
	@FXML
	private Button login;
	@FXML
	private TextField server;
	@FXML
	private ProgressIndicator progress;
	@FXML
	private Label invalidPassword;
	@FXML
	private Label invalidServer;

	private String clickedStyle = "	-fx-scale-y: 1.15;\n" + "	-fx-scale-x: 1.15;\n"
			+ "	-fx-background-color: #DFDDDF;";
	private String unclickedStyle = "";

	private String errorColor = "#FFCCCC88";
	private String passwordDefaultStyle = "-fx-background-color: transparent; -fx-border-width: 0 0 2 0; -fx-border-color:  #59617F#59617F;";
	private String serverDefaultStyle = "-fx-background-color:  #59617F0A;";
	public Map<String, List<String>> badPasswords = new HashMap<String, List<String>>(); // represents all bad
																							// passwords to their
																							// corresponding levels.
	public List<String> badServers = new LinkedList<String>(); // represents all bad servers

	private String level = "";

	/**
	 * Constructs a LoginScreenController object.
	 */
	public LoginScreenController() {
		resetBadPasswords();

	}

	/**
	 * Resets the BadPasswords map. Should be called when server text changes
	 */
	private void resetBadPasswords() {
		badPasswords.put("admin", new LinkedList<String>());
		badPasswords.put("write", new LinkedList<String>());
		badPasswords.put("read", new LinkedList<String>());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		login.setDisable(true);
		password.setDisable(true);
		password.setText("");
		progress.setVisible(false);
		invalidPassword.setVisible(false);
		invalidServer.setVisible(false);
		resetBadPasswords();
		// add a listener to enable/disable login button when password not empty
		password.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				checkForErrors();
			}
		});
		server.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				resetBadPasswords();
				checkForErrors();
			}
		});

	}

	/**
	 * Checks for errors and updates appropriate fields. Should be called after
	 * updating invalidPasswords and invalidServers lists
	 */
	public void checkForErrors() {
		if (isValid()) {
			login.setDisable(false);
			server.setStyle(serverDefaultStyle);
			invalidServer.setVisible(false);
			password.setStyle(passwordDefaultStyle);
			invalidPassword.setVisible(false);
		} else {
			login.setDisable(true);
			if (badServers.contains(server.getText())) {
				setInvalid(server);
				invalidServer.setVisible(true);
			} else {
				server.setStyle(serverDefaultStyle);
				invalidServer.setVisible(false);
			}
			if (badPasswords.containsKey(level) && badPasswords.get(level).contains(password.getText())) {
				setInvalid(password);
				invalidPassword.setVisible(true);
			} else {
				password.setStyle(passwordDefaultStyle);
				invalidPassword.setVisible(false);
			}
		}
	}

	/**
	 * Sets the style of the given control to error style
	 * 
	 * @param control given control to set style of, either server or password
	 *                fields
	 */
	private void setInvalid(Control control) {
		control.setStyle("-fx-background-color: " + errorColor + ";");
	}

	/**
	 * @return whether all fields are in their valid states
	 */
	private boolean isValid() {
		return server.getText().length() > 0 && !level.equals("") && password.getText().length() > 0
				&& !badServers.contains(server.getText()) && badPasswords.containsKey(level)
				&& !badPasswords.get(level).contains(password.getText());
	}

	/**
	 * When admin VBox clicked
	 */
	@FXML
	public void adminClicked(MouseEvent event) {
		level = "admin";
		password.setDisable(false);
		admin.setStyle(clickedStyle);
		write.setStyle(unclickedStyle);
		read.setStyle(unclickedStyle);
	}

	/**
	 * When write VBox clicked
	 */
	@FXML
	public void writeClicked(MouseEvent event) {
		level = "write";
		password.setDisable(false);
		admin.setStyle(unclickedStyle);
		write.setStyle(clickedStyle);
		read.setStyle(unclickedStyle);
	}

	/**
	 * When read VBox clicked
	 */
	@FXML
	public void readClicked(MouseEvent event) {
		level = "read";
		password.setDisable(false);
		admin.setStyle(unclickedStyle);
		write.setStyle(unclickedStyle);
		read.setStyle(clickedStyle);
	}

	/**
	 * @return a reference to the login button
	 */
	public Button getLogin() {
		return login;
	}

	/**
	 * @return a reference to the progress indicator
	 */
	public ProgressIndicator getProgressIndicator() {
		return progress;
	}

	/**
	 * @return password text
	 */
	public String getPassword() {
		return password.getText();
	}

	/**
	 * @return level - one of "admin", "read", or "write"
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @return server url. adds slash at end if it wasn't there before
	 */
	public String getURL() {
		String s = server.getText();
		if (s.length() > 0 && s.charAt(s.length() - 1) != '/')
			s += "/";
		return s;
	}

}
