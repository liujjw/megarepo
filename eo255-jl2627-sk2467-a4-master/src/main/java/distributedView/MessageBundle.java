package distributedView;

/** Represents a {"message": message, "param": param} JSON object */
public class MessageBundle {
	String message;
	int param;

	public MessageBundle(String m, int i) {
		message = m;
		param = i;
	}
}