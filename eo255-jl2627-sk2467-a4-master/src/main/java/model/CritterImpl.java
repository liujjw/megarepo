package model;

import static model.Constants.ABILITY_COST;
import static model.Constants.ATTACK_COST;
import static model.Constants.BASE_DAMAGE;
import static model.Constants.BUD_COST;
import static model.Constants.DAMAGE_INC;
import static model.Constants.ENERGY_PER_SIZE;
import static model.Constants.GROW_COST;
import static model.Constants.INITIAL_ENERGY;
import static model.Constants.MATE_COST;
import static model.Constants.MAX_SMELL_DISTANCE;
import static model.Constants.MOVE_COST;
import static model.Constants.RULE_COST;
import static model.Constants.SOLAR_FLUX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import ast.Node;
import ast.Program;
import ast.ProgramImpl;
import ast.Rule;
import distributedView.CritterWorldServer;
import model.WorldImpl.Vector;
import util.digraph.Vertex;
import util.digraph.WeightedDigraph;
import util.digraph.WeightedVertex;

/** A class to hold the critter implementation */
public class CritterImpl implements Critter, ReadOnlyCritter {

	private String species; // inv: not empty string
	private Memory memory; // inv: not null
	private Program genome; // inv: not null
	private Rule lastExecutedRule;
	private World world; // inv: not null
	private WorldImpl.CritterTile crtTile; // inv: not null
	private boolean lusty;
	private int subtractedEnergy;
	private boolean hadATurn;
	private int lastSmell;
	private final int ID;

	public static final int NO_FOOD_SMELL_VAL = 1000000;

	/**
	 * Constructs a Critter with given species name, memory array, and program
	 * 
	 * @param species species name of critter
	 * @param memory  initial memory array of critter. Size cannot be less than 7.
	 *                If any of the values in memory array are -1, default values
	 *                will be applied in appropriate slots.
	 *
	 * @param program parsed program critter obeys
	 */
	public CritterImpl(String species, int[] memory, Program program) {
		this.species = species;
		this.genome = program;
		this.memory = new Memory(memory[0]);
		this.hadATurn = false;
		System.arraycopy(memory, 0, this.memory.memArray, 0, Math.min(memory.length, memory[0]));
		this.subtractedEnergy = 0;
		this.lusty = false;
		this.lastExecutedRule = null;
		this.ID = CritterWorldServer.getCritterID();
	}

	@Override
	public Vector getLocation() {
		return this.crtTile.worldCoordinate;
	}

	@Override
	public void setHadTurn(boolean b) {
		hadATurn = b;
	}

	@Override
	public boolean hadTurn() {
		return hadATurn;
	}

	public int smell() {
		WeightedDigraph<HexTile> graph = new WeightedDigraph<>();
		return search(graph);
	}

	// generates a graph representation of the hex grid before searching
	// issues: need to do - 1 to get accurate distance, critter could be facing 3
	// turns away from a slice, so should be max smell - 3 (rotate grid?)
	// simple, but very inefficient because rocks could drastically reduce distances
	// (see a7 spec fig 2)
	private void gridToGraph_before(WeightedDigraph<HexTile> g) {
		WorldImpl.Vector critCoord = crtTile.worldCoordinate;
		for (int row = 0; row < world.numRows(); row++) {
			for (int col = 0; col < world.numCols(); col++) {
				WorldImpl.Vector coordToCheck = new WorldImpl.Vector(col, row);
				if (world.inBounds(coordToCheck)) {
					// "slices", section 2 of the project spec
					int delRow = Math.abs(critCoord.row - coordToCheck.row);
					int delCol = Math.abs(critCoord.col - coordToCheck.col);
					int delSlice = Math.abs(delRow - delCol);
					int len = Math.max(Math.max(delCol, delRow), delSlice);
					if (len <= MAX_SMELL_DISTANCE) {
						g.addVertex(new Vertex<HexTile>(world.getTile(coordToCheck)));
					}
				}
			}
		}
	}

