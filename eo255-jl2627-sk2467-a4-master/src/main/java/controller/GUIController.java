package controller;

import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedList;

import controller.ControllerImpl;
import model.Observable;
import model.Observer;
import model.ReadOnlyWorld;
import view.HexGridRender;

/**
 * Represents the GUI model in the simulation. Holds the list of all observers
 * and notifies them at appropriate times.
 */
public class GUIController extends ControllerImpl implements Observable<ReadOnlyWorld> {
	private Collection<Observer<ReadOnlyWorld>> observers = new LinkedList<Observer<ReadOnlyWorld>>();

	/**
	 * Constructs a GUIModel object
	 */
	public GUIController() {
		super();
	}

	/**
	 * Constructs a GUIModel object with the given printstream
	 * 
	 * @param p printstream to print error messages to
	 */
	public GUIController(PrintStream p) {
		super(p);
	}

	@Override
	public void registerObserver(Observer<ReadOnlyWorld> obs) {
		observers.add(obs);
	}

	@Override
	public void notifyAll(ReadOnlyWorld event) {
		for (Observer<ReadOnlyWorld> o : observers)
			o.notify(event, System.currentTimeMillis());
	}

	/**
	 * Notifies observers when time is advanced and passes in the new world.
	 */
	@Override
	public boolean advanceTime(int n) {
		if (super.advanceTime(n)) {
			notifyAll(this.getReadOnlyWorld());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean loadCritters(String filename, int n) {
		if (super.loadCritters(filename, n)) {
			notifyAll(this.getReadOnlyWorld());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean loadOneCritter(String filename, int row, int col) {
		if (super.loadOneCritter(filename, row, col)) {
			notifyAll(this.getReadOnlyWorld());
			return true;
		} else {
			return false;
		}

	}
}
