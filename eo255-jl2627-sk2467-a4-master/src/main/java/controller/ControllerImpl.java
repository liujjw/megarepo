package controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ast.Program;
import model.Constants;
import model.Critter;
import model.CritterImpl;
import model.ModelFactory;
import model.ReadOnlyWorld;
import model.World;
import model.WorldImpl.Vector;
import parse.Parser;
import parse.ParserFactory;

/** A class controlling the simulation that implements Controller methods */
public class ControllerImpl implements Controller {
	public World world; // Invariant: world not null
	private final Random rand = new Random(); // Invariant: not null
	private PrintStream err; // Invariant: not null

	/**
	 * Constructs a ControllerImpl object that prints errors to the console.
	 */
	public ControllerImpl() {
		err = System.err;
	}

	/**
	 * Constructs a ControllerImpl object that prints errors to the given
	 * printstream
	 * 
	 * @param p printStream to be printed to
	 */
	public ControllerImpl(PrintStream p) {
		err = p;
	}

	@Override
	public void setPrintStream(PrintStream p) {
		err = p;
	}

	@Override
	public ReadOnlyWorld getReadOnlyWorld() {
		return (ReadOnlyWorld) world;
	}

	@Override
	public void newWorld() {
		world = ModelFactory.getDefaultWorld();
		int numRocks = rand.nextInt(world.getWidth() * world.getHeight() / 15);
		for (int i = 0; i < numRocks; i++) {
			Vector loc = world.getRandomLocation();
			if (loc.col != -1 && loc.row != -1)
				world.setRock(loc.col, loc.row);
		}
	}

	@Override
	public boolean loadWorld(String filename) {
		try {
			readWorld(getReader(filename), filename);
			return true;
		} catch (IOException e) {
			err.println("Error: could not find world file at " + filename);
			return false;
		} catch (IndexOutOfBoundsException e) {
			err.println("Error: world file in invalid format.");
			return false;
		}
	}

	public boolean loadWorldFromString(String s) {
		try {
			InputStream inputStream = new ByteArrayInputStream(s.getBytes("UTF-8"));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			readWorld(bufferedReader, null);
			return true;
		} catch (IOException e) {
			err.println("Error: could not read specified world");
			return false;
		} catch (IndexOutOfBoundsException e) {
			err.println("Error: world in invalid format.");
			return false;
		}
	}

	/**
	 * Gets a reader from the file at the specified location
	 * 
	 * @param filename location of file to be found
	 * @return BufferedReader reading from specified file
	 * @throws IOException if file at given location can not be found
	 */
	private BufferedReader getReader(String filename) throws IOException {
		FileReader fr = new FileReader(filename);
		BufferedReader r = new BufferedReader(fr);
		return r;
	}

	/**
	 * For use with testing from a resources folder. Classloader functionality (not
	 * in use).
	 *
	 * Original method works with absolute paths, ie we want any file anywhere to
	 * load.
	 */
	private BufferedReader getReader_t(String filename) {
		// only works if file is chosen in src/main/resources
		InputStream in = ClassLoader.getSystemResourceAsStream(filename);
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		return r;
	}