	// call while searching to generate graph on the fly
	private void generateGraph(WeightedDigraph<HexTile> g, Vertex<HexTile> source, WorldImpl.Vector sourceCoord) {
		g.addVertex(source);
		for (int i = 0; i < 6; i++) {
			HexTile hexTileNearby = world.getTile(aheadCoord(WorldImpl.Orientation.toOrientation(i), sourceCoord));
			// food tiles count as empty tiles from a7 spec fig 2
			// crittertiles cannot be crossed (known problem: the root tile is never added
			// to the adjacent vertices of
			// nearby root vertices, but shouldnt matter because backtracking will always
			// give the non-optimal path)
			if (hexTileNearby instanceof EmptyTile || hexTileNearby instanceof WorldImpl.FoodTile) {
				Vertex<HexTile> b = new Vertex<>(hexTileNearby);
				g.addVertex(b);
				g.addEdge(source, b, 1);
			} else { // placeholder for using indices to know what direction
				Vertex<HexTile> b = new Vertex<>(new Impassible());
				g.addVertex(b);
				g.addEdge(source, b, Integer.MAX_VALUE);
			}
		}
	}

	private class Impassible extends HexTile {
		@Override
		public WorldImpl.Vector getCoord() {
			throw new UnsupportedOperationException();
		}
	}

