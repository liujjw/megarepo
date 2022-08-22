package distributedView;

/**
 * Represents an entity creation JSON object of the form: { "row": row, "col":
 * col, "type": type, "amount": amount }
 */
public class EntityCreation {
	public int row;
	public int col;
	public String type;
	public Integer amount; // can be null

	/**
	 * Constructs an EntityCreation object with given row, col, type, and amount
	 * 
	 * @param row    row
	 * @param col    column
	 * @param type   "food" or "rock"
	 * @param amount amount of food to spawn // can be null
	 */
	public EntityCreation(int row, int col, String type, Integer amount) {
		this.row = row;
		this.col = col;
		this.type = type;
		this.amount = amount;
	}

	/**
	 * Checks if this object is valid.
	 * 
	 * @return whether this contains a valid representation (with the appropriate
	 *         fields null/not null
	 */
	public boolean isValid() {
		if (type == null)
			return false;
		if (type.equals("food"))
			return amount != null;
		else {
			return amount == null;
		}
	}
}
