package view;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import controller.ConfirmationController;
import controller.CritterworldUIController;
import controller.LoadScreenController;
import controller.LoginScreenController;
import distributedView.CritterWorldClient;
import distributedView.UserType;
import helpers.Helpers;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.ClientWorld;

/**
 * The class that launches GUI. Calling the main method launches GUI.
 */
public class DistributedCritterWorld extends CritterWorld {

	private UserType level; // level
	private CritterWorldClient client; // client to use
	private CritterworldUIController mainController; // main critterworldUI controller

	/** Launches application */
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(final Stage stage) {
		try {
			final URL r = ClassLoader.getSystemResource("LoginScreen.fxml");
			if (r == null) {
				System.err.println("No FXML resource found.");
				try {
					stop();
				} catch (final Exception e) {
				}
			}

			FXMLLoader loader = new FXMLLoader(r);
			LoginScreenController controller = new LoginScreenController();
			loader.setController(controller);
			final Parent node = loader.load();
			final Scene scene = new Scene(node);
			scene.getStylesheets().add(ClassLoader.getSystemResource("styles.css").toExternalForm());

			stage.setScene(scene);
			stage.setResizable(false);
			stage.sizeToScene();
			stage.show();
			controller.getLogin().setOnMouseClicked(e -> {
				controller.getLogin().setVisible(false);
				controller.getProgressIndicator().setVisible(true);
				client = new CritterWorldClient(controller.getURL());
				int status = client.login(controller.getLevel(), controller.getPassword());
				controller.getLogin().setVisible(true);
				controller.getProgressIndicator().setVisible(false);
				if (status == 200) {
					switch (controller.getLevel()) {
					case "admin":
						level = UserType.ADMIN;
						break;
					case "write":
						level = UserType.WRITE;
						break;
					case "read":
						level = UserType.READ;
						break;
					default:
						System.err.println("Incorrect level returned from server");
						return;
					}
					Scene s = getMainScene();
					if (s == null)
						return;
					goToMainScreen(stage, s);
					stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
						@Override
						public void handle(WindowEvent event) {
							mainController.doQuit();
						}
					});

				} else if (status == 404) {
					controller.badServers.add(controller.getURL());
				} else if (status == 401) {
					controller.badPasswords.get(controller.getLevel()).add(controller.getPassword());
				} else {
					System.err.println("Login error, please try again.");
				}
				controller.checkForErrors();
			});

		} catch (

		final IOException ioe) {
			System.err.println("Can't load FXML file.");
			ioe.printStackTrace();
			try {
				stop();
			} catch (final Exception e) {
			}
		} catch (NullPointerException e) {
			System.err.println("There was an error performing start() actions.");
		}

	}

	@Override
	protected Scene getMainScene() {
		try {
			final URL r = ClassLoader.getSystemResource("CritterworldUI.fxml");
			if (r == null) {
				System.err.println("No FXML resource found.");
				try {
					stop();
				} catch (final Exception e) {
				}
			}
			FXMLLoader loader = new FXMLLoader(r);
			CritterworldUIController controller = new CritterworldUIController(level, client);
			mainController = controller;
			loader.setController(controller);
			final Parent node = loader.load();
			final Scene scene = new Scene(node);
			scene.getStylesheets().add(ClassLoader.getSystemResource("styles.css").toExternalForm());
			new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
					}
					ClientWorld info = client.loadWorldState();
					if (info == null && level == UserType.ADMIN) {
						chooseWorld(scene);
					} else {
						controller.setWorld(info);
					}
				}
			}.start();
			controller.checkLevel();
			return scene;

		} catch (

		final IOException ioe) {
			System.err.println("Can't load FXML file.");
			ioe.printStackTrace();
			try {
				stop();
			} catch (final Exception e) {
			}
			return null;
		} catch (NullPointerException e) {
			System.err.println("There was an error performing start() actions.");
			return null;
		}
	}

	/**
	 * Brings up the choose world screen on top of parent scene
	 * 
	 * @param parent parent scene to display chooser on top of
	 */
	private void chooseWorld(Scene parent) {
		Platform.runLater(() -> {
			try {
				final URL r = ClassLoader.getSystemResource("LoadingScreen.fxml");
				if (r == null) {
					System.err.println("No FXML resource found.");
					try {
						stop();
					} catch (final Exception e) {
					}
				}
				FXMLLoader loader = new FXMLLoader(r);
				LoadScreenController controller = new LoadScreenController();
				loader.setController(controller);
				final Parent node = loader.load();
				final Scene scene = new Scene(node);

				final Stage dialog = new Stage();
				dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent event) {
						event.consume();
					}
				});
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(parent.getWindow());
				dialog.setScene(scene);
				dialog.setResizable(false);
				dialog.sizeToScene();
				dialog.show();

				controller.getLoadWorld().setOnMouseClicked(e -> {
					FileChooser worldChooser = new FileChooser();
					worldChooser.setTitle("Choose World File");
					worldChooser.getExtensionFilters().addAll(new ExtensionFilter("World Files", "*.txt"));
					File selectedFile = worldChooser.showOpenDialog(dialog);
					if (selectedFile == null)
						return;
					boolean b = loadWorldServer(dialog, selectedFile);
					if (b) {
						dialog.close();
						mainController.setWorld(client.loadWorldState());
					} else {
						return;
					}

				});
				controller.getNewWorld().setOnMouseClicked(e -> {
					boolean b = loadWorldServer(dialog, null);
					if (b) {
						dialog.close();
						ClientWorld w = client.loadWorldState();
						assert w != null : "Error null new world";
						mainController.setWorld(w);
					} else {
						return;
					}

				});

			} catch (

			final IOException ioe) {
				System.err.println("Can't load FXML file.");
				ioe.printStackTrace();
				try {
					stop();
				} catch (final Exception e) {
				}
				return;
			} catch (NullPointerException e) {
				System.out.println("There was an error performing start() actions.");
				return;
			}
		});
	}

	/**
	 * Loads the specified world file onto the server
	 * 
	 * @param stage        stage to initialize confirmation window onto
	 * @param selectedFile file chosen by user. Can be null to indicate load new
	 *                     world
	 * @return whether or not world was successfully loaded.
	 */
	protected boolean loadWorldServer(final Stage stage, File selectedFile) {
		String worldDescription = Helpers.getRandomWorld();
		if (selectedFile != null) {
			String d = Helpers.fileToString(selectedFile);
			if (d != null) {
				System.out.println("Selected file could not be read, using default world instead.");
				worldDescription = d;
			}
		}
		String description = "";
		int status = client.newWorld(worldDescription);
		switch (status) {
		case 201:
		case 200:
			break;
		case 406:
			description = "Error " + status + ": Unable to read selected world, please try again.";
			break;
		case 401:
			description = "Error " + status + ": Unauthorized.";
			break;
		default:
			description = "Error " + status + ": There was an error trying to load world, please try again.";
			break;
		}
		if (selectedFile == null) {
			if (status == 201 || status == 202) {
				return true;
			} else {
				System.out.println(description);
				return false;
			}
		}
		try {

			final URL r = ClassLoader.getSystemResource("Confirmation.fxml");
			if (r == null) {
				System.err.println("No FXML resource found.");
				try {
					stop();
				} catch (final Exception e) {
				}
			}

			FXMLLoader loader = new FXMLLoader(r);
			ConfirmationController controller = new ConfirmationController();
			loader.setController(controller);
			final Parent node = loader.load();
			final Scene scene = new Scene(node);
			final Stage dialog = new Stage();
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.initOwner(stage.getScene().getWindow());
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.sizeToScene();
			dialog.setTitle("Load world " + selectedFile.getName());
			dialog.show();

			controller.setError(!description.isEmpty());
			controller.getErrorDescriptionLabel().setText(description);

			controller.getButton().setOnAction(e -> {
				dialog.close();
			});
			return !controller.isError();

		} catch (

		final IOException ioe) {
			System.err.println("Can't load FXML file.");
			ioe.printStackTrace();
			try {
				stop();
			} catch (final Exception e) {
			}
			return false;
		} catch (NullPointerException e) {
			System.out.println("There was an error performing start() actions.");
			return false;
		}

	}
}