	// take crit's current location and make that the root
	// push onto queue with distance 0
	// generate the graph from root coordinate
	// then pop, look at its adjacent edges
	// if any edge contains a foodtile then return the distance to get to that tile
	// - 1
	// else if the adjacent edges are in the queue, update their distance to be the
	// lesser of the current one found and its previous one
	// we check queue membership by our equals function: two weighted vertices are
	// equal if they contain the same vertex
	// if not in the queue put in the queue
	// further generate graph from that vertex
	// update crit direction
	private int search(WeightedDigraph<HexTile> g) {
		// weighed vertices in queue contain distances = moveforward + turns, NOT
		// weights from vertex a to b
		PriorityQueue<WeightedVertex<HexTile>> frontier = new PriorityQueue<>();
		// holds distance info (cant index into pqueue)
		HashMap<Vertex<HexTile>, Integer> distances = new HashMap<>();
		// map of critter directions based on vertex
		HashMap<Vertex<HexTile>, Integer> critDir = new HashMap<>();
		// backpointers, space inefficient but fast
		HashMap<Vertex<HexTile>, Vertex<HexTile>> backpointers = new HashMap<>();

		// treat critter tile as empty tile
		Vertex<HexTile> root = new Vertex<>(
				new EmptyTile(new WorldImpl.Vector(crtTile.worldCoordinate.col, crtTile.worldCoordinate.row)));
		WeightedVertex<HexTile> wroot = new WeightedVertex<>(root, 0);
		critDir.put(root, crtTile.critterOrientation.numeric);
		backpointers.put(root, null);

		frontier.add(wroot);
		WorldImpl.Vector curCoord = crtTile.worldCoordinate;
		generateGraph(g, root, curCoord);

		int direction = Integer.MIN_VALUE;
		int shortestPathSoFar = Integer.MAX_VALUE;

		// critter cant already be on a foodtile
		while (!frontier.isEmpty()) {
			WeightedVertex<HexTile> v = frontier.poll();
			// distances.remove(v.getVertex());
			ArrayList<WeightedVertex<HexTile>> adjVertices = g.getAdjVertices(v.getVertex());
			assert adjVertices != null : "No adjacent vertices found for vertex.";
			for (int i = 0; i < adjVertices.size(); i++) {
				if (adjVertices.get(i).getVertex().getData() instanceof Impassible)
					continue;
				assert adjVertices.size() <= 6 : "Illegal number of adjacent vertices in graph generation";
				WeightedVertex<HexTile> w = adjVertices.get(i);
				// max diff in rel dir is 3; beyond that turn other direction to get less turns
				// position in array tells direction of tile
				int diff = (Math.abs(i - critDir.get(v.getVertex())));
				int relativeDirection = diff <= 3 ? diff : (diff == 4 ? 2 : 1);
				int delDistance = relativeDirection + w.getWeight();
				int distanceToVertex = v.getWeight() + delDistance; // a7 spec fig 2
				if (v.getVertex().equals(root))
					distanceToVertex -= 1;
				if (distanceToVertex > MAX_SMELL_DISTANCE)
					continue;
				WeightedVertex<HexTile> newW = new WeightedVertex<>(w.getVertex(), distanceToVertex);
				if (w.getVertex().getData() instanceof WorldImpl.FoodTile) {
					Vertex<HexTile> vertexAlongPath = v.getVertex();
					while (true) {
						Vertex<HexTile> prev = backpointers.get(vertexAlongPath);
						if (prev == null) {
							vertexAlongPath = w.getVertex(); // when food is within the adj vertices of root
							break;
						}
						if (prev.equals(root))
							break;
						vertexAlongPath = backpointers.get(vertexAlongPath);
					}
					// get direction
					ArrayList<WeightedVertex<HexTile>> adjs = g.getAdjVertices(root);
					int absDir = Integer.MAX_VALUE;
					for (int j = 0; j < adjs.size(); j++) {
						if (adjs.get(j).getVertex().equals(vertexAlongPath)) {
							absDir = j;
							break;
						}
						if (j == adjs.size() - 1)
							assert false : "Couldn't find absDir";
					}
					// max val of rel dir is 5 when calculating direction but in searching it is 3
					// solve (crit's current orientation + x = orientation starting shortest path)
					// mod 6
					direction = trueMod(absDir - critDir.get(root), 6);
					int val = distanceToVertex * 1000 + direction;
					if (val < shortestPathSoFar)
						shortestPathSoFar = val; // section 11 a7 spec (lowest moves considered without direction)
				}
				if (distances.containsKey(w.getVertex())) {
					if (distances.get(w.getVertex()) > distanceToVertex) {
						distances.put(w.getVertex(), distanceToVertex);
						frontier.remove(w);
						frontier.add(newW);
						backpointers.put(w.getVertex(), v.getVertex());
					}
				} else {
					distances.put(w.getVertex(), distanceToVertex);
					frontier.add(newW);
					generateGraph(g, w.getVertex(), w.getVertex().getData().getCoord());
					critDir.put(w.getVertex(), i); // Require: orientation that results in least number of turns and
													// moves
					backpointers.put(w.getVertex(), v.getVertex());
				}
			}
		}
		lastSmell = shortestPathSoFar < NO_FOOD_SMELL_VAL ? shortestPathSoFar : NO_FOOD_SMELL_VAL;
		return Math.min(shortestPathSoFar, NO_FOOD_SMELL_VAL);
	}

	public int getLastSmell() {
		return lastSmell;
	}

	/**
	 * Number theoretic definition of modulus (consistent with definition for
	 * negatives).
	 * 
	 * @param a number to mod
	 * @param b modulus
	 */
	private int trueMod(int a, int b) {
		return (((a % b) + b) % b);
	}

	/**
	 * Returns the complexity of the critter as specified in the project specs
	 * 
	 * @return complexity value
	 */
	public int getComplexity() {
		int numRules = genome.getChildren().size();
		int offense = memory.getOffense();
		int defense = memory.getDefense();
		return numRules * RULE_COST + (offense + defense) * ABILITY_COST;
	}

	@Override
	public boolean isLusty() {
		return lusty;
	}

	@Override
	public String getSpecies() {
		return species;
	}

	@Override
	public int[] getMemory() {
		int[] m = new int[memory.size()];
		System.arraycopy(memory.memArray, 0, m, 0, memory.size());
		return m;
	}

	@Override
	public String getProgramString() {
		return genome.toString();
	}

	@Override
	public String getLastRuleString() {
		if (lastExecutedRule != null) {
			return lastExecutedRule.toString();
		} else {
			return "None";
		}
	}

