package distributedView;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.threadPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import model.Critter;
import model.ReadOnlyCritter;
import model.ServerModel;
import model.World;
import model.WorldImpl.Vector;

/**
 * The server on which the critterworld model will be held that can be queried
 * and updated by clients
 */

// TODO: check that all locks are unlocked before returning, add checks for JSON throwing errors
public class CritterWorldServer {

	private final Random random = new Random(); // To hold a random object
	private final Gson gson = new Gson(); // Create a new GSON converter
	private final JsonParser parser = new JsonParser();
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock(); // read-write lock for simulation
	private final int port; // specified port
	private final String readPSW; // specified read password
	private final String writePSW; // specified write password
	private final String adminPSW; // specified admin password

	private ServerModel model = new ServerModel();

	private ServerUpdateThread updateThread = new ServerUpdateThread();

	private static int nextCritterID = 1;
	private double rate = 0.0; // rate of world simulation

	/**
	 * A map for storing all clients. This is not the most secure method for doing
	 * so, but security was not part of the project specs. It stores all
	 * session_id's to the user type.
	 */
	private Map<Integer, UserType> clients = new HashMap<Integer, UserType>();
	private final ReentrantReadWriteLock clientLock = new ReentrantReadWriteLock(); // read-write lock for clients

	/**
	 * A map for storing all critter ID's to their creators' sessions
	 */
	private Map<Integer, Integer> critters_sessions = new HashMap<Integer, Integer>();

	/**
	 * Constructs a CritterWorldServer object with a given port and read, write, and
	 * admin passwords
	 * 
	 * @param port          port to run server on
	 * @param readPassword  read password
	 * @param writePassword write password
	 * @param adminPassword admin password
	 */
	public CritterWorldServer(int port, String readPassword, String writePassword, String adminPassword) {
		this.port = port;
		this.readPSW = readPassword;
		this.writePSW = writePassword;
		this.adminPSW = adminPassword;
		nextCritterID = 1;
		updateThread.start();
	}

	public static void main(String[] args) {
//		Gson gson = new Gson();
//		String json = "{\"row\": 2,\"col\": 2,\"type\": \"food\", \"amount\": 3}";
//		EntityCreation entity = gson.fromJson(json, EntityCreation.class);
//		System.out.println(gson.toJson(entity));
//		json = "{\"row\": 2,\"col\": 2,\"type\": \"food\"}";
//		entity = gson.fromJson(json, EntityCreation.class);
//		entity.type = null;
//		System.out.println(gson.toJson(entity));
		int from_row = 0;
		int from_col = 0;
		int height = 15;
		int width = 10;
		int num_rows = 12;
		int num_cols = 10;
		int to_row = from_row + (int) Math.ceil((height + 1) / 2.0);
		System.out.println(to_row);
		int to_col = from_col + width;
		int count = 0;
		for (int r = from_row; r < height; r++) {
			for (int c = from_col; c < to_col; c++) {
				if (!inSubsectionBounds(r, c, from_row, from_col, height, width)) {
					System.out.println(c + " " + r);
					continue;
				}
				count++;
			}
		}
		System.out.println(count);
	}

