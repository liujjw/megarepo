package model;

import java.util.Collection;
import java.util.LinkedList;

/**From CS2112 Lab*/
/**A class for objects that will notify its observers.*/
public interface Observable<T> {

	/**
	 * Add an observer to the set of observers this will notify.
	 * */
	void registerObserver(Observer<T> obs);

	/**
	 * Notify all known observers of some event.
	 * */
	void notifyAll(T event);

}