	@Override
	public int getLastRuleIndex() {
		if (lastExecutedRule != null) {
			return genome.getChildren().indexOf(lastExecutedRule);
		} else {
			return -1;
		}
	}

	@Override
	public int getMemory(int index) {
		if (index >= memory.size() || index < 0) {
			return 0;
		}
		return memory.memArray[index];
	}

	@Override
	public void setLastExecutedRule(Rule lastExecutedRule) {
		this.lastExecutedRule = lastExecutedRule;
	}

	@Override
	public void setMemFromRule(int index, int val) {
		if (index <= 5)
			return;
		if (index == 6 && (val > 99 || val < 0))
			return;
		memory.set(index, val);
	}

	@Override
	public void setPass(int val) {

		if (val < 0 || val > Constants.MAX_RULES_PER_TURN)
			return;
		memory.set(5, val);
	}

	/**
	 * Sets the critter's world to w
	 * 
	 * @param w world
	 */
	public void setWorld(World w) {
		world = w;
		world.addCritID(this.ID, this);

	}

	/**
	 * Sets the critter's tile to crt
	 * 
	 * @param crt CritterTile to be set
	 */
	public void setCritterTile(WorldImpl.CritterTile crt) {
		crtTile = crt;
	}

	@Override
	public int getAhead(int index) {
		if (index < 0)
			index = 0;
		WorldImpl.Vector at = this.crtTile.worldCoordinate;
		for (int i = 0; i < index; i++) {
			at = at.vectorSum(this.crtTile.critterOrientation.displacement);
		}
		return sensingScheme(at);
	}

	/*
	 * Applies sensing scheme in section 7 of project spec, checks for out of bounds
	 * before asking for a tile.
	 * 
	 * @param coordinate coordinate
	 */
	private int sensingScheme(WorldImpl.Vector coordinate) {
		HexTile h = world.getTile(coordinate); // always returns a tile
		if (h instanceof WorldImpl.RockTile) {
			return -1;
		} else if (h instanceof EmptyTile) {
			return 0;
		} else if (h instanceof WorldImpl.CritterTile) {
			return ((WorldImpl.CritterTile) h).critter.getAppearance(this.crtTile.critterOrientation.numeric);
		} else if (h instanceof WorldImpl.FoodTile) {
			return -(((WorldImpl.FoodTile) h).amntFood + 1);
		} else {
			return -1;
		}
	}

	@Override
	public int getNearby(int index) {
		int dir = (this.crtTile.critterOrientation.numeric + index) % 6;
		WorldImpl.Vector coordinate = (this.crtTile.worldCoordinate
				.vectorSum(WorldImpl.Orientation.toOrientation(dir).displacement));
		return sensingScheme(coordinate);
	}

	public int getAppearance(int observer) {
		int dir = (Math.abs(observer - this.crtTile.critterOrientation.numeric));
		return memory.getSize() * 1000 + memory.getPosture() * 10 + dir;
	}

	@Override
	public int getDirection() {
		return this.crtTile.critterOrientation.numeric;
	}

	@Override
	public void moveForward() {
		if (!changeEnergy(-(memory.getSize() * MOVE_COST)))
			return;
		if (getAhead(1) != 0) {
			return;
		}
		world.moveCritter(this.crtTile, this.crtTile.worldCoordinate,
				this.crtTile.worldCoordinate.vectorSum(this.crtTile.critterOrientation.displacement));
	}

	@Override
	public void moveBackwards() {
		if (!changeEnergy(-(memory.getSize() * MOVE_COST)))
			return;
		if (getNearby(3) != 0)
			return;
		world.moveCritter(this.crtTile, this.crtTile.worldCoordinate,
				this.crtTile.worldCoordinate.vectorSum(behindDirection(this.crtTile.critterOrientation).displacement));
	}

	@Override
	public void waiting() {
		if (!this.changeEnergy(memory.getSize() * SOLAR_FLUX))
			return;
	}

