package distributedView;

/** Represents a login JSON object */
public class LoginInfo {
	public String level; // One of: "read", "write", or "admin"
	public String password; // Specified password

	public LoginInfo(String level, String password) {
		this.level = level;
		this.password = password;
	}

	/**
	 * Checks if this object is valid.
	 * 
	 * @return whether this contains a valid representation (with the appropriate
	 *         fields null/not null
	 */
	public boolean isValid() {
		return level != null && password != null;
	}
}
