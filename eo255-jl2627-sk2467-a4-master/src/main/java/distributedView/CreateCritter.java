package distributedView;

import com.google.gson.JsonElement;

/**
 * Used to store a critter creation JSON element, containing species_id,
 * program, mem, and either positions or num
 */
public class CreateCritter {
	public String species_id; // Can be null, in which case
	public String program; // program string. cannot be null
	public int[] mem; // critter's mem array, cannot be null.
	public JsonElement positions; // Should be array of CritterPositions
	public Integer num; // if positions given, then num should be null, and vice versa

	/**
	 * Constructs a CreateCritter object with given fields
	 * 
	 * @param species_id Can be null
	 * @param program    program string, cannot be null
	 * @param mem        mem array, cannot be null
	 * @param positions  // Should be JSONTree of of CritterPositions
	 * @param num        // if positions given, then num should be null, and vice
	 *                   versa
	 */
	public CreateCritter(String species_id, String program, int[] mem, JsonElement positions, Integer num) {
		this.species_id = species_id;
		this.program = program;
		this.mem = mem;
		this.positions = positions;
		this.num = num;
	}

	/**
	 * Checks if this object is valid.
	 * 
	 * @return whether this contains a valid representation (with the appropriate
	 *         fields null/not null
	 */
	public boolean isValid() {
		return species_id != null && program != null && mem != null && ((positions != null) ^ (num != null));
	}
}
