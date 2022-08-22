package distributedView;

/**
 * Represents all types of users: ADMIN, READ, or WRITE
 */
public enum UserType {
	ADMIN, // for creating new worlds and having control over all critters
	READ, // for reading general world state (excluding critter programs)
	WRITE; // for changing the world state and reading back programs of critters that this
			// user created

	/**
	 * Converts a UserType to its appropriate string representation
	 * 
	 * @param type UserType to convert
	 * @return String representation of type
	 */
	public static String toString(UserType type) {
		switch (type) {
		case ADMIN:
			return "admin";
		case READ:
			return "read";
		case WRITE:
			return "write";
		default:
			return "";
		}
	}

	/**
	 * Gets the UserType from a given string. If not one of "admin", "read", or
	 * "write", returns null
	 * 
	 * @param type type string
	 * @return corresponding UserType or null if invalid type
	 */
	public static UserType getTypeFromString(String type) {
		type = type.toLowerCase();
		switch (type) {
		case "admin":
			return UserType.ADMIN;
		case "read":
			return UserType.READ;
		case "write":
			return UserType.WRITE;
		default:
			return null;
		}
	}
}