	/**
	 * Effect: starts running a server
	 */
	public void run() {
		// Make the server run on the specified port
		port(port);
		threadPool(8); // set 8 threads to be able to access this server at the same time, although
						// the number very well could depend on the processor.
		post("/login", (request, response) -> {
			try {
				response.type("application/json");
				String json = request.body();
				LoginInfo loginInfo;
				try {
					loginInfo = gson.fromJson(json, LoginInfo.class);
				} catch (JsonSyntaxException e) {
					response.status(400);
					return new MessageSet(false, "Login information in incorrect format.");
				}
				if (loginInfo == null || !loginInfo.isValid()) {
					response.status(400);
					return new MessageSet(false, "Login information in incorrect format.");
				}
				int id = getNewSessionID();
				UserType type = UserType.getTypeFromString(loginInfo.level);
				if (type == null) {
					response.status(400);
					return new MessageSet(false, "Invalid level.");
				}
				if (!isAuthorized(type, loginInfo.password)) {
					response.status(401);
					return new MessageSet(false, "Invalid credentials.");
				}
				clientLock.writeLock().lock();
				clients.put(id, type);
				clientLock.writeLock().unlock();
				response.status(200);
				return new LoginComplete(id);
			} catch (Exception e) {
				closeLocks();
				response.status(500);
				return "Internal server error";
			}
		}, gson::toJson);

		post("/world", (request, response) -> {
			try {
				response.type("text/plain");
				int sess = getSessionID(request.queryParams("session_id"));
				if (sess == -1) {
					response.status(400);
					return "Invalid session id";
				}
				clientLock.readLock().lock();
				if (clients.get(sess) != UserType.ADMIN) {
					response.status(401);
					clientLock.readLock().unlock();
					return "Client unauthorized to start new world";
				}
				clientLock.readLock().unlock();
				String json = request.body();
				WorldDescription worldDescription;
				try {
					worldDescription = gson.fromJson(json, WorldDescription.class);
				} catch (JsonSyntaxException e) {
					response.status(400);
					return "Json in incorrect format";
				}
				if (worldDescription == null || !worldDescription.isValid()) {
					response.status(400);
					return "Json in incorrect format";
				}
				rwl.writeLock().lock();
				boolean b;
				double prevRate = rate;
				try {
					rate = 0;
					b = model.loadWorldFromString(worldDescription.description);
				} catch (Exception e) {
					response.status(400);
					rwl.writeLock().unlock();
					return "Could not load world from string";
				}
				rwl.writeLock().unlock();
				if (!b) {
					rate = prevRate;
					response.status(406);
					return "Unable to read world from string.";
				}

				response.status(201);
				return "Ok";
			} catch (Exception e) {
				closeLocks();
				response.status(500);
				return "Internal server error";
			}
		});

		get("/critters", (request, response) -> {
			try {
				response.type("application/json");
				int sess = getSessionID(request.queryParams("session_id"));
				if (sess == -1) {
					response.status(400);
					return gson.toJson(new MessageSet(false, "Invalid session ID."));
				}
				if (model.world == null) {
					response.status(404);
					return gson.toJson(new MessageSet(false, "No world set, please start a new one."));
				}
				clientLock.readLock().lock();
				UserType type = clients.get(sess);
				clientLock.readLock().unlock();
				rwl.readLock().lock();
				Map<Integer, Critter> critIDMap = model.world.getCritterIDMap();
				List<CritterInfo> allCrits = new LinkedList<CritterInfo>();
				for (Map.Entry<Integer, Critter> entry : critIDMap.entrySet()) {
					Critter critter = entry.getValue();
					CritterInfo adding = CritterInfo.toCritterInfo(critter);
					Integer createdSess = critters_sessions.get(entry.getKey());
					if (type != UserType.ADMIN && createdSess != null && createdSess != sess) {
						adding.program = null;
						adding.recently_executed_rule = null;
					}
					allCrits.add(adding);
				}
				rwl.readLock().unlock();

				response.status(200);
				return gson.toJsonTree(allCrits);
			} catch (Exception e) {
				closeLocks();
				response.status(500);
				return "Internal server error";
			}
		}); // No transformer needed: already converted

		post("/critters", (request, response) -> {
			try {
				response.type("application/json");
				int sess = getSessionID(request.queryParams("session_id"));
				if (sess == -1) {
					response.status(400);
					return new MessageSet(false, "Invalid session ID.");
				}
				if (model.world == null) {
					response.status(404);
					return new MessageSet(false, "No world set, please start a new one.");
				}
				clientLock.readLock().lock();
				UserType type = clients.get(sess);
				clientLock.readLock().unlock();
				if (type == UserType.READ) {
					response.status(401);
					return new MessageSet(false, "Unauthorized to create new critter.");
				}
				String json = request.body();
				CreateCritter createCritter;
				try {
					createCritter = gson.fromJson(json, CreateCritter.class);
				} catch (JsonSyntaxException e) {
					response.status(400);
					return new MessageSet(false, "Critter information in incorrect format.");
				}
				if (!createCritter.isValid()) {
					response.status(400);
					return "Json in incorrect format";
				}
				boolean byPos = createCritter.positions != null;
				if (byPos && createCritter.num != null) {
					response.status(400);
					return new MessageSet(false, "Cannot specify both critter positions and number.");
				}
				String species = createCritter.species_id;
				if (species == null)
					species = "New species " + nextCritterID;
				List<Integer> ids = new ArrayList<Integer>();
				if (!byPos) {
					rwl.readLock().lock();
					ids = model.loadCrittersFromString(species, createCritter.mem, createCritter.program,
							createCritter.num);
					rwl.readLock().unlock();
					for (int id : ids)
						critters_sessions.put(id, sess);
				} else {
					try {
						JsonArray arr = createCritter.positions.getAsJsonArray();
						rwl.readLock().lock();
						for (int i = 0; i < arr.size(); i++) {
							CritterPosition critterPosition;
							try {
								critterPosition = gson.fromJson(arr.get(i), CritterPosition.class);
							} catch (JsonSyntaxException e) {
								response.status(400);
								rwl.readLock().unlock();
								return new MessageSet(false, "Critter positions in incorrect format.");
							}
							int id = model.loadOneCritterFromString(species, createCritter.mem, createCritter.program,
									critterPosition.row, critterPosition.col, critterPosition.direction);
							if (id != -1) {
								ids.add(id);
								critters_sessions.put(id, sess);
							}
						}
						rwl.readLock().unlock();
					} catch (Exception e) {
						response.status(400);
						rwl.readLock().unlock();
						return new MessageSet(false, "Positions in incorrect format.");
					}
				}
				if (ids == null) {
					response.status(406);
					return new MessageSet(false, "Unable to load critter(s) with supplied information.");
				}
				response.status(201);
				return new SpeciesIDs(species, toIntArray(ids));
			} catch (Exception e) {
				closeLocks();
				response.status(500);
				return "Internal server error";
			}
		}, gson::toJson);

		get("/critter/:id", (request, response) -> {
			try {
				response.type("application/json");
				int crit_id;
				try {
					crit_id = Integer.parseInt(request.params(":id"));
				} catch (NumberFormatException e) {
					response.status(400);
					return new MessageSet(false, "Invalid critter id.");
				}
				int sess = getSessionID(request.queryParams("session_id"));
				if (sess == -1) {
					response.status(400);
					return new MessageSet(false, "Invalid session id.");
				}
				if (model.world == null) {
					response.status(404);
					return new MessageSet(false, "No world set, please start a new one.");
				}
				clientLock.readLock().lock();
				UserType type = clients.get(sess);
				clientLock.readLock().unlock();

				rwl.readLock().lock();
				Critter critter = model.world.getCritterByID(crit_id);
				if (critter == null) {
					response.status(404);
					rwl.readLock().unlock();
					return new MessageSet(false, "Could not find critter with given ID");
				}
				rwl.readLock().unlock();

				CritterInfo info = CritterInfo.toCritterInfo(critter);
				clientLock.readLock().lock();

				Integer createdSess = critters_sessions.get(crit_id);
				if (type != UserType.ADMIN && (createdSess == null || createdSess != sess)) {
					info.program = null;
					info.recently_executed_rule = null;
				}
				clientLock.readLock().unlock();
				response.status(200);
				return info;
			} catch (Exception e) {
				closeLocks();
				response.status(500);
				return "Internal server error";
			}
		}, gson::toJson);

		delete("/critter/:id", (request, response) -> {
			try {
				response.type("plain/text");
				int crit_id;
				try {
					crit_id = Integer.parseInt(request.params(":id"));
				} catch (NumberFormatException e) {
					response.status(400);
					return "Invalid critter id.";
				}
				int sess = getSessionID(request.queryParams("session_id"));
				if (sess == -1) {
					response.status(400);
					return "Invalid session id.";
				}
				if (model.world == null) {
					response.status(404);
					return new MessageSet(false, "No world set, please start a new one.");
				}
				clientLock.readLock().lock();
				UserType type = clients.get(sess);
				Integer createdSess = critters_sessions.get(crit_id);
				if (type != UserType.ADMIN && (createdSess == null || createdSess != sess)) {
					response.status(401);
					clientLock.readLock().unlock();

					return "Unauthorized to delete critter.";
				}
				clientLock.readLock().unlock();

				rwl.writeLock().lock();
				Critter critter = model.world.getCritterByID(crit_id);
				if (critter == null) {
					response.status(404);
					return "Could not find critter with given id";
				}
				model.removeCritter(critter);
				rwl.writeLock().unlock();
				response.status(204);
				return "";
			} catch (Exception e) {
				closeLocks();
				response.status(500);
				return "Internal server error";
			}
		});

		post("/world/create_entity", (request, response) -> {
			try {
				response.type("text/plain");
				int sess = getSessionID(request.queryParams("session_id"));
				if (sess == -1) {
					response.status(400);
					return "Invalid session id.";
				}
				if (model.world == null) {
					response.status(404);
					return new MessageSet(false, "No world set, please start a new one.");
				}
				clientLock.readLock().lock();
				UserType userType = clients.get(sess);
				clientLock.readLock().unlock();
				if (userType == UserType.READ) {
					response.status(401);
					return "Unauthorized";
				}
				String json = request.body();
				EntityCreation entity;
				try {
					entity = gson.fromJson(json, EntityCreation.class);
				} catch (JsonSyntaxException e) {
					response.status(400);
					return "Entity in incorrect format.";
				}
				if (entity == null || !entity.isValid()) {
					response.status(400);
					return "Unable to parse JSON as entity";
				}
				int i;
				try {
					rwl.readLock().lock();
					i = model.getReadOnlyWorld().getTerrainInfo(entity.col, entity.row);
					rwl.readLock().unlock();
				} catch (IllegalArgumentException e) {
					response.status(400);
					rwl.readLock().unlock();
					return "World coordinate out of bounds";
				}
				if (i != 0) {
					response.status(406);
					return "Location already occupied: terrain info " + i;
				}
				rwl.writeLock().lock();
				if (entity.type.equals("rock"))
					model.world.setRock(entity.col, entity.row);
				else
					model.world.setFood(new Vector(entity.col, entity.row), entity.amount);
				rwl.writeLock().unlock();
				response.status(201);
				return "Ok";
			} catch (Exception e) {
				closeLocks();
				response.status(500);
				return "Internal server error";
			}

		});

		// TODO
		// Could be either subsection or whole world
		get("/world", (request, response) -> {
			try {
				response.type("application/json");
				int sess = getSessionID(request.queryParams("session_id"));
				if (sess == -1) {
					response.status(400);
					return new MessageSet(false, "Invalid session ID.");
				}
				if (model.world == null) {
					response.status(404);
					return new MessageSet(false, "No world set, please start a new one.");
				}
				response.status(200);
				if (request.queryParams().size() == 1) {
					rwl.readLock().lock();
					WorldInfo wi = toWorldInfo(model.world, sess, 0, 0, model.world.getHeight(),
							model.world.getWidth());
					rwl.readLock().unlock();
					return wi;
				}
				try {
					int from_row = Integer.parseInt(request.queryParams("from_row"));
					int from_col = Integer.parseInt(request.queryParams("from_col"));
					int height = Integer.parseInt(request.queryParams("height"));
					int width = Integer.parseInt(request.queryParams("width"));
					if (height < 0 || width < 0) {
						response.status(400);
						return new MessageSet(false, "Invalid height or width of subection.");
					}
					rwl.readLock().lock();
					WorldInfo wi = toWorldInfo(model.world, sess, from_row, from_col, height, width);
					rwl.readLock().unlock();
					return wi;
				} catch (NullPointerException | NumberFormatException e) {
					response.status(400);
					return new MessageSet(false, "Invalid query string.");
				}
			} catch (Exception e) {
				closeLocks();
				response.status(500);
				return "Internal server error";
			}
		}, gson::toJson);

		post("/step", (request, response) -> {
			try {
				response.type("text/plain");
				int sess = getSessionID(request.queryParams("session_id"));
				if (sess == -1) {
					response.status(400);
					return "Invalid session id.";
				}
				if (model.world == null) {
					response.status(404);
					return new MessageSet(false, "No world set, please start a new one.");
				}
				clientLock.readLock().lock();
				UserType userType = clients.get(sess);
				clientLock.readLock().unlock();
				if (userType == UserType.READ) {
					response.status(401);
					return "Unauthorized";
				}
				if (rate != 0) {
					response.status(406);
					return "Cannot step the simulation when proceeding at rate " + rate;
				}
				int count = -1;
				try {
					JsonObject elem = parser.parse(request.body()).getAsJsonObject();
					count = elem.get("count").getAsInt();
				} catch (Exception e) {
					response.status(400);
					return "Could not parse JSON.";
				}
				stepWorld(count);
				response.status(200);
				return "Ok";
			} catch (Exception e) {
				closeLocks();
				response.status(500);
				return "Internal server error";
			}
		});

		post("/run", (request, response) -> {
			try {
				response.type("text/plain");
				int sess = getSessionID(request.queryParams("session_id"));
				if (sess == -1) {
					response.status(400);
					return "Invalid session id.";
				}
				if (model.world == null) {
					response.status(404);
					return new MessageSet(false, "No world set, please start a new one.");
				}
				clientLock.readLock().lock();
				UserType userType = clients.get(sess);
				clientLock.readLock().unlock();
				if (userType == UserType.READ) {
					response.status(401);
					return "Unauthorized";
				}
				double newRate = -1;
				try {
					JsonObject elem = parser.parse(request.body()).getAsJsonObject();
					newRate = elem.get("rate").getAsDouble();
				} catch (JsonParseException | NullPointerException | ClassCastException e) {
					response.status(400);
					return "Could not parse JSON.";
				}
				if (newRate < 0) {
					response.status(406);
					return "Cannot set rate less than 0.";
				}
				rate = newRate;

				if (rate == 0)
					updateThread.shouldEnd();
				else {
					if (!updateThread.isAlive()) {
						updateThread = new ServerUpdateThread();// (new ServerModelUpdate());
						updateThread.start();
					}
				}
				response.status(200);
				return "Ok";
			} catch (Exception e) {
				closeLocks();
				response.status(500);
				return "Internal server error";
			}
		});

		// A nice error message to show the user that someone is there
		get("/*", (request, response) -> {
			response.type("text/html");
			response.status(404);
			return "<h1>404 not found</h1>\n<h2>Hello! Welcome to our critterworld.</h2> \n\nIt seems you do not know how to use this very well... \n\t(please see the API https://cs2112fall2019.docs.apiary.io/ to know what to do)";
		});

	}