	@Override
	public void eat() {
		if (!this.changeEnergy(-memory.getSize()))
			return;
		if (getAhead(1) < -1) {
			WorldImpl.Vector foodCoord = this.crtTile.worldCoordinate
					.vectorSum(this.crtTile.critterOrientation.displacement);
			HexTile h = world.getTile(foodCoord);
			if (!(h instanceof WorldImpl.FoodTile))
				return;
			int food = ((WorldImpl.FoodTile) h).amntFood;
			int max = memory.getSize() * ENERGY_PER_SIZE;
			int hungry = max - memory.getEnergy();
			int eat = Math.min(food, Math.max(hungry, 0));
			memory.set(4, memory.getEnergy() + eat);
			world.changeFood(foodCoord.col, foodCoord.row, -eat);
		}
	}

	/**
	 * Changes the critter's energy by {@code by}
	 * 
	 * @param by amount to be changed
	 * @return whether there was enough energy to subtract from and the critter is
	 *         still alive
	 */
	public boolean changeEnergy(int by) {
		int curEnergy = memory.getEnergy();
		int finalEnergy = curEnergy + by;
		if (finalEnergy <= 0) {
			world.killCritter(this.crtTile);
			return false;
		}
		if (finalEnergy > ENERGY_PER_SIZE * this.memory.getSize()) {
			finalEnergy = ENERGY_PER_SIZE * this.memory.getSize();
		}
		memory.set(4, finalEnergy);
		return true;
	}

	@Override
	public void turnLeft() {
		if (!this.changeEnergy(-memory.getSize()))
			return;
		int n = this.crtTile.critterOrientation.numeric - 1 < 0 ? 5 : this.crtTile.critterOrientation.numeric - 1;
		this.crtTile.critterOrientation = WorldImpl.Orientation.toOrientation(n);
	}

	@Override
	public void turnRight() {
		if (!this.changeEnergy(-memory.getSize()))
			return;
		int n = (this.crtTile.critterOrientation.numeric + 1) % 6;
		this.crtTile.critterOrientation = WorldImpl.Orientation.toOrientation(n);
	}

	@Override
	public void serve(int exprVal) {
		int d = memory.getSize() + exprVal;
		if (d > memory.getEnergy())
			d = memory.getEnergy();
		this.changeEnergy(-d); // can kill critter, but still completes the serve
		WorldImpl.Vector v = aheadCoord(crtTile.critterOrientation, crtTile.worldCoordinate);
		HexTile inFront = world.getTile(v); // TODO cleanup helper methods that search for hexes in front or whatever
		if (inFront instanceof WorldImpl.FoodTile) {
			WorldImpl.FoodTile food = (WorldImpl.FoodTile) inFront;
			food.amntFood += d;
		} else if (inFront instanceof EmptyTile) {
			world.setFood(v, d);
		}

	}

	@Override
	public void attack() {
		if (!this.changeEnergy(-(memory.getSize() * ATTACK_COST)))
			return;
		HexTile tile = world.getTile(aheadCoord(this.crtTile.critterOrientation, this.crtTile.worldCoordinate));
		if (!(tile instanceof WorldImpl.CritterTile))
			return;
		Critter victim = ((WorldImpl.CritterTile) tile).critter;
		int s1 = this.getMemory(3);
		int s2 = victim.getMemory(3);
		int o1 = this.getMemory(2);
		int d2 = victim.getMemory(1);
		int damage = (int) (BASE_DAMAGE * s1 * P(DAMAGE_INC * (s1 * o1 - s2 * d2)));
		if (!victim.changeEnergy(-damage))
			return;
	}

	/**
	 * Computes 1/(1+e^-x)
	 * 
	 * @param x x
	 * @return 1/(1+e^-x)
	 */
	private double P(double x) {
		double den = (1 + Math.exp(-x));
		return (1 / den);
	}

	@Override
	public void grow() {
		if (!this.changeEnergy(-(memory.getSize() * this.getComplexity() * GROW_COST)))
			return;
		memory.set(3, memory.getSize() + 1);
	}

