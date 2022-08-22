package distributedView;

/**
 * Represents a MessageSet object for converting to and from JSON. Holds a
 * boolean status and a message string
 */
public class MessageSet {
	public boolean status;
	public String message;

	/**
	 * Constructs a MessageSet with given status and message
	 */
	public MessageSet(boolean status, String message) {
		this.status = status;
		this.message = message;
	}
}