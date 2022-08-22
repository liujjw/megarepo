package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import distributedView.CritterWorldClient;
import distributedView.UserType;
import helpers.Helpers;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.ClientWorld;
import model.ReadOnlyCritter;
import model.WorldImpl;
import model.WorldImpl.Vector;
import view.HexGridRender;
import view.HexGridRender.Coordinate;

/**
 * A class controlling the main Critterworld UI scene. Handles all UI events and
 * updates, and calls functions on the world at the appropriate times.
 */
public class CritterworldUIController implements Initializable {
	/********** Menu bar items ***********/
	@FXML
	private MenuBar menubar;
	@FXML
	private Menu file;
	@FXML
	private Button play;
	@FXML
	private Button pause;
	@FXML
	private Button step;
	@FXML
	private Slider slider;
	@FXML
	private Button zoomIn;
	@FXML
	private Button zoomOut;
	/********* Menu items and general functionality ************/
	@FXML
	private Pane divider1;
	@FXML
	private Pane divider2;
	@FXML
	private Pane divider3;
	@FXML
	private MenuItem loadCritter;
	@FXML
	private MenuItem newWorld;
	@FXML
	private MenuItem newRandomWorld;
	@FXML
	private MenuItem quit;
	@FXML
	private MenuItem hideDetails;
	@FXML
	private VBox details;
	@FXML
	private ToolBar toolBar;
	@FXML
	private ProgressIndicator progress;
	/******** World details *********/
	@FXML
	private Label rowsLabel;
	@FXML
	private Label colsLabel;
	@FXML
	private Label numCrittersLabel;
	SimpleStringProperty nCritsProperty;
	@FXML
	private Label timeStepLabel;
	SimpleStringProperty timeStepProperty;
	/******** END world details ******/

	/********* Critter details *************/
	@FXML
	private Label speciesLabel;
	@FXML
	private Label memsizeLabel;
	@FXML
	private Label defenseLabel;
	@FXML
	private Label offenseLabel;
	@FXML
	private Label sizeLabel;
	@FXML
	private Label energyLabel;
	@FXML
	private Label passLabel;
	@FXML
	private Label postureLabel;
	@FXML
	private Label lastRuleLabel;
	@FXML
	private Label programLabel;
	@FXML
	private VBox critterDetailsBox;
	@FXML
	private VBox foodDetailsBox;
	@FXML
	private Label amountFoodLabel;

	@FXML
	private Canvas canvas;
	@FXML
	private ScrollPane scrollPane;

	@FXML
	private HBox levelBox;
	@FXML
	private Label levelText;
	@FXML
	private Label noWorldLoaded;
	/********* END FXML items ***********/

	private FileChooser worldChooser; // a FileChooser to choose world files
	private FileChooser critterChooser; // a FileChooser to choose critter files
	private HexGridRender render;
	private boolean initialized;
	private Map<Coordinate, WorldImpl.Vector> hexDistanceToCoord = new HashMap<>();
	ModelUpdate modelUpdate;
	Thread updateThread;
	private static final long MILLIS_PER_UPDATE_FOR_THIRTY_FPS = (long) (1000.0 / 30.0);
	private int IDtoFollow = -1;
	private UserType level;
	private CritterWorldClient client;
	private ClientWorld world;
	private boolean dragBeingDone = false; // Represents when the slider is being dragged
	private final Random rand = new Random();

