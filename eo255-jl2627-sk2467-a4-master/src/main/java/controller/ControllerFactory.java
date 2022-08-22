package controller;

import java.io.PrintStream;

/**
 * DO NOT REMOVE ANY METHODS IN THIS CLASS. Course staff needs this to test your
 * world.
 */
public final class ControllerFactory {

	/**
	 * Private constructor can prevent any {@code new ControllerFactory()} since
	 * they do not make sense.
	 */
	private ControllerFactory() {
	}

	/**
	 * @return a controller for console to allow course staff to test your code. The
	 *         returned controller does not contain any already initialized world.
	 *         NOTE (added by Eric): Prints errors to console by default
	 */
	public static Controller getConsoleController() {
		return new ControllerImpl();
	}

	/**
	 * Gets a console Controller that prints errors to the given printstream
	 * 
	 * @param p printStream to be printed to
	 * @return Controller
	 */
	public static Controller getConsoleController(PrintStream p) {
		return new ControllerImpl(p);
	}
}
