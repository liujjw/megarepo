package distributedView;

/** A data structure to store a species_id and an array of integers (ids) */
public class SpeciesIDs {
	public String species_id;
	public int[] ids;

	/**
	 * Constructs a SpeciesIDs object with given species_id and ids
	 * 
	 * @param species_id species_id
	 * @param ids        ids
	 */
	public SpeciesIDs(String species_id, int[] ids) {
		this.species_id = species_id;
		this.ids = ids;
	}

	/**
	 * Checks if this object is valid.
	 * 
	 * @return whether this contains a valid representation (with the appropriate
	 *         fields null/not null
	 */
	public boolean isValid() {
		return species_id != null && ids != null;
	}
}
