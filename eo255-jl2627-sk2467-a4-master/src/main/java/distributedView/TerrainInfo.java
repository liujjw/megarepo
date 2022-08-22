package distributedView;

/**
 * A representation of a world hex that stores all necessary information. For
 * use in WorldInfo's state field
 */
public class TerrainInfo {
	/* For everything */
	public int row;
	public int col;
	public String type;

	/* For food */
	public Integer value;

	/* The rest for critters */
	public Integer id;
	public String species_id;
	public String program;
	public Integer direction;
	public int[] mem;
	public Integer recently_executed_rule;

	/**
	 * Constructs a TerrainInfo object with the specified data
	 * 
	 * @param row                    row - required
	 * @param col                    col - required
	 * @param type                   type - required, one of "food", "rock",
	 *                               "critter", or "nothing"
	 * @param value                  food value - optional, required for food
	 * @param id                     critter id - optional, required for critter
	 * @param species_id             critter species_id - optional, required for
	 *                               critter
	 * @param program                critter program string - optional, required for
	 *                               critter if user can access
	 * @param direction              critter direction - optional, required for
	 *                               critter
	 * @param mem                    critter mem array - optional, required for
	 *                               critter if user can access
	 * @param recently_executed_rule critter recently_executed_rule - optional,
	 *                               required for critter if user can access
	 */
	public TerrainInfo(int row, int col, String type, Integer value, Integer id, String species_id, String program,
			Integer direction, int[] mem, Integer recently_executed_rule) {
		this.row = row;
		this.col = col;
		this.type = type;
		this.value = value;
		this.id = id;
		this.species_id = species_id;
		this.program = program;
		this.direction = direction;
		this.mem = mem;
		this.recently_executed_rule = recently_executed_rule;
	}

	/**
	 * Checks if this object is valid.
	 * 
	 * @return whether this contains a valid representation (with the appropriate
	 *         fields null/not null
	 */
	public boolean isValid() {
		int n = isNothing();
		int f = isFood();
		int r = isRock();
		int c = isCritter();
		if (n == -1 || f == -1 || r == -1 || c == -1) {
			return false;
		}
		return isNothing() == 1 || isFood() == 1 || isRock() == 1 || isCritter() == 1;
	}

	/**
	 * Applies to each of the following isSomething methods. Checks if this object
	 * is of that type.
	 * 
	 * @return 1 if of that type, 0 if not, and -1 if it should be that type but
	 *         incorrect fields are filled out. For example, if of type rock but
	 *         there is an amount specified, this is an error and so will return -1.
	 */
	public int isNothing() {
		if (type == null)
			return -1;
		if (!type.equals("nothing"))
			return 0;
		if (value == null && id == null && species_id == null && program == null && direction == null && mem == null
				&& recently_executed_rule == null)
			return 1;
		return -1;
	}

	public int isFood() {
		if (type == null)
			return -1;
		if (!type.equals("food"))
			return 0;
		if (value != null && id == null && species_id == null && program == null && direction == null && mem == null
				&& recently_executed_rule == null)
			return 1;
		return -1;
	}

	public int isRock() {
		if (type == null)
			return -1;
		if (!type.equals("rock"))
			return 0;
		if (value == null && id == null && species_id == null && program == null && direction == null && mem == null
				&& recently_executed_rule == null)
			return 1;
		return -1;
	}

	public int isCritter() {
		if (type == null)
			return -1;
		if (!type.equals("critter"))
			return 0;
		if (value == null && id != null && species_id != null && direction != null && mem != null) {
			if ((program == null) == (recently_executed_rule == null))
				return 1;
			return -1;
		}
		return -1;
	}
}