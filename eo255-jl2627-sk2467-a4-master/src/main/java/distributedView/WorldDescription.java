package distributedView;

/** Represents a world description JSON object */
public class WorldDescription {
	public String description;

	public WorldDescription(String desc) {
		description = desc;
	}

	/**
	 * Checks if this object is valid.
	 * 
	 * @return whether this contains a valid representation (with the appropriate
	 *         fields null/not null
	 */
	public boolean isValid() {
		return description != null;
	}
}