	@Override
	public void bud() {
		if (!changeEnergy(-(BUD_COST * getComplexity())))
			return;
		Memory childMem = new Memory(memory.size());
		System.arraycopy(memory.memArray, 0, childMem.memArray, 0, memory.size());
		childMem.memArray[4] = INITIAL_ENERGY;
		childMem.memArray[3] = 1;
		childMem.memArray[6] = 0;
		for (int i = 7; i < memory.size(); i++) {
			childMem.memArray[i] = 0;
		}
		Program childGenome = ((Program) genome.clone()).mutate(); // just in case mutate doesnt return a new program
		WorldImpl.Orientation parentOrientation = this.crtTile.critterOrientation;
		WorldImpl.Orientation backwardOrientation = behindDirection(parentOrientation);
		WorldImpl.Vector backCoord = this.crtTile.worldCoordinate.vectorSum(backwardOrientation.displacement);
		if (!world.inBounds(backCoord)) {
			// TODO choose another valid coordinate
			return;
		}
		Random r = new Random();
		Critter offspring = new CritterImpl(species + ((char) ((r.nextInt(123) + 65) % 123)), childMem.memArray,
				childGenome);
		world.setNewlyCreatedCritter(backCoord.col, backCoord.row, parentOrientation.numeric, offspring);
	}

	/**
	 * Direction to the back of a given orientation.
	 * 
	 * @param o orientation
	 */
	private WorldImpl.Orientation behindDirection(WorldImpl.Orientation o) {
		return WorldImpl.Orientation.toOrientation((o.numeric + 3) % 6);
	}

	/**
	 * Returns the vector sum of v and the given orientation's displacement vector
	 * 
	 * @param o orientation
	 * @param v vector
	 * @return vector sum
	 */
	private WorldImpl.Vector aheadCoord(WorldImpl.Orientation o, WorldImpl.Vector v) {
		return v.vectorSum(o.displacement);
	}

	/**
	 * Gets the subtracted energy
	 * 
	 * @return subtracted energy
	 */
	public int subtractedEnergy() {
		return subtractedEnergy;
	}