	/**
	 * Reads and stores world from given reader
	 * 
	 * @param r        BufferedReader to be read from
	 * @param filename name of world file, if applicable. Can be null if reading
	 *                 from string
	 * @throws IOException               if error when reading world file
	 * @throws IndexOutOfBoundsException if a line is in incorrect format
	 */
	private void readWorld(BufferedReader r, String filename) throws IOException, IndexOutOfBoundsException {
		String line = r.readLine();
		int ln = 1;
		if (line == null) {
			world = ModelFactory.getDefaultWorld();
			return;
		}
		String name;
		if (!line.substring(0, 4).equals("name")) {
			name = "New World";
			printError(ln, "No world name specified, using default value instead.", "world");
		} else {
			name = line.substring(5);
			line = r.readLine();
			ln++;
		}

		if (line == null) {
			printError(ln, "Not enough data in specified world file.", "world");
			throw new IOException();
		}

		String[] lineElems = line.split(" ");
		int width = -1, height = -1;
		if (!lineElems[0].equals("size")) {
			printError(ln, "Size of world not specified.", "world");
			throw new IOException();
		} else {
			try {
				width = Integer.parseInt(lineElems[1]);
				height = Integer.parseInt(lineElems[2]);
			} catch (NumberFormatException e) {
				printError(ln, "Invalid size of world width '" + lineElems[1] + "', height '" + lineElems[2] + "'.",
						"world");
				throw new IOException();
			}
			if (width < 0 || height < 0) {
				printError(ln, "Invalid size of world width " + width + ", height " + height + ".", "world");
				throw new IOException();
			}
			world = ModelFactory.getWorld(name, width, height);
		}

		while ((line = r.readLine()) != null) {
			ln++;
			if (line.isBlank())
				continue;
			else if (line.length() < 2)
				continue;
			else if (line.substring(0, 2).equals("//"))
				continue;
			else {
				lineElems = line.split(" ");
				switch (lineElems[0]) {
				case "rock":
					processRock(lineElems, ln);
					break;
				case "food":
					processFood(lineElems, ln);
					break;
				case "critter":
					String rootDirectory = "";
					if (filename != null)
						rootDirectory = filename.substring(0, filename.lastIndexOf("/") + 1);
					processCritter(lineElems, ln, rootDirectory);
					break;
				default:
					printError(ln, "Unsure what kind of object meant to be placed", "world");
					break;
				}
			}
		}
		r.close();
	}

	/**
	 * Handles spawning food at given location or printing error.
	 * 
	 * @param lineElems string arguments in line being read
	 * @param ln        line number being read
	 */
	private void processFood(String[] lineElems, int ln) {
		int column, row, amount;
		try {
			column = Integer.parseInt(lineElems[1]);
			row = Integer.parseInt(lineElems[2]);
		} catch (NumberFormatException | IndexOutOfBoundsException e) {
			printError(ln, "Food did not have valid coordinates, using random location instead.", "world");
			Vector loc = world.getRandomLocation();
			column = loc.col;
			row = loc.row;
			if (column == -1 || row == -1) // then it took too long to find a random location
				return;
		}
		try {
			amount = Integer.parseInt(lineElems[3]);
		} catch (NumberFormatException | IndexOutOfBoundsException e) {
			printError(ln, "Food had invalid amount, using random amount instead.", "world");
			amount = 1 + rand.nextInt(5);
		}

		// Only change if tile is empty
		if (world.getTerrainInfo(column, row) == 0)
			world.changeFood(column, row, amount);

	}

	/**
	 * Handles spawning rock at given location or printing error.
	 * 
	 * @param lineElems string arguments in line being read
	 * @param ln        line number being read
	 */
	private void processRock(String[] lineElems, int ln) {
		int column, row;
		try {
			column = Integer.parseInt(lineElems[1]);
			row = Integer.parseInt(lineElems[2]);
		} catch (NumberFormatException | IndexOutOfBoundsException e) {
			printError(ln, "Rock did not have valid coordinates, using random location instead.", "world");
			Vector loc = world.getRandomLocation();
			column = loc.col;
			row = loc.row;
			if (column == -1 || row == -1)
				return;
		}

		// Only place if empty
		if (world.getTerrainInfo(column, row) == 0)
			world.setRock(column, row);
	}

	/**
	 * Handles spawning critter at given location or printing error.
	 * 
	 * @param lineElems     string arguments in line being read
	 * @param ln            line number being read
	 * @param rootDirectory root directory that critter file is stored in
	 */
	private void processCritter(String[] lineElems, int ln, String rootDirectory) {
		Critter critter;
		try {
			critter = getCritter(rootDirectory + lineElems[1]);
		} catch (IndexOutOfBoundsException e) {
			printError(ln, "Critter did not have enough information to be spawned.", "world");
			return;
		} catch (IOException e) {
			printError(ln, "Critter could not be read from specified file.", "world");
			return;
		}

		int column, row, direction;
		try {
			column = Integer.parseInt(lineElems[2]);
			row = Integer.parseInt(lineElems[3]);
		} catch (NumberFormatException | IndexOutOfBoundsException e) {
			printError(ln, "Critter did not have valid coordinates, using random ones instead.", "world");
			Vector loc = world.getRandomLocation();
			column = loc.col;
			row = loc.row;
			if (column == -1 || row == -1)
				return;
		}
		try {
			direction = Integer.parseInt(lineElems[4]);
		} catch (NumberFormatException | IndexOutOfBoundsException e) {
			printError(ln, "Critter did not have valid direction, using random instead.", "world");
			direction = rand.nextInt(6);
		}

		// Only place it if the hex is empty
		if (world.getTerrainInfo(column, row) == 0)
			world.setNewlyCreatedCritter(column, row, direction, critter);

	}