	/**
	 * Attempts to open all read or write locks.
	 */
	private void closeLocks() {
		try {
			rwl.writeLock().unlock();
		} catch (IllegalMonitorStateException e) {
		}
		try {
			rwl.readLock().unlock();
		} catch (IllegalMonitorStateException e) {
		}
		try {
			clientLock.writeLock().unlock();
		} catch (IllegalMonitorStateException e) {
		}
		try {
			clientLock.readLock().unlock();
		} catch (IllegalMonitorStateException e) {
		}
	}

	/**
	 * Steps the world {@code count} number of times in a separate thread
	 * 
	 * @param count number of times to step world
	 */
	public void stepWorld(int count) {
		new Thread() {
			@Override
			public void run() {
				rwl.writeLock().lock();
				model.advanceTime(count);
				rwl.writeLock().unlock();
			}
		}.start();
	}

	/**
	 * Converts a subsection of the world into its equivalent WorldInfo
	 * representation. Treats out-of-bounds hexes as rocks
	 * 
	 * @param world    world to be converted
	 * @param sess     session of client requesting world, so as to know permissions
	 * @param from_row starting row (inclusive)
	 * @param from_col starting col (inclusive)
	 * @param height   height of subsection
	 * @param width    width of subsection
	 * @return WorldInfo representation of {@code world}
	 */
	private WorldInfo toWorldInfo(World world, int sess, int from_row, int from_col, int height, int width) {
		int current_timestep = world.getSteps();
		String name = world.getName();
		int population = world.getNumberOfAliveCritters();
		List<TerrainInfo> terrain = new LinkedList<TerrainInfo>();
		for (int r = from_row; r < height + from_row; r++) {
			for (int c = from_col; c < width + from_col; c++) {
				if (!inSubsectionBounds(r, c, from_row, from_col, height, width)) {
					continue;
				}
				if (!world.inBounds(new Vector(c, r))) {
					terrain.add(new TerrainInfo(r, c, "rock", null, null, null, null, null, null, null)); // rock
					continue;
				}
				TerrainInfo info;
				ReadOnlyCritter critter = world.getReadOnlyCritter(c, r);
				if (critter != null) {
					String program = null;
					Integer recently_executed_rule = null;
					Integer createdSess = critters_sessions.get(critter.getID());
					if (clients.get(sess) == UserType.ADMIN || (createdSess != null && createdSess == sess)) {
						program = critter.getProgramString();
						recently_executed_rule = critter.getLastRuleIndex();
					}
					info = new TerrainInfo(r, c, "critter", null, critter.getID(), critter.getSpecies(), program,
							critter.getDirection(), critter.getMemory(), recently_executed_rule);
				} else {
					int i = world.getTerrainInfo(c, r);
					if (i == -1) { // rock
						info = new TerrainInfo(r, c, "rock", null, null, null, null, null, null, null);
					} else if (i == 0) { // empty
						info = new TerrainInfo(r, c, "nothing", null, null, null, null, null, null, null);
					} else { // -X is (X-1) food
						info = new TerrainInfo(r, c, "food", -1 * i - 1, null, null, null, null, null, null);

					}
				}
				terrain.add(info);
			}
		}
		JsonElement state = gson.toJsonTree(terrain);
		return new WorldInfo(current_timestep, rate, name, population, width, height, state);
	}

