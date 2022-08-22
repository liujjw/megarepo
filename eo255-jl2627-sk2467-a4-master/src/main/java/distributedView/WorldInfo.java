package distributedView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

/** A representation of a world object for use with passing to and from JSON */
public class WorldInfo {
	public int current_timestep;
	public double rate;
	public String name;
	public int population;
	public int width;
	public int height;
	public JsonElement state;

	private static final Gson gson = new Gson();

	/**
	 * Constructs a WorldInfo object with given informatino
	 * 
	 * @param current_timestep current_timestep of world
	 * @param rate             rate of world
	 * @param name             name of world
	 * @param population       population of world
	 * @param width            width of world
	 * @param height           height of world
	 * @param state            state of world, should be JsonTree of TerrainInfo
	 *                         objects
	 */
	public WorldInfo(int current_timestep, double rate, String name, int population, int width, int height,
			JsonElement state) {
		this.current_timestep = current_timestep;
		this.rate = rate;
		this.name = name;
		this.population = population;
		this.width = width;
		this.height = height;
		this.state = state;
	}

	/**
	 * Checks if this object is valid.
	 * 
	 * @return whether this contains a valid representation (with the appropriate
	 *         fields null/not null
	 */
	public boolean isValid() {
		return name != null && state != null;
	}

}