	/**
	 * Method to format error messages. Prints given error to command line in format
	 * "(type) Error line [line]: [message]".
	 * 
	 * @param ln   line number of error
	 * @param msg  message error to be printed to user
	 * @param type either world or critter
	 */
	private void printError(int ln, String msg, String type) {
		err.println("(" + type + ") Error line " + ln + ": " + msg);
	}

	public static void main(String[] args) {
		ControllerImpl controller = new ControllerImpl();
		try {
			controller.getCritter("example-critter.txt");
		} catch (IOException e) {
			System.out.println("Didnt work");
		}
		try {
			controller.loadWorldFromString("name small world\nsize 10 15");
			System.out.println(controller.getReadOnlyWorld().getWidth());
			System.out.println(controller.getReadOnlyWorld().getHeight());
			System.out.println(controller.getReadOnlyWorld().numRows());
			System.out.println(controller.getReadOnlyWorld().numCols());

		} catch (Exception e) {
			System.out.println("Didnt work");
		}
	}

	/**
	 * Gets and returns the critter object read from the file.
	 * 
	 * @param filename location of critter file
	 * @return Critter object read from given file
	 * @throws IOException if error reading from Critter file
	 */
	public Critter getCritter(String filename) throws IOException {
		BufferedReader r;
		try {
			r = getReader(filename);
		} catch (IOException e) {
//			throw new IOException();
			// If could not be found from direct path, try using system resource.
			try {
				r = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(filename)));
			} catch (NullPointerException e2) {
				throw new IOException();
			}
		}
		String species = "New species";
		int memsize, defense, offense, size, energy, posture;
		memsize = Constants.MIN_MEMORY;
		// The next are random values deemed appropriate for critters
		defense = 1 + rand.nextInt(3);
		offense = 1 + rand.nextInt(3);
		size = 1;
		energy = Constants.INITIAL_ENERGY;
		posture = 0;
		String line;
		int ln = 0;
		boolean inProgram = false;
		while (!inProgram && (line = r.readLine()) != null) {
			// No more than 50 characters should be on a line
			if (line.isBlank() || line.trim().length() < 2 || line.trim().substring(0, 2).equals("//")) {
				continue;
			}
			ln++;
			String[] lineElems = line.split(": ");
			try {
				switch (lineElems[0]) {
				case "species":
					species = lineElems[1];
					if (ln != 1)
						printError(ln, "Species did not occur first. Still using value", "critter");
					break;
				case "memsize":
					memsize = Integer.parseInt(lineElems[1]);
					if (memsize < Constants.MIN_MEMORY) {
						printError(ln, "Cannot have memory size less than 7. Using default value 7 instead.",
								"critter");
						memsize = Constants.MIN_MEMORY;
					}
					if (ln != 2)
						printError(ln, "Memsize did not occur second. Still using value", "critter");
					break;
				case "defense":
					defense = Integer.parseInt(lineElems[1]);
					if (ln != 3)
						printError(ln, "Defense did not occur third. Still using value", "critter");
					break;
				case "offense":
					offense = Integer.parseInt(lineElems[1]);
					if (ln != 4)
						printError(ln, "Offense did not occur fourwth. Still using value", "critter");
					break;
				case "size":
					size = Integer.parseInt(lineElems[1]);
					if (ln != 5)
						printError(ln, "Size did not occur fifth. Still using value", "critter");
					break;
				case "energy":
					energy = Integer.parseInt(lineElems[1]);
					if (ln != 6)
						printError(ln, "Energy did not occur sixth. Still using value", "critter");
					break;
				case "posture":
					posture = Integer.parseInt(lineElems[1]);
					if (posture < 0 || posture > 99) {
						printError(ln, "Posture " + posture + " out of bounds. Using 0 instead.", "critter");
						posture = 0;
					}
					if (ln != 7)
						printError(ln, "Posture did not occur seventh. Still using value", "critter");
					break;
				default:
					r.reset();
					inProgram = true;
					break;
				}
				r.mark(300); // marks the end of the previous line to be able to return here
								// 300 should be enough characters for any non-program lines
			} catch (IndexOutOfBoundsException e) {
				printError(ln, "Not enough information stored in line, using default value instead.", "critter");
			} catch (NumberFormatException e) {
				printError(ln, "Could not read integer from line, using default value instead.", "critter");
			}
		}

		int[] memory = new int[memsize]; // memsize will not be less than Constants.MIN_MEMORY
		memory[0] = memsize;
		memory[1] = defense;
		memory[2] = offense;
		memory[3] = size;
		memory[4] = energy;
		memory[5] = 0; // pass is initialized to 0
		memory[6] = posture;
		Program program = ParserFactory.getParser().parse(r);
		if (program == null) {
			err.println("(critter) Error: invalid critter program for species " + species + ", cannot spawn.");
			throw new IOException();
		}
		Critter cr = ModelFactory.getCritter(species, memory, program);
		return cr;
	}

	public boolean loadOneCritter_t(String filename, int row, int col, int dir) {
		if (row < 0 || col < 0 || row > world.numRows() || col > world.numCols()) {
			err.println("(critter) Error: location of spawning (" + row + "," + col + ") out of bounds.");
			return false;
		}
		if (world.getCritterDirection(col, row) != -1) {
			err.println("(critter) Error: location of spawning (" + row + "," + col
					+ ") currently occupied by critter. Cannot spawn here.");
			return false;
		}
		if (world.getTerrainInfo(col, row) != 0) {
			err.println("(critter) Error: location of spawning (" + row + "," + col
					+ ") currently occupied by food or rock. Cannot spawn here.");
			return false;
		}
		Critter critter;
		try {
			critter = getCritter(filename);
		} catch (IOException e) {
			return false;
		}
		world.setNewlyCreatedCritter(col, row, dir, critter);
		return true;
	}

	@Override
	public boolean loadOneCritter(String filename, int row, int col) {
		if (row < 0 || col < 0 || row > world.numRows() || col > world.numCols()) {
			err.println("(critter) Error: location of spawning (" + row + "," + col + ") out of bounds.");
			return false;
		}
		if (world.getCritterDirection(col, row) != -1) {
			err.println("(critter) Error: location of spawning (" + row + "," + col
					+ ") currently occupied by critter. Cannot spawn here.");
			return false;
		}
		if (world.getTerrainInfo(col, row) != 0) {
			err.println("(critter) Error: location of spawning (" + row + "," + col
					+ ") currently occupied by food or rock. Cannot spawn here.");
			return false;
		}
		Critter critter;
		try {
			critter = getCritter(filename);
		} catch (IOException e) {
			return false;
		}
		world.setNewlyCreatedCritter(col, row, rand.nextInt(6), critter);
		return true;
	}

	@Override
	public int loadOneCritterFromString(String species, int[] mem, String program, int row, int col, int direction) {
		if (row < 0 || col < 0 || row > world.numRows() || col > world.numCols()) {
			err.println("(critter) Error: location of spawning (" + row + "," + col + ") out of bounds.");
			return -1;
		}
		if (world.getCritterDirection(col, row) != -1) {
			err.println("(critter) Error: location of spawning (" + row + "," + col
					+ ") currently occupied by critter. Cannot spawn here.");
			return -1;
		}
		if (world.getTerrainInfo(col, row) != 0) {
			err.println("(critter) Error: location of spawning (" + row + "," + col
					+ ") currently occupied by food or rock. Cannot spawn here.");
			return -1;
		}
		Parser parser = ParserFactory.getParser();
		Program prog = parser.parse(new StringReader(program));
		if (prog == null)
			return -1;
		Critter c = new CritterImpl(species, mem, (Program) prog.clone());
		if (direction == -1)
			direction = rand.nextInt(6);
		world.setNewlyCreatedCritter(col, row, direction, c);
		return c.getID();
	}

	@Override
	public List<Integer> loadCrittersFromString(String species, int[] mem, String program, int num) {
		if (num < 0) {
			err.println("(critter) Error: cannot spawn n=" + num + " critters. Aborting.");
			return null;
		}
		Parser parser = ParserFactory.getParser();
		Program prog = parser.parse(new StringReader(program));
		if (prog == null)
			return null;
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < num; i++) {
			Vector loc = world.getRandomLocation();
			if (loc.col == -1 || loc.row == -1)
				continue;
			Critter c = new CritterImpl(species, mem, (Program) prog.clone());
			world.setNewlyCreatedCritter(loc.col, loc.row, rand.nextInt(6), c);
			ids.add(c.getID());
		}
		return ids;
	}

	@Override
	public boolean loadCritters(String filename, int n) {
		if (n < 0) {
			err.println("(critter) Error: cannot spawn n=" + n + " critters. Aborting.");
			return false;
		}
		Critter critter;
		try {
			critter = getCritter(filename);
		} catch (IOException e) {
			return false;
		}
		for (int i = 0; i < n; i++) {
			Vector loc = world.getRandomLocation();
			if (loc.col == -1 || loc.row == -1)
				continue;
			Critter c = new CritterImpl(critter.getSpecies(), critter.getMemory(),
					(Program) critter.getProgram().clone());
			world.setNewlyCreatedCritter(loc.col, loc.row, rand.nextInt(6), c);
		}
		return true;
	}

	@Override
	public boolean advanceTime(int n) {
		if (world == null || n < 0)
			return false;
		for (int i = 0; i < n; i++)
			world.passTime();
		return true;
	}

	@Override
	public void printWorld(PrintStream out) {
		ReadOnlyWorld wrld = getReadOnlyWorld();
		if (wrld == null) {
			err.println("Error: Trying to print world without having initialized it, aborting.");
			return;
		}
		StringBuffer s = new StringBuffer();
		int width = wrld.getWidth();
		int height = wrld.getHeight();
		for (int i = height - 1; i >= 0; i--) {
			int nInRow;
			int offset;
			if (i % 2 == 0) {
				nInRow = (int) Math.ceil(width / 2.0);
				offset = 0;
			} else {
				nInRow = width / 2;
				offset = 1;
				s.append("  ");// two spaces indentation
			}
			for (int j = 0; j < nInRow; j++) {
				int lCol = offset; // leftmost column coordinate of row
				int lRow = (i + 1) / 2; // leftmost row coordinate
				int c = lCol + 2 * j;
				int r = lRow + j;
				printLoc(s, c, r, wrld, out);
			}
			s.append("\n");
		}
		out.print(s);
	}

	/**
	 * Prints the correct character corresponding to the given location to the
	 * printstream
	 * 
	 * @param c    column of location
	 * @param r    row of location
	 * @param wrld world to be read from
	 * @param out  printstream to be printed to
	 */
	public void printLoc(StringBuffer s, int c, int r, ReadOnlyWorld wrld, PrintStream out) {
		boolean isCritter = false;
		int i = wrld.getCritterDirection(c, r);
		if (i == -1) {
			try {
				i = wrld.getTerrainInfo(c, r);
			} catch (IllegalArgumentException e) {
				// TODO: REMOVE THIS LINE Later
				i = 6; // invalid number
				err.println("Error: invalid location (" + c + "," + r + ") from board");
			}
		} else {
			isCritter = true;
		}

		if (i == 0 && !isCritter)
			s.append("-");
		else if (i == -1)
			s.append("#");
		else if (i < 0)
			s.append("F");
		else if (i >= 0 && i < 6)
			s.append(i);
		else {
			err.println("Error: invalid int i=" + i + " at location (" + c + "," + r + ")");
			return;
		}
		s.append("   "); // three spaces between information
	}

}