	/**
	 * Checks whether the given coordinates are in the bounds of the subsection
	 * 
	 * @param row      row
	 * @param col      col
	 * @param from_row from_row
	 * @param from_col from_col
	 * @param height   height of subsection
	 * @param width    width of subsection
	 * @return whether coordinate (col,row) is in bounds of this subection
	 */

	private static boolean inSubsectionBounds(int row, int col, int from_row, int from_col, int height, int width) {
		row -= from_row;
		col -= from_col;
		return !(col >= width || 2 * row - col < 0 || 2 * row - col >= height || col < 0 || row < 0);
	}

	/**
	 * Converts a list of Integers to an array of ints
	 * 
	 * @param ids Integer list to convert
	 * @return int[] representation of ids
	 */
	private int[] toIntArray(List<Integer> ids) {
		int[] arr = new int[ids.size()];
		int i = 0;
		for (Integer id : ids)
			arr[i++] = id;
		return arr;
	}

	/**
	 * Gets the integer session ID out of a string sess.
	 * 
	 * @param sess string to be converted to session ID
	 * @return session ID integer representation if applicable. Returns -1 if either
	 *         sess not an integer or clients does not contain that session
	 */
	private int getSessionID(String sess) {
		try {
			int i = Integer.parseInt(sess);
			if (!clients.containsKey(i)) {
				return -1;
			}
			return i;
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	/**
	 * Generates a new session ID
	 * 
	 * @return session id not already used
	 */
	private int getNewSessionID() {
		int id = 1 + random.nextInt(1000);
		while (clients.containsKey(id)) {
			id = 1 + random.nextInt(1000);
		}
		return id;
	}

	/**
	 * Gets the next critter ID. Effect: increases the next critter ID
	 * 
	 * @return critter ID
	 */
	public static int getCritterID() {
		return nextCritterID++;
	}

	/**
	 * Checks whether the given user type is authorized for the specified password
	 * 
	 * @param type user type
	 * @param psw  password
	 * @return whether password is correct for user type
	 */
	private boolean isAuthorized(UserType type, String psw) {
		switch (type) {
		case ADMIN:
			return psw.equals(adminPSW);
		case READ:
			return psw.equals(readPSW);
		case WRITE:
			return psw.equals(writePSW);
		default:
			return false;
		}
	}

	/** A specialized thread class for updating the server model */
	class ServerUpdateThread extends Thread {
		private boolean shouldEnd = false; // whether or not the thread should end

		/**
		 * Constructs a ServerUpdateThread
		 */
		public ServerUpdateThread() {
			super();
		}

		/**
		 * Tells the thread to end by setting the local variable flag that keeps it
		 * running to false.
		 */
		public void shouldEnd() {
			shouldEnd = true;
		}

		@Override
		public void run() {
			while (!shouldEnd) {
				if (rate <= 0) {
					break;
				}
				long millisPerUpdate = (long) (1000.0 / rate);
				long start = System.currentTimeMillis();
				rwl.writeLock().lock();
				model.advanceTime(1);
				rwl.writeLock().unlock();
				try {
					long t = millisPerUpdate - (System.currentTimeMillis() - start);
					if (t > 0)
						Thread.sleep(t);
					else {
//						System.out.println("did not sleep");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}