	/**
	 * Create a new UI controller with a specified world.
	 * 
	 * @param c an instance of Controller that contains a world, if null use
	 *          Controller's default world
	 */
	public CritterworldUIController(GUIController c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Constructs a new UI controller with a specified user level
	 * 
	 * @param type   level of user - one of "admin", "read", or "write"
	 * @param client CritterWorldClient object with correct server
	 */
	public CritterworldUIController(UserType type, CritterWorldClient client) {
		initialized = false;
		this.level = type;
		this.client = client;
	}

	/**
	 * For use in separate thread to step model.
	 */
	@SuppressWarnings("rawtypes")
	class ModelUpdate extends Task {

		private boolean shouldEnd = false; // whether or not the getting thread should end
		private long timeSinceLastUpdate = 0;

		/**
		 * Creates a ModelUpdate thread in the given UI controller
		 * 
		 * 
		 */
		public ModelUpdate() {
			super();
		}

		/**
		 * Tells the thread to end by setting the local variable flag that keeps it
		 * running to false.
		 */
		public void shouldEnd() {
			shouldEnd = true;
		}

		@Override
		protected Task call() throws Exception {
			try {
				while (!shouldEnd) {
					ClientWorld newWorld = client.loadWorldState();
					double rate = newWorld.rate;
					Platform.runLater(() -> {
						if (level == UserType.READ)
							return;
						if (rate == 0) {
							play.setDisable(false);
							pause.setDisable(true);
							step.setDisable(false);
							if (!dragBeingDone)
								slider.setValue(0);

						} else {
							play.setDisable(true);
							step.setDisable(true);
							pause.setDisable(false);
							if (!dragBeingDone)
								slider.setValue(rate);
						}
					});

					boolean shouldRedraw = false;
					final ClientWorld oldWorld = world;
					if (oldWorld.width != newWorld.width || oldWorld.height != newWorld.height) {
						rowsColsChanged();
						shouldRedraw = true;
					}
					ReadOnlyCritter crit = null;
					if (IDtoFollow != -1) {
						crit = client.retrieveACritter(IDtoFollow);
					}
					ReadOnlyCritter finalcrit = crit;
					world = newWorld;
					Vector v = render.getSelected();
					boolean shouldUpdateFoodLabel = false;
					int amountFood = -1;
					if (v != null && finalcrit == null) {
						int i = world.getTerrainInfo(v.col, v.row);
						if (i < -1) {
							shouldUpdateFoodLabel = true;
							amountFood = -1 * i - 1;
						}

					}

					final boolean finalRedraw = shouldRedraw;

					final int finalAmountFood = amountFood;
					final boolean finalUpdateFood = shouldUpdateFoodLabel;
					Platform.runLater(() -> {
						numCrittersLabel.textProperty().setValue("" + world.getNumberOfAliveCritters());
						timeStepLabel.textProperty().setValue("" + world.getSteps());
//					if (dragBeingDone)
//						slider.setValue(rateToSet);
						if (finalcrit != null) {
							setCritterDetails(finalcrit);
						} else if (finalUpdateFood) {
							amountFoodLabel.setText("" + finalAmountFood);
						} else {
							setCritterDetails(null);
							foodDetailsBox.setVisible(false);
						}

					});
					long start = System.currentTimeMillis();
					Platform.runLater(() -> {
						if (start - timeSinceLastUpdate >= MILLIS_PER_UPDATE_FOR_THIRTY_FPS) {
							timeSinceLastUpdate = start;
							updateHexRender(finalRedraw);
						}
					});

				}
			} catch (Exception e) {
				System.out.println("Query thread task exception. Started new one.");
				modelUpdate.shouldEnd(); // completes current thread, ie kills it?
				modelUpdate = new ModelUpdate();
				updateThread = new Thread(modelUpdate);
				updateThread.start();
			}

			return null;
		}
	}

	/**
	 * Sets the world to run the simulation on to info and hides progress indicator.
	 * 
	 * @param info ClientWorld with all necessary world information, can be null to
	 *             indicate no world
	 */
	public void setWorld(ClientWorld info) {
		progress.setVisible(false);
		world = info;
		render = new HexGridRender(world, canvas);
		if (info == null) {
			noWorldLoaded.setVisible(true);
		} else {
			rowsColsChanged();
			if (!updateThread.isAlive()) {
				try {
					updateThread.start();
				} catch (Exception e) {
					System.out.println(e.getCause());
				}
			}
		}
	}

	/**
	 * Should be called when either of the rows and column lengths have changed,
	 * meaning appropriate fields need to be updated.
	 */
	private void rowsColsChanged() {
		Platform.runLater(() -> {
			colsLabel.textProperty().setValue("" + world.numCols());
			rowsLabel.textProperty().setValue("" + world.numRows());
			render = new HexGridRender(world, canvas);
		});
	}

	/**
	 * Sets the style of each of the 3 dividers to the specified style
	 * 
	 * @param style style string to be set
	 */
	private void setDividers(String style) {
		divider1.setStyle(style);
		divider2.setStyle(style);
		divider3.setStyle(style);
	}

	/**
	 * Checks what level has been instantiated and styles appropriate fields
	 */
	public void checkLevel() {
		if (level == null)
			return;
		switch (level) {
		case ADMIN:
			levelBox.setStyle("-fx-background-color: #ffcccc;");
			setDividers("-fx-background-color: #ffcccc;");
			levelText.setText("Admin");
			break;
		case WRITE:
			newWorld.setDisable(true);
			newRandomWorld.setDisable(true);
			levelBox.setStyle("-fx-background-color: #ccffcc;");
			setDividers("-fx-background-color: #ccffcc;");
			levelText.setText("Write");
			break;
		case READ:
			loadCritter.setDisable(true);
			newWorld.setDisable(true);
			newRandomWorld.setDisable(true);
			slider.setDisable(true);
			play.setDisable(true);
			pause.setDisable(true);
			step.setDisable(true);
			levelBox.setStyle("-fx-background-color: #ccebff;");
			setDividers("-fx-background-color: #ccebff;");
			levelText.setText("Read");
			break;
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (initialized)
			return;
		initialized = true;

		noWorldLoaded.setVisible(false);
		progress.setVisible(true);
		checkLevel();
		critterDetailsBox.setVisible(false);
		foodDetailsBox.setVisible(false);
		pause.setDisable(true);
		slider.setValue(0);
		worldChooser = new FileChooser();
		worldChooser.setTitle("Choose World File");
		worldChooser.getExtensionFilters().addAll(new ExtensionFilter("World Files", "*.txt"));
		critterChooser = new FileChooser();
		critterChooser.setTitle("Choose Critter File");
		critterChooser.getExtensionFilters().addAll(new ExtensionFilter("Critter Files", "*.txt"));
		modelUpdate = new ModelUpdate();
		updateThread = new Thread(modelUpdate);

		slider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean wasChanging,
					Boolean changing) {
				if (changing) {
					dragBeingDone = true;
				} else {
					new Thread() {
						@Override
						public void run() {
							client.runContinuous(slider.getValue());
							dragBeingDone = false;
						}
					}.start();
				}
			}
		});

//		slider.valueProperty().addListener(new ChangeListener<Number>() {
//			@Override
//			public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
//				dragBeingDone = true;
//			}
//		});

