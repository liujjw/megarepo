package helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import distributedView.CreateCritter;
import distributedView.CritterPosition;
import distributedView.CritterWorldClient;
import model.Constants;
import model.WorldImpl.Vector;

/** A class to hold all potential helpers needed in any classes */
public class Helpers {
	private static final Random rand = new Random();

	/**
	 * Converts file to a single string
	 * 
	 * @param file file to be read from (should be text file)
	 * @return String contents of file. May return null if IO exception occurs.
	 */
	public static String fileToString(File file) {
		try {
			BufferedReader r;
			r = new BufferedReader(new FileReader(file));
			StringBuffer s = new StringBuffer();
			String line;
			while ((line = r.readLine()) != null) {
				s.append(line + "\n");
			}
			r.close();
			return s.toString();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Converts inputstream to a single string
	 * 
	 * @param in inputstream to be read from (should contain text)
	 * @return String contents of file. May return null if IO exception occurs.
	 */
	public static String fileToString(InputStream in) {
		try {
			BufferedReader r;
			r = new BufferedReader(new InputStreamReader(in));
			StringBuffer s = new StringBuffer();
			String line;
			while ((line = r.readLine()) != null) {
				s.append(line + "\n");
			}
			r.close();
			return s.toString();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Gets a string representation of a random world with name "Default Random
	 * World," size determined by Constants file, and randomly populated rocks.
	 * 
	 * @return String representation of random new world
	 */
	public static String getRandomWorld() {
		int height = 3 + rand.nextInt(30);
		int width = 3 + rand.nextInt(30);
		StringBuffer description = new StringBuffer(
				"name Default Random World\n" + "size " + height + " " + width + "\n");
		int numRocks = rand.nextInt(Math.max(height * width / 25, 4));
		Set<Vector> locs = new HashSet<Vector>();
		locs.add(new Vector(-1, -1));
		for (int j = 0; j < numRocks; j++) {
			int row = rand.nextInt(width);
			int col = rand.nextInt(height);
			if (!(col >= width || 2 * row - col < 0 || 2 * row - col >= height || col < 0 || row < 0)
					&& !locs.contains(new Vector(col, row))) {
				locs.add(new Vector(col, row));
				description.append("rock " + row + " " + col + "\n");
			}
		}
		return description.toString();
	}

	/**
	 * Spawns a critter with given information on the server referred to by client.
	 * 
	 * @param client client to send request
	 * @param file   file of critter
	 * @param num    number of critters to spawn on server
	 * @param row    row to spawn (only looked at if num = 1, -1 indicates random)
	 * @param col    col to spawn (only looked at if num = 1, -1 indicates random)
	 * @return error string if unsuccessful, null if successful
	 */
	public static String spawnCritterOnServer(CritterWorldClient client, File file, int num, int row, int col) {
		String error = "Could not parse critter file:\n";
		try {
			FileReader fr = new FileReader(file.getAbsolutePath());
			BufferedReader r = new BufferedReader(fr);
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
							error += "Species did not occur first. Still using value\n";
						break;
					case "memsize":
						memsize = Integer.parseInt(lineElems[1]);
						if (memsize < Constants.MIN_MEMORY) {
							error += "Cannot have memory size less than 7. Using default value 7 instead.\n";
							memsize = Constants.MIN_MEMORY;
						}
						if (ln != 2)
							error += "Memsize did not occur second. Still using value\n";
						break;
					case "defense":
						defense = Integer.parseInt(lineElems[1]);
						if (ln != 3)
							error += "Defense did not occur third. Still using value\n";
						break;
					case "offense":
						offense = Integer.parseInt(lineElems[1]);
						if (ln != 4)
							error += "Offense did not occur fourwth. Still using value\n";
						break;
					case "size":
						size = Integer.parseInt(lineElems[1]);
						if (ln != 5)
							error += "Size did not occur fifth. Still using value\n";
						break;
					case "energy":
						energy = Integer.parseInt(lineElems[1]);
						if (ln != 6)
							error += "Energy did not occur sixth. Still using value\n";
						break;
					case "posture":
						posture = Integer.parseInt(lineElems[1]);
						if (posture < 0 || posture > 99) {
							error += "Posture " + posture + " out of bounds. Using 0 instead.\n";
							posture = 0;
						}
						if (ln != 7)
							error += "Posture did not occur seventh. Still using value\n";
						break;
					default:
						r.reset();
						inProgram = true;
						break;
					}
					r.mark(300); // marks the end of the previous line to be able to return here
									// 300 should be enough characters for any non-program lines
				} catch (IndexOutOfBoundsException e) {
					error += "Not enough information stored in line, using default value instead.\n";
				} catch (NumberFormatException e) {
					error += "Could not read integer from line, using default value instead.\n";
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
			StringBuffer buff = new StringBuffer();
			while ((line = r.readLine()) != null) {
				buff.append(line + "\n");
			}
			r.close();
			CreateCritter crit;
			if (num == 1 && row > 0 && col > 0) {
				LinkedList<CritterPosition> ps = new LinkedList<CritterPosition>();
				ps.add(new CritterPosition(row, col, rand.nextInt(6)));
				JsonElement positions = new Gson().toJsonTree(ps, CritterPosition.class);
				System.out.println(positions);
				crit = new CreateCritter(species, buff.toString(), memory, positions, null);
			} else {
				crit = new CreateCritter(species, buff.toString(), memory, null, num);
			}
			int status = client.newCrit(crit);
			error = "(server) Error " + status + ": ";
			switch (status) {
			case 201:
				return null;
			case 401:
				error += "Unauthorized to create a critter.";
				break;
			default:
				error += "Critter information in invalid format, could not create.";
				break;
			}
			throw new IOException();
		} catch (IOException e) {
			return error;
		}
	}

}
