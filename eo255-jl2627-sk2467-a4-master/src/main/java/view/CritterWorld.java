package view;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import controller.ConfirmationController;
import controller.CritterworldUIController;
import controller.LoadScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import controller.GUIController;
import model.LabelOutputStream;

/**
 * Represents the application itself. Holds loading world functionality, but the
 * bulk of the world simulation logic is handled in CritterworldUIController.
 */
public abstract class CritterWorld extends Application {

	private GUIController model; // Represents the GUIModel of this simulation after user selection

	/**
	 * Launches the application.
	 */
	public static void main(final String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(final Stage stage) {
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
			scene.getStylesheets().add(ClassLoader.getSystemResource("styles.css").toExternalForm());
//			stage.setTitle("Critterworld");

			stage.setScene(scene);
			stage.setResizable(false);
			stage.sizeToScene();
			stage.show();
			controller.getLoadWorld().setOnMouseClicked(e -> {
				FileChooser worldChooser = new FileChooser();
				worldChooser.setTitle("Choose World File");
				worldChooser.getExtensionFilters().addAll(new ExtensionFilter("World Files", "*.txt"));
				File selectedFile = worldChooser.showOpenDialog(stage);
				if (selectedFile == null)
					return;
				loadWorld(stage, selectedFile);
				Scene s = getMainScene();
				if (s == null)
					return;
			});
			controller.getNewWorld().setOnMouseClicked(e -> {
				Scene s = getMainScene();
				if (s == null)
					return;
				goToMainScreen(stage, s);

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
			System.out.println("There was an error performing start() actions.");
		}

	}

	/***
	 * Triggers an animation for going from the current stage to the main screen.
	 * 
	 * @param stage stage to change size of
	 * @param scene final scene to be set
	 */
	protected void goToMainScreen(final Stage stage, Scene scene) {
		getBiggerAnimation(stage, 900, 600);
		stage.setScene(scene);

		stage.setResizable(true);
		stage.sizeToScene();
	}

	/**
	 * Opens a confirmation window on the specified stage after trying to load the
	 * specified world file.
	 * 
	 * @param stage current stage of application, onto which the confirmation window
	 *              will be opened
	 * @param file  user specified file
	 */
	protected void loadWorld(final Stage stage, File file) {
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
			PrintStream p = new PrintStream(new LabelOutputStream(controller.getErrorDescriptionLabel()));

			model = new GUIController(p);
			boolean b = model.loadWorld(file.getAbsolutePath()); // TODO: check this line. absolute path may
																	// not work
			final Stage dialog = new Stage();
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.initOwner(stage.getScene().getWindow());
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.sizeToScene();
			dialog.setTitle("Load world " + file.getName());
			dialog.show();

			controller.setError(!b);

			controller.getButton().setOnAction(e -> {
				dialog.close();
				if (controller.isError())
					return;
				Scene main = getMainScene();
				if (main == null)
					return;
				goToMainScreen(stage, main);

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
	}

	/**
	 * Represents an animation whereby the stage will slowly increase in size
	 * symmetrically until the finalWidth and finalHeight are achieved.
	 * 
	 * @param stage       stage to change size of
	 * @param finalWidth  desired final width of stage
	 * @param finalHeight desired final height of stage
	 */
	protected void getBiggerAnimation(final Stage stage, int finalWidth, int finalHeight) {
		double wI = stage.getWidth();
		double wF = finalWidth;
		double hI = stage.getHeight();
		double hF = finalHeight;
		int numSteps = 100; // number of steps to take
		double wStep = (wF - wI) / numSteps;
		double hStep = (hF - hI) / numSteps;
		Timer animTimer = new Timer();
		animTimer.scheduleAtFixedRate(new TimerTask() {
			int i = 0;

			@Override
			public void run() {
				if (i < numSteps) {
					stage.setX(stage.getX() - wStep / 2);
					stage.setY(stage.getY() - hStep / 2);
					stage.setWidth(stage.getWidth() + wStep);
					stage.setHeight(stage.getHeight() + hStep);
				} else {
					this.cancel();
				}
				i++;
			}
		}, 0, 20);

	}

	/**
	 * Initializes the main scene, aka CritterworldUI aka simulator, and sets the
	 * appropriate controller. Returns the newly created scene object.
	 * 
	 * @return newly created scene object
	 */
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
			CritterworldUIController controller = new CritterworldUIController(model);
			loader.setController(controller);
			final Parent node = loader.load();
			final Scene scene = new Scene(node);
			scene.getStylesheets().add(ClassLoader.getSystemResource("styles.css").toExternalForm());

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
			System.out.println("There was an error performing start() actions.");
			return null;
		}
	}

}
