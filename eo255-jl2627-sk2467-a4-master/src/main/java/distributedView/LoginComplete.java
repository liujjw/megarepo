package distributedView;

/** For transmitting a session_id back to the user */
public class LoginComplete {
	public int session_id;

	public LoginComplete(int id) {
		session_id = id;
	}
}