	@Override
	public void mate() {
		lusty = true;
		if (getNearby(0) <= 0)
			return;
		HexTile h = (world
				.getTile(this.crtTile.worldCoordinate.vectorSum(this.crtTile.critterOrientation.displacement)));
		if (!(h instanceof WorldImpl.CritterTile))
			return; // additional check to avoid class cast exception
		WorldImpl.CritterTile mate = (WorldImpl.CritterTile) h;
		// if other crit isnt ready to groovy in this time step, still subtract energy
		// as though we have failed,
		// then subtract the rest if mate is actually ready
		if (mate.critter.isLusty()
				&& (behindDirection(mate.critterOrientation).numeric == this.crtTile.critterOrientation.numeric)) {
			// use == to avoid .equals() on objects
			int matingCost1 = MATE_COST * getComplexity();
			int matingCost2 = MATE_COST * mate.critter.getComplexity();
			if (!this.changeEnergy(-(matingCost1 - subtractedEnergy))) // either add energy back if subtracted too much
																		// or subtract more
				return;
			if (!mate.critter.changeEnergy(-(matingCost2 - mate.critter.subtractedEnergy())))
				return;
			Random r = new Random();
			Memory offspringMem;
			if (r.nextBoolean()) {
				offspringMem = new Memory(this.memory.size());
				offspringMem.memArray[0] = this.memory.memArray[0];
				offspringMem.memArray[1] = this.memory.memArray[1];
				offspringMem.memArray[2] = this.memory.memArray[2];
			} else {
				offspringMem = new Memory(mate.critter.getMemory().length);
				offspringMem.memArray[0] = mate.critter.getMemory(0);
				offspringMem.memArray[1] = mate.critter.getMemory(1);
				offspringMem.memArray[2] = mate.critter.getMemory(2);
			}
			offspringMem.memArray[4] = INITIAL_ENERGY;
			offspringMem.memArray[3] = 1;
			offspringMem.memArray[6] = 0;

			for (int i = 7; i < offspringMem.size(); i++) {
				offspringMem.memArray[i] = 0;
			}
			Program newGenome = new ProgramImpl();
			Program mom = ((Program) this.genome.clone()).mutate(); // mutation in dna replication
			Program dad = ((Program) mate.critter.getProgram().clone()).mutate();
			int size = r.nextBoolean() ? mom.getChildren().size() : dad.getChildren().size();
			List<Node> children = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				if (i >= mom.getChildren().size()) {
					for (int j = i; j < size; j++) {
						children.add(dad.getChildren().get(j));
					}
					break;
				} else if (i >= dad.getChildren().size()) {
					for (int j = i; j < size; j++) {
						children.add(mom.getChildren().get(j));
					}
					break;
				}
				if (r.nextBoolean()) {
					children.add(mom.getChildren().get(i));
				} else {
					children.add(dad.getChildren().get(i));
				}
			}
			newGenome.setChildren(children);
			String nameSpecies = this.species.equals(mate.critter.getSpecies()) ? this.species
					: this.species + " " + mate.critter.getSpecies();
			Critter offspring = new CritterImpl(nameSpecies, offspringMem.memArray, newGenome);
			WorldImpl.Vector at = placeBehind(r.nextBoolean() ? this.crtTile : mate);
			if (at == null)
				return;
			world.setNewlyCreatedCritter(at.col, at.row, r.nextInt(6), offspring);
		}
		subtractedEnergy = memory.getSize();
		if (!changeEnergy(-subtractedEnergy)) // mating failed, subtract failure energy and does not continue if die
			return;
	}

	/**
	 * Place the given critter tile behind the critter
	 * 
	 * @param crt critterTile to be placed
	 * @return coordinate of location
	 */
	private WorldImpl.Vector placeBehind(WorldImpl.CritterTile crt) {
		WorldImpl.Vector b = crt.worldCoordinate.vectorSum(behindDirection(crt.critterOrientation).displacement);
		if (getNearby(3) != 0) {
			b = null;
			// TODO choose any valid hex nearby
		}
		return b;
	}

	@Override
	public Program getProgram() {
		return genome;
	}

	/** Represents a critter's memory array */
	private class Memory {
		public int[] memArray; // inv: length >= MIN_MEM

		/**
		 * Gets the size of the memory array
		 * 
		 * @return size
		 */
		public int size() {
			return memArray.length;
		}

		/**
		 * Constructs a memory array with given size
		 * 
		 * @param size size of array
		 */
		public Memory(int size) {
			memArray = new int[size];
		}

		/**
		 * Returns the value in memory's size field
		 * 
		 * @return size
		 */
		public int getSize() {
			return memArray[3];
		}

		/**
		 * Returns energy
		 * 
		 * @return energy
		 */
		public int getEnergy() {
			return memArray[4];
		}

		/**
		 * Returns pass
		 * 
		 * @return pass
		 */
		public int getPass() {
			return memArray[5];
		}

		/**
		 * Returns posture
		 * 
		 * @return posture
		 */
		public int getPosture() {
			return memArray[6];
		}

		/**
		 * Returns defense
		 * 
		 * @return defense
		 */
		public int getDefense() {
			return memArray[1];
		}

		/**
		 * Returns offense
		 * 
		 * @return offense
		 */
		public int getOffense() {
			return memArray[2];
		}

		/**
		 * Returns memory length as stored in memory array
		 * 
		 * @return stored mem length
		 */
		public int getMemLen() {
			return memArray[0];
		}

		/**
		 * Sets the given index of memory array to the value
		 * 
		 * @param i index of memory array
		 * @param v value to be set
		 */
		public void set(int i, int v) {
			if (i < 0 || i > size())
				return;
			memArray[i] = v;
		}
	}

	@Override
	public int getID() {
		return ID;
	}

}
