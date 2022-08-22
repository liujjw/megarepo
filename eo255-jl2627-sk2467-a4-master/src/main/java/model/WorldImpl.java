package model;

import static model.Constants.FOOD_PER_SIZE;
import static model.Constants.HEIGHT;
import static model.Constants.MANNA_AMOUNT;
import static model.Constants.MANNA_COUNT;
import static model.Constants.WIDTH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

/** A class representing the world implementation */
public class WorldImpl implements World, ReadOnlyWorld {

	private HexGrid terrain; // inv: not null
	private int time; // inv: >0
	private String name; // inv: not null or empty string
	private Queue<Critter> queue; // inv: not null, can me empty
	/**
	 * A map for storing all critters IDs to their corresponding critter
	 */
	public Map<Integer, Critter> critterIDs = new HashMap<Integer, Critter>();

	/**
	 * Constructs a new world with name "New world" and width and height given in
	 * Constants.java file
	 */
	public WorldImpl() {
		this("New world", WIDTH, HEIGHT);
	}

	/**
	 * Constructs a world with a given name and size.
	 * 
	 * @param n name of created world
	 * @param w width of the world's hex representation. If -1, then supply default
	 *          value.
	 * @param h height of the world's hex representation. If -1, then supply default
	 *          value.
	 */
	public WorldImpl(String n, int w, int h) {
		// rows correspond to height, cols to width
		time = 0;
		name = n;
		terrain = new HexGrid(w, h);
		queue = new LinkedList<>();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void addCritID(int ID, Critter critter) {
		critterIDs.put(ID, critter);
	}

	@Override
	public Critter getCritterByID(int ID) {
		return critterIDs.get(ID);
	}

	@Override
	public Map<Integer, Critter> getCritterIDMap() {
		return critterIDs;
	}

	@Override
	public int getSteps() {
		return time;
	}

	@Override
	public int getNumberOfAliveCritters() {
		synchronized (queue) {
			return queue.size();
		}
	}

	@Override
	public ReadOnlyCritter getReadOnlyCritter(int c, int r) {
		Vector coord = new Vector(c, r);
		HexTile h = terrain.get(coord);
		if (!(h instanceof CritterTile))
			return null;
		return (ReadOnlyCritter) ((CritterTile) h).critter;
	}

	@Override
	public int getTerrainInfo(int c, int r) throws IllegalArgumentException {
		Vector coord = new Vector(c, r);
		if (!terrain.inBounds(coord)) {
			return -1;
		}
		HexTile h = terrain.get(coord);
		if (h instanceof RockTile) {
			return -1;
		} else if (h instanceof CritterTile) {
			throw new IllegalArgumentException();
		} else if (h instanceof FoodTile) {
			int x = ((FoodTile) h).amntFood;
			int k = -(x + 1);
			return k;
		} else if (h instanceof EmptyTile) {
			return 0;
		} else {
			return -1; // default i guess
		}
	}

	@Override
	public void setRock(int c, int r) {
		terrain.set(new Vector(c, r), new RockTile());
	}

	@Override
	public void setNewlyCreatedCritter(int c, int r, int d, Critter cr) {
		synchronized (queue) {
			CritterTile crt = new CritterTile(cr, Orientation.toOrientation(d), this, new Vector(c, r));
			terrain.set(new Vector(c, r), crt);
			if (queue.contains(cr)) {
				System.out.println("WARNING: ignored adding a duplicate critter to queue");
				return;
			}
			queue.add(cr);
		}
	}

	/**
	 * Kills the critter in that critterTile
	 * 
	 * @param critterTile critterTile holding critter to be killed
	 */
	public void killCritter(CritterTile critterTile) {
		synchronized (queue) {
			queue.remove(critterTile.critter);
			critterIDs.remove(critterTile.critter.getID());
			terrain.set(critterTile.worldCoordinate, new FoodTile(FOOD_PER_SIZE * critterTile.critter.getMemory(3),
					new Vector(critterTile.worldCoordinate.col, critterTile.worldCoordinate.row)));
		}
	}

	/**
	 * Retrieves the HexTile at location
	 * 
	 * @param at location of HexTile to be retrieved
	 * @return HexTile at location (Rock if out of bounds)
	 */
	public HexTile getTile(Vector at) {
		return terrain.get(at);
	}

	@Override
	public void changeFood(int c, int r, int amt) {
		Vector coord = new Vector(c, r);
		HexTile h = terrain.get(coord);
		if (h instanceof FoodTile) {
			FoodTile f = ((FoodTile) h);
			f.amntFood += amt;
			if (f.amntFood <= 0) {
				terrain.set(new Vector(c, r), new EmptyTile(new Vector(c, r)));
			}
		} else if (h instanceof EmptyTile) {
			setFood(coord, amt);
		} // do nothing otherwise
	}

	/**
	 * Moves the given CritterTile at {@code from} to {@code to}
	 * 
	 * @param crtTile CritterTile to be moved
	 * @param from    initial location
	 * @param to      final location
	 */
	public void moveCritter(CritterTile crtTile, Vector from, Vector to) {
		terrain.set(to, new CritterTile(crtTile.critter, crtTile.critterOrientation, this, to));
		terrain.set(from, new EmptyTile(new Vector(from.col, from.row)));
	}

	/**
	 * Sets the food at given location to amount
	 * 
	 * @param v   location vector
	 * @param amt amount of food to be set
	 */
	public void setFood(Vector v, int amt) {
		terrain.set(v, new FoodTile(amt, new Vector(v.col, v.row)));
	}

	@Override
	public void passTime() {
		synchronized (queue) {
			time++;
			// critters could die or be created in the middle of a time step
			// queue may also have critters added or removed
			ArrayList<Critter> curCrits = new ArrayList<>();
			for (Critter crit : queue) {
				crit.setHadTurn(false); // redundant/unnecessary for now
				curCrits.add(crit);
			}
			for (Critter crit : curCrits) {
				Interpreter.interpret(crit);
				crit.setHadTurn(true);
			}
			ArrayList<Critter> newCrits = new ArrayList<>();
			while (true) {
				for (Critter c : queue) {
					if (!c.hadTurn()) {
						newCrits.add(c);
					}
				}
				if (newCrits.isEmpty())
					break;
				for (Critter cr : newCrits) {
					cr.setHadTurn(true);
					Interpreter.interpret(cr);
				}
				newCrits = new ArrayList<>();
			}

			for (int i = 0; i < MANNA_COUNT; i++) {
				Vector v = randomInBoundsCoord();
				if (terrain.get(v) instanceof EmptyTile) {
					terrain.set(v, new FoodTile(MANNA_AMOUNT, new Vector(v.col, v.row)));
				} else if (terrain.get(v) instanceof FoodTile) {
					((FoodTile) terrain.get(v)).amntFood += MANNA_AMOUNT;
				}
			}
		}
	}

	@Override
	public int numRows() {
		return terrain.numRows;
	}

	@Override
	public int numCols() {
		return terrain.numCols;
	}

	@Override
	public Vector getRandomLocation() {
		Vector v = randomInBoundsCoord();
		int counter = 1;
		while (!(terrain.get(v) instanceof EmptyTile)) {
			v = randomInBoundsCoord();
			if (counter++ > terrain.numRows * terrain.numCols) {
				return new Vector(-1, -1); // nowhere to place
			}
		}
		return v;
	}

	/**
	 * Get a random coordinate in bounds, or (0,0) if it takes too long.
	 * 
	 * @return random in bounds vector coordinate, may or may not be occupied
	 */
	private Vector randomInBoundsCoord() {
		Random r = new Random();
		int row = r.nextInt(terrain.height);
		int col = r.nextInt(terrain.width);
		int counter = 1;
		while (!terrain.inBounds(new Vector(col, row))) {
			if (counter > 999) {
				return new Vector(0, 0);
			}
			if (r.nextBoolean()) {
				row = r.nextInt(terrain.height);
				counter++;
			} else {
				col = r.nextInt(terrain.width);
				counter++;
			}
		}
		return new Vector(col, row);
	}

	/**
	 * Checks whether the given coordinate is in bounds
	 * 
	 * @param v vector to be checked
	 */
	public boolean inBounds(Vector v) {
		return terrain.inBounds(v);
	}

	@Override
	public int getHeight() {
		return terrain.height;
	}

	@Override
	public int getWidth() {
		return terrain.width;
	}

	@Override
	public int getCritterDirection(int c, int r) {
		Vector coord = new Vector(c, r);
		HexTile h = terrain.get(coord);
		if (h instanceof CritterTile) {
			return (((CritterTile) h).critterOrientation.numeric);
		}
		return -1;
	}

	/**
	 * Critter orientation in the world, along with a vector displacement in that
	 * orientation.
	 */
	public enum Orientation {
		N(new Vector(0, 1), 0), NE(new Vector(1, 1), 1), SE(new Vector(1, 0), 2), S(new Vector(0, -1), 3),
		SW(new Vector(-1, -1), 4), NW(new Vector(-1, 0), 5);

		public Vector displacement;
		public int numeric;

		/**
		 * Creates an Orientation with given displacement and numeric
		 * 
		 * @param v displacement vector
		 * @param n numeric vector
		 */
		private Orientation(Vector v, int n) {
			displacement = v;
			numeric = n;
		}

		/**
		 * Converts the given integer to an Orientation
		 * 
		 * @param i int corresponding to direction as per specs
		 * @return Orientation
		 */
		public static Orientation toOrientation(int i) {
			switch (i) {
			default:
				return N;
			case 1:
				return NE;
			case 2:
				return SE;
			case 3:
				return S;
			case 4:
				return SW;
			case 5:
				return NW;
			}
		}
	}

	/** A class to be used in the HexGrid for representing a critter */
	public class CritterTile extends HexTile {
		public Critter critter; // inv: not null
		public Orientation critterOrientation; // inv: not null
		public Vector worldCoordinate; // inv: not null

		/**
		 * Constructs a critter tile holding given critter with given critter
		 * orientation in the specified world at given location
		 * 
		 * @param critter            critter to be placed
		 * @param critterOrientation orientation of critter
		 * @param w                  world to be spawned in
		 * @param worldCoordinate    coordinate in world
		 */
		public CritterTile(Critter critter, Orientation critterOrientation, World w, Vector worldCoordinate) {
			type = 1;
			this.critter = critter;
			this.critterOrientation = critterOrientation;
			this.worldCoordinate = worldCoordinate;
			this.critter.setWorld(w);
			this.critter.setCritterTile(this);
		}

		/**
		 * Gets the critter located in this tile
		 *
		 * @return critter housed in tile
		 */
		public Critter getCritter() {
			return critter;
		}

		/**
		 * Returns the direction of the critter stored in this tile
		 *
		 * @return direction as an int 0..5
		 */
		public int getDirection() {
			return critterOrientation.numeric;
		}

		/**
		 * Another way to get the world coordinate.
		 */
		@Override
		public Vector getCoord() {
			return worldCoordinate;
		}
	}

	/** A class to be used in HexGrid for representing food */
	public class FoodTile extends HexTile {
		/** Can be accessed publicly and modified. */
		public int amntFood; // inv: >0
		private Vector v;

		/**
		 * Constructs a FoodTile with given amount of food
		 * 
		 * @param n amount of food
		 * @param v coordinate of foodtile
		 */
		public FoodTile(int n, Vector v) {
			this.v = v;
			type = 2;
			assert n > 0;
			amntFood = n;
		}

		/**
		 * @return coordinate of foodtile in world
		 */
		public Vector getCoord() {
			return v;
		}
	}

	/** A class to be used in HexGrid for representing a rock */
	public class RockTile extends HexTile {
		/**
		 * Constructs a RockTile
		 */
		public RockTile() {
			type = 3;
		}

		/**
		 * Not implemented yet. WARNING.
		 */
		@Override
		public Vector getCoord() {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Represents the hexagonal grid of tiles of the Critter World.
	 */
	public class HexGrid {
		private HexTile[][] h;
		public int numRows; // Inv: >0
		public int numCols; // Inv: >0
		public int height; // Inv: >0
		public int width; // Inv: >0

		/**
		 * Constructs a HexGrid with given width and height
		 * 
		 * @param width  width of grid
		 * @param height height of grid
		 */
		public HexGrid(int width, int height) {
			this.height = height;
			this.width = width;
			h = new HexTile[this.height][this.width];
			numCols = this.width;
			numRows = 0;
			// height is always greater than number of rows
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					if (inBounds(new Vector(j, i))) {
						numRows++; // if a height has at least one hex in bounds, it is a row
						break;
					}
				}
			}
		}

		/**
		 * Sets the given location to the given hextile
		 * 
		 * @param v location of placement
		 * @param n HexTile to be placed there
		 */
		public void set(Vector v, HexTile n) {
			if (!inBounds(v)) {
				System.out.println("Out of bounds set method call in HexGrid ignored.");
				return; // cant set out of bounds coordinates, off limits from modification so to speak
			}
			h[v.row][v.col] = n;
		}

		/**
		 * Gets the HexTile at the given location
		 * 
		 * @param v location of retrieval
		 * @return HexTile at location (RockTile if out-of-bounds)
		 */
		public HexTile get(Vector v) {
			if (!inBounds(v)) {
				return new RockTile();
			} else if (h[v.row][v.col] == null && inBounds(v)) { // we dont set empty tiles until we call them
				h[v.row][v.col] = new EmptyTile(new Vector(v.col, v.row));
			}
			return h[v.row][v.col];
		}

		/**
		 * Check if a coordinate is out of bounds
		 * 
		 * @param v location coordinates
		 * @return whether the coordinate was in bounds or not
		 */
		public boolean inBounds(Vector v) {
			return !(v.col >= this.width || 2 * v.row - v.col < 0 || 2 * v.row - v.col >= this.height || v.col < 0
					|| v.row < 0 || v.row >= h.length || v.col >= h[v.row].length);
		}
	}

	/**
	 * Use for world coordinate manipulation; an ordered pair abstraction. Columns
	 * are first to correspond to hex world representation.
	 */
	public static class Vector {
		public int row; // Inv: not null
		public int col; // Inv: not null

		/**
		 * Constructs a vector with given row and col
		 * 
		 * @param col column coordinate
		 * @param row row coordinate
		 */
		public Vector(int col, int row) {
			this.col = col;
			this.row = row;
		}

		/**
		 * Computes the vector sum between this and v
		 * 
		 * @param v other vector to be added to this
		 * @return summed vector
		 */
		public Vector vectorSum(Vector v) {
			Vector sum = new Vector(this.col + v.col, this.row + v.row);
			return sum;
		}

		@Override
		public boolean equals(Object obj) {
			Vector b = (Vector) obj;
			return (b.col == this.col && b.row == this.row);
		}
	}

	@Override
	public boolean removeCritter(Vector v) {
		HexTile tile = getTile(v);
		if (!(tile instanceof CritterTile))
			return false;
		terrain.set(v, new EmptyTile(v));
		return true;
	}

}