		canvas.setOnMouseClicked(mouseEvent -> {
			getMouseClickOnCanvas(mouseEvent);
		});
	}

	/**
	 * Handles displaying critter or food information when a user clicks on a
	 * hextile that contains a critter or food.
	 */
	private void getMouseClickOnCanvas(MouseEvent m) {
		try {
			if (render == null)
				return;
			if (m.getSource() != canvas)
				return;
			double x = m.getX();
			double y = m.getY();
			if (!render.inBounds(x, y))
				return;
			// find the closest hex to this mouse click
			double closest = Double.MAX_VALUE;
			WorldImpl.Vector hexCoord = null;
			for (Map.Entry<Coordinate, WorldImpl.Vector> e : hexDistanceToCoord.entrySet()) {
				Coordinate coord = e.getKey();
				double d = Math.sqrt(Math.pow(coord.x - x, 2) + Math.pow(coord.y - y, 2));
				if (d < closest) {
					closest = d;
					hexCoord = e.getValue();
				}
			}
			// see if the hex contains a critter
			if (hexCoord != null) {
				ReadOnlyCritter r = world.getReadOnlyCritter(hexCoord.col, hexCoord.row);
				if (r != null) {
					final Vector v = new Vector(hexCoord.col, hexCoord.row);
					Platform.runLater(() -> {
						IDtoFollow = r.getID();
						foodDetailsBox.setVisible(false);
						setCritterDetails(r);
						render.setSelectedHexCritter(v, r);
					});
					return;
				} else {
					int i = world.getTerrainInfo(hexCoord.col, hexCoord.row);
					if (i < -1) {
						final int food = -1 * i - 1;
						final Vector v = new Vector(hexCoord.col, hexCoord.row);
						Platform.runLater(() -> {
							IDtoFollow = -1;
							foodDetailsBox.setVisible(true);
							amountFoodLabel.setText("" + food);
							setCritterDetails(null);
							render.setSelectedHexFood(v, food);
						});

						return;
					}
				}
			}
			render.setNothingSelected(hexCoord);
			IDtoFollow = -1;
			foodDetailsBox.setVisible(false);
			setCritterDetails(null);
			return;
		} catch (Exception e) {
			System.out.println(e.getCause());
		} finally {
		}

	}

	/**
	 * Locks model in current thread until display is updated. WARNING: Should be
	 * called from application thread.
	 * 
	 * ] * @param redrawHexAndRocks whether or not the hexes and rocks should be
	 * redrawn (inefficient)
	 */
	private synchronized void updateHexRender(boolean redrawHexAndRocks) {
		// situation: passtime occurs, critters get to take a turn each, then once done
		// notify the controller to redraw
		// pass in the world, gets locked from future passtime until redraw is complete
		try {
			numCrittersLabel.textProperty().setValue("" + world.getNumberOfAliveCritters());
			timeStepLabel.textProperty().setValue("" + world.getSteps());
			if (level == UserType.READ)
				slider.setValue(world.rate);
			Bounds bounds = scrollPane.getViewportBounds();
			double lowestXPixelShown = -1 * (double) bounds.getMinX() + 1;
			double highestXPixelShown = -1 * (double) bounds.getMinX() + (double) bounds.getMaxX();
			double lowestYPixelShown = -1 * (double) bounds.getMinY() + 1;
			double highestYPixelShown = -1 * (double) bounds.getMinY() + (double) bounds.getMaxY();
			Map<Coordinate, Vector> m = render.draw(world, redrawHexAndRocks,
					new Coordinate(lowestXPixelShown, lowestYPixelShown),
					new Coordinate(highestXPixelShown, highestYPixelShown));
			if (m != null)
				hexDistanceToCoord = m;
		} catch (Exception e) {
			System.out.println(e.getCause());
		} finally {
		}

	}

	/**
	 * Sets all the labels in the critter details box to contain the appropriate
	 * information
	 * 
	 * @param critter ReadOnlyCritter to be read from. Can be null to indicate that
	 *                the critterDetailsBox should be hidden
	 */
	public void setCritterDetails(ReadOnlyCritter critter) {
		if (critter == null) {
			critterDetailsBox.setVisible(false);
			return;
		}
		render.setSelectedHexCritter(world.getCritterLocation(critter.getID()), critter);
		critterDetailsBox.setVisible(true);
		int[] mem = critter.getMemory();
		speciesLabel.setText(critter.getSpecies());
		memsizeLabel.setText("" + mem[0]);
		defenseLabel.setText("" + mem[1]);
		offenseLabel.setText("" + mem[2]);
		sizeLabel.setText("" + mem[3]);
		energyLabel.setText("" + mem[4]);
		passLabel.setText("" + mem[5]);
		postureLabel.setText("" + mem[6]);
		String text = critter.getLastRuleString();
		String program = critter.getProgramString();
		if (program == null) {
			program = "Unauthorized";
			text = "Unauthorized";
		}
		if (text == null || text.isBlank())
			text = "[no rule executed]";
		lastRuleLabel.setText(text);
		programLabel.setText(program);
	}

	/**
	 * Represents the zoom in button click. Zooms in the display.
	 */
	@FXML
	private void zoomIn() {
		boolean b = render.zoomIn();

		zoomOut.setDisable(false);
		if (!b) {
			zoomIn.setDisable(true);
		}
	}

	/**
	 * Represents the zoom out button click. Zooms out the display.
	 */
	@FXML
	private void zoomOut() {
		boolean b = render.zoomOut();
		zoomIn.setDisable(false);
		if (!b) {
			zoomOut.setDisable(true);
		}
	}

	@FXML
	/**
	 * Represents Menu > View > hide/show details MenuItem click. If showing, hides
	 * details tab. If hidden, shows details tab.
	 * 
	 * @param ae
	 */
	private void hideDetails(ActionEvent ae) {
		if (hideDetails.getText().substring(0, 4).equals("Hide")) {
			details.setMinWidth(0);
			details.setMaxWidth(0);
			details.setVisible(false);
			divider2.setVisible(false);
			divider2.setPrefWidth(0);
			hideDetails.setText("Show details");
		} else {
			details.setMinWidth(150);
			details.setMaxWidth(250);
			details.setVisible(true);
			divider2.setVisible(true);
			divider2.setPrefWidth(5);
			hideDetails.setText("Hide details");
		}
	}

	/**
	 * Represents the action of the play button being pressed. Disables play and
	 * step buttons while enabling pause.
	 */
	@FXML
	private void playPressed(ActionEvent ae) {
		int response = client.runContinuous(slider.getValue());
		if (response == 200) {
			if (slider.getValue() == 0) {
				play.setDisable(false);
				pause.setDisable(true);
				step.setDisable(false);
			} else {
				play.setDisable(true);
				pause.setDisable(false);
				step.setDisable(true);
			}
		}
	}

	/**
	 * Represents the action of the pause button being pressed. Disables pause
	 * button while enabling play and step buttons.
	 * 
	 */
	@FXML
	private void pausePressed(ActionEvent ae) {
		int response = client.runContinuous(0);
		if (response == 200) {
			pause.setDisable(true);
			play.setDisable(false);
			step.setDisable(false);
		}
	}

	/**
	 * Represents step button pressed. Proceeds one step with the simulation. Can
	 * only be called when simulation is paused.
	 * 
	 */
	@FXML
	private void stepPressed(ActionEvent ae) {
		int a = client.advanceWorld(1);
		if (a == 200) {
			play.setDisable(true);
			step.setDisable(true);
			play.setDisable(false);
			step.setDisable(false);
		}
	}

	/**
	 * Returns a node in the given scene. For use when menu items are clicked (which
	 * are not part of the scene)
	 * 
	 * @return Node
	 */
	private Node getNode() {
		return play;
	}

	@FXML
	/**
	 * Represents Menu > View > Load critter button press. Opens up window for
	 * number of critters to spawn. If confirmed, displays a confirmation window
	 * with appropriate information.
	 * 
	 * @param ae
	 */
	private void loadCritter(ActionEvent ae) {
		drawNums(false);
		pausePressed(ae);
		Window theStageWindow = getNode().getScene().getWindow();

		File selectedFile = critterChooser.showOpenDialog(theStageWindow);
		if (selectedFile == null)
			return;

		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(theStageWindow);
		Scene scene = loadCritterFXML("LoadCritter.fxml", dialog, selectedFile);
		if (scene == null)
			return;
		dialog.setResizable(false);
		dialog.setTitle("Load critters");
		drawNums(true);
	}

	private static void drawNums(boolean clear) {
		// draw numbers on the hex grid and clear them later
	}

	/**
	 * Loads a scene from the fxml file at the given location
	 * 
	 * @param loc    name of fxml file relative to resources directory
	 * @param dialog stage to place on
	 * @param file   file of critter
	 * @return scene from FXML, or null if error
	 */
	private Scene loadCritterFXML(String loc, Stage dialog, File file) {
		try {
			final URL r = ClassLoader.getSystemResource(loc);
			if (r == null) {
				System.err.println("No FXML resource found.");
				return null;
			}
			FXMLLoader loader = new FXMLLoader(r);
			LoadCritterController controller = new LoadCritterController();
			loader.setController(controller);
			final Parent node = loader.load();
			final Scene scene = new Scene(node);
			scene.getStylesheets().add(ClassLoader.getSystemResource("styles.css").toExternalForm());

			dialog.setScene(scene);
			dialog.sizeToScene();
			dialog.show();
			controller.setFileLabel(file.getAbsolutePath());
			controller.getCancel().setOnAction(e -> dialog.close());
			controller.getConfirm().setOnAction(e -> {
				if (controller.isLocation()) {
					spawnCritterOnServer(file, controller.getNum(), controller.getRow(), controller.getCol());
				} else {
					spawnCritterOnServer(file, controller.getNum(), -1, -1);
				}
				dialog.close();
			});
			return scene;
		} catch (final IOException ioe) {
			System.err.println("Can't load FXML file.");
			ioe.printStackTrace();
			return null;
		}
	}

	/**
	 * Spawns a critter with given information to the server, pulling up error
	 * message if appropriate
	 * 
	 * @param file file of critter
	 * @param num  number of critters to spawn
	 * @param row  row of single critter to spawn if num = 1 (-1 indicates random)
	 * @param col  col of single critter to spawn if col = 1 (-1 indicates random)
	 */
	private void spawnCritterOnServer(File file, int num, int row, int col) {
		new Thread() {
			@Override
			public void run() {
				final String error = Helpers.spawnCritterOnServer(client, file, num, row, col);
				if (error == null)
					return;
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.ERROR, error, ButtonType.OK);
					alert.showAndWait();
				});
				return;
			}

		}.start();

	}

	@FXML
	/**
	 * Starts a new world by opening a file chooser after a confirmation message.
	 * 
	 * @param ae
	 */
	private void newWorld(ActionEvent ae) {
		Alert alert = new Alert(AlertType.WARNING,
				"Are you sure you want to start a new world? All progress will be lost.", ButtonType.YES,
				ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() != ButtonType.YES) {
			return;
		}
		File file = worldChooser.showOpenDialog(alert.getOwner());
		if (file == null)
			return;
		tryLoadWorld(file);

	}

	/**
	 * Tries to load a world with description in world file, shows alert with proper
	 * error messages if unsuccesful.
	 * 
	 * @param description
	 */
	private void tryLoadWorld(File file) {
		String text = "";
		try {
			int i = -1;
			if (file == null) {

				i = client.newWorld(Helpers.getRandomWorld());
			} else {
				String desc = Helpers.fileToString(file);
				if (desc == null) {
					throw new IOException();
				}
				i = client.newWorld(desc);
			}
			if (i / 100 == 2) {
				return;
			}
			text = "Error " + i + ": ";
			switch (i) {
			case 404:
				text += "Server could not find resource.";
				break;
			case 401:
				text += "You do not have access to perform this operation.";
				break;
			default:
				text += "Server could not complete your request.";
			}
		} catch (IOException e) {
			text = "Could not load specified world, using random world instead.";
		}
		Alert alert2 = new Alert(AlertType.ERROR, text, ButtonType.OK);
		alert2.showAndWait();
	}

	@FXML
	/**
	 * Starts a new world by opening a file chooser after a confirmation message.
	 * 
	 * @param ae
	 */
	private void newRandomWorld(ActionEvent ae) {
		Alert alert = new Alert(AlertType.WARNING,
				"Are you sure you want to start a new world? All progress will be lost.", ButtonType.YES,
				ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() != ButtonType.YES) {
			return;
		}
		tryLoadWorld(null);
	}

	@FXML
	/**
	 * Quits the program using Platform.exit() and closes all threads after showing
	 * a warning window.
	 * 
	 * @param ae
	 */
	private void quitProgram(ActionEvent ae) {
		// pausePressed(ae);
		Alert alert = new Alert(AlertType.WARNING, "Are you sure you want to quit? All progress will be lost.",
				ButtonType.YES, ButtonType.NO);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {
			Platform.exit();
			if (modelUpdate != null)
				modelUpdate.shouldEnd();
			System.out.println("***** User quit program. *****");
		}
	}

	/**
	 * Quits the program and all threads.
	 */
	public void doQuit() {
		quitProgram(new ActionEvent());
	}

}
