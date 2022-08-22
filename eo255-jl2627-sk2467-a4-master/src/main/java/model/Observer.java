package model;

/**From CS2112 lab.*/
/** An observer is notified whenever events occur. */
public interface Observer<T> {
	/**
	 * Call to tell this of an event.
	 *
	 * @param event the new event
	 * @param callingTime time the method is called
	 * */
	void notify(T event, long callingTime);
}
