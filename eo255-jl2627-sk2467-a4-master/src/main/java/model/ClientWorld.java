package model;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;

import distributedView.TerrainInfo;
import distributedView.WorldInfo;
import model.WorldImpl.Vector;

/**
 * Represents the world datatype that the client will interact in
 */
public class ClientWorld extends WorldInfo implements ReadOnlyWorld {

	private TerrainInfo[][] terrain;
	private int rows;
	private int cols;
	private int numAliveCritters = 0;

	public Map<Integer, Vector> critterlocations = new HashMap<Integer, WorldImpl.Vector>();

	/**
	 * Constructs a ClientWorld object with the given parameters
	 */
	public ClientWorld(int current_timestep, double rate, String name, int population, int width, int height,
			JsonElement state) {
		super(current_timestep, rate, name, population, width, height, state);
		terrain = new TerrainInfo[this.height][this.width];
		cols = this.width;
		rows = 0;
		// height is always greater than number of rows
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (inBounds(i, j)) {
					rows++; // if a height has at least one hex in bounds, it is a row
					break;
				}
			}
		}
	}

	/**
	 * Check if a coordinate is out of bounds
	 * 
	 * @param row row of location
	 * @param col col of location
	 * @return whether the coordinate was in bounds or not
	 */
	public boolean inBounds(int row, int col) {
		return !(col >= this.width || 2 * row - col < 0 || 2 * row - col >= this.height || col < 0 || row < 0 || row >= height || col >= width);
	}

	/**
	 * Returns the ClientWorld equivalent of {@code info}
	 * 
	 * @param info WorldInfo object to be converted to ClientWorld
	 * @return ClientWorld representation of info
	 */
	public static ClientWorld toClientWorld(WorldInfo info) {
		return new ClientWorld(info.current_timestep, info.rate, info.name, info.population, info.width, info.height,
				info.state);
	}

	/**
	 * Gets the location of the critter with the given ID
	 * 
	 * @param id ID of critter
	 * @return vector of critter location, null if not found
	 */
	public Vector getCritterLocation(int id) {
		return critterlocations.get(id);
	}

	/**
	 * Sets the terrain at given row and column to info
	 * 
	 * @param r    row
	 * @param c    column
	 * @param info terrainInfo to store there
	 * @return whether or not r and c were in bounds and info could be set
	 */
	public boolean setTerrain(int r, int c, TerrainInfo info) {
		if (!inBounds(r, c) || info == null)
			return false;

		if (info.isCritter() == 1) {
			critterlocations.put(info.id, new Vector(c, r));
			numAliveCritters++;
		}
		terrain[r][c] = info;
		return true;

	}

	@Override
	public int getSteps() {
		return current_timestep;
	}

	@Override
	public int getNumberOfAliveCritters() {
		return numAliveCritters;
	}

	@Override
	public ReadOnlyCritter getReadOnlyCritter(int c, int r) {
		if (!inBounds(r, c))
			return null;
		TerrainInfo info = terrain[r][c];
		if (info == null || info.isCritter() != 1)
			return null;
		return ClientCritter.toClientCritter(info);
	}

	@Override
	public int getTerrainInfo(int c, int r) {
		TerrainInfo info = terrain[r][c];
		if (info == null) {
			return -1;
		}
		if (info.isCritter() == 1)
			throw new IllegalArgumentException("There was a critter here.");
		if (info.isNothing() == 1)
			return 0;
		if (info.isFood() == 1)
			return -1 * (info.value + 1);
		return -1;
	}

	@Override
	public int getCritterDirection(int c, int r) {
		TerrainInfo info = terrain[r][c];
		if (info == null || info.isCritter() != 1)
			return -1;
		return info.direction;
	}

	@Override
	public int numRows() {
		return rows;
	}

	@Override
	public int numCols() {
		return cols;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public boolean inBounds(Vector v) {
		return inBounds(v.row, v.col);
	}

}
