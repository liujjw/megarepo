package distributedView;

import model.Critter;

/**
 * A JSON representation of a critter, containing its id, species_id, row, col,
 * direction, and mem, and possibly others.
 */
public class CritterInfo {
	public int id;
	public String species_id;
	public String program; // Can be null
	public int row;
	public int col;
	public int direction;
	public int[] mem;
	public Integer recently_executed_rule; // can be null

	/**
	 * Constructs a CritterInfo object with the given information
	 * 
	 * @param id                     id given by server
	 * @param species_id             species id
	 * @param program                program string
	 * @param row                    row
	 * @param col                    col
	 * @param direction              direction
	 * @param mem                    memory array
	 * @param recently_executed_rule last executed rule ID
	 */
	public CritterInfo(int id, String species_id, String program, int row, int col, int direction, int[] mem,
			Integer recently_executed_rule) {
		this.id = id;
		this.species_id = species_id;
		this.program = program;
		this.row = row;
		this.col = col;
		this.direction = direction;
		this.mem = mem;
		this.recently_executed_rule = recently_executed_rule;
	}

	/**
	 * Gets the equivalent CritterInfo object from a Critter
	 * 
	 * @param critter critter to convert to CritterInfo
	 * @return CritterInfo equivalent of critter
	 */
	public static CritterInfo toCritterInfo(Critter critter) {
		int id = critter.getID();
		String species_id = critter.getSpecies();
		String program = critter.getProgramString();
		int row = critter.getLocation().row;
		int col = critter.getLocation().col;
		int direction = critter.getDirection();
		int[] mem = critter.getMemory();
		int recently_executed_rule = critter.getLastRuleIndex();
		return new CritterInfo(id, species_id, program, row, col, direction, mem, recently_executed_rule);
	}

	/**
	 * Checks if this object is valid.
	 * 
	 * @return whether this contains a valid representation (with the appropriate
	 *         fields null/not null
	 */
	public boolean isValid() {
		return species_id != null && mem != null && ((program != null) == (recently_executed_rule != null));
	}
}
