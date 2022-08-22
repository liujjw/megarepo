package distributedView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import model.ClientCritter;
import model.ClientWorld;

/** The CritterWorldClient */
public class CritterWorldClient {

	// For now we will work on dummy stub without GUI
	private Gson gson = new Gson();
	private int SessionID;
	private final String Server;

	public CritterWorldClient(String server) {
		this.Server = server;
	}

	/**
	 * Logs client into the server and retrieves client sessionid
	 * 
	 * @param level    - String representing the access rights of client ( "read",
	 *                 "write", or "Admin")
	 * 
	 * @param password - String representing password for server at that access
	 *                 level
	 * 
	 * @return int Response Code
	 * 
	 *         Note: 400 = Bad request, 401 = Unauthorized, 404 = NotFound, 406 =
	 *         Not acceptable
	 */

	public int login(String level, String password) {
		try {
			String myurl = Server + "/login";
			URL url = new URL(myurl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true); // send a POST message
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			PrintWriter w = new PrintWriter(connection.getOutputStream());
			// read write admin
			// bilbo, frodo, and gandalf, respectively

			LoginInfo msg = new LoginInfo(level, password);
			w.write(gson.toJson(msg));
			w.flush();

			int ResponseCode = connection.getResponseCode();
			if (ResponseCode == 200) {
				BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder sb = new StringBuilder();

				for (String l = r.readLine(); l != null; l = r.readLine()) {
					sb.append(l + "\n");
				}

				String textOut = sb.toString();
				LoginComplete loginDetails = gson.fromJson(textOut, LoginComplete.class);
				SessionID = loginDetails.session_id;
			}

			return ResponseCode;

		} catch (IOException e) {
			System.err.println("IO exception: " + e.getMessage());
		}

		return 404;
	}

	/**
	 * Get the list of critter objects from the server
	 * 
	 * @return the critter list
	 */

	public ClientCritter[] listCritters() {
		try {
			String myurl = Server + "/critters?session_id=" + String.valueOf(SessionID);
			URL url = new URL(myurl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();

			int status = connection.getResponseCode();
			if (status != 200) {
				return null;
			}

			BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder sb = new StringBuilder();

			for (String l = r.readLine(); l != null; l = r.readLine()) {
				sb.append(l + "\n");
			}

			String textOut = sb.toString();
			CritterInfo[] critList = gson.fromJson(textOut, CritterInfo[].class);
			ClientCritter[] clientCritList = new ClientCritter[critList.length];
			for (int i = 0; i < clientCritList.length; i++)
				clientCritList[i] = ClientCritter.toClientCritter(critList[i]);
			return clientCritList;

		} catch (IOException e) {
			System.err.println("IO exception: " + e.getMessage());
		}
		return null;
	}

	/**
	 * Retrieve full world state from server
	 * 
	 * @param from_row int Row that the subsection starts from, inclusive
	 * 
	 * @param from_col int Column that the subsection starts from, inclusive
	 * 
	 * @param height   int Height of the subsection
	 * 
	 * @param width    int Width of the subsection
	 * 
	 * @return ClientWorld -> the data of the world within the subsection
	 */

	public ClientWorld loadWorldState() {
		try {
			String myurl = Server + "/world?session_id=" + String.valueOf(SessionID);
			URL url = new URL(myurl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			int status = connection.getResponseCode();
			if (status != 200) {
				return null;
			}
			BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder sb = new StringBuilder();

			for (String l = r.readLine(); l != null; l = r.readLine()) {
				sb.append(l + "\n");
			}
			String textOut = sb.toString();

			WorldInfo LoadedWorldState = gson.fromJson(textOut, WorldInfo.class);

			ClientWorld myworld = ClientWorld.toClientWorld(LoadedWorldState);

			JsonArray arr = LoadedWorldState.state.getAsJsonArray();
			for (int i = 0; i < arr.size(); i++) {
				TerrainInfo tileInfo = gson.fromJson(arr.get(i), TerrainInfo.class);
				myworld.setTerrain(tileInfo.row, tileInfo.col, tileInfo);
			}

			return myworld;
		} catch (IOException e) {
			System.err.println("IO exception: " + e.getMessage());
		}
		return null;
	}

	/**
	 * Retrieve subsection of world form server
	 * 
	 * @param from_row int Row that the subsection starts from, inclusive
	 * 
	 * @param from_col int Column that the subsection starts from, inclusive
	 * 
	 * @param height   int Height of the subsection
	 * 
	 * @param width    int Width of the subsection
	 * 
	 * @return WorldInfo -> the data of the world within the subsection
	 * 
	 */

	public ClientWorld loadSubsection(int from_row, int from_col, int height, int width) {
		try {
			String myurl = Server + "/world?session_id=" + String.valueOf(SessionID);
			myurl = myurl + "&from_row=" + String.valueOf(from_row);
			myurl = myurl + "&from_col=" + String.valueOf(from_col);
			myurl = myurl + "&height=" + String.valueOf(height);
			myurl = myurl + "&width=" + String.valueOf(width);
			URL url = new URL(myurl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			int status = connection.getResponseCode();
			if (status != 200) {
				return null;
			}
			BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder sb = new StringBuilder();

			for (String l = r.readLine(); l != null; l = r.readLine()) {
				sb.append(l + "\n");
			}
			String textOut = sb.toString();

			WorldInfo LoadedWorldState = gson.fromJson(textOut, WorldInfo.class);
			ClientWorld myworld = ClientWorld.toClientWorld(LoadedWorldState);

			JsonArray arr = LoadedWorldState.state.getAsJsonArray();
			for (int i = 0; i < arr.size(); i++) {
				TerrainInfo tileInfo = gson.fromJson(arr.get(i), TerrainInfo.class);
				myworld.setTerrain(tileInfo.row - from_row, tileInfo.col - from_col, tileInfo);
			}
			return myworld;
		} catch (IOException e) {
			System.err.println("IO exception: " + e.getMessage());
		}
		return null;
	}

	/**
	 * Tell server to create an entity (either rock or food)
	 * 
	 * @param EntityCreation contains object to post
	 * 
	 * @return int ResponseCode
	 * 
	 *         Note: 406 = Not Acceptable, 404 = Not Found, 401 = Unauthorized
	 */

	public int createAnEntity(EntityCreation payload) {
		try {
			String myurl = Server + "/world/create_entity?session_id=" + String.valueOf(SessionID);
			URL url = new URL(myurl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true); // send a POST message
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			PrintWriter w = new PrintWriter(connection.getOutputStream());
			w.write(gson.toJson(payload));
			w.flush();
			return connection.getResponseCode();

		} catch (IOException e) {
			System.err.println("IO exception: " + e.getMessage());
		}
		return 404;
	}

	/**
	 * retrieves critter details using id from server
	 * 
	 * @param id is id of critter
	 * 
	 * @return ClientCritter from server
	 */
	public ClientCritter retrieveACritter(int id) {
		try {
			String myurl = Server + "/critter/" + String.valueOf(id) + "?session_id=" + String.valueOf(SessionID);
			URL url = new URL(myurl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			int status = connection.getResponseCode();
			if (status != 200) {
				return null;
			}
			BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder sb = new StringBuilder();

			for (String l = r.readLine(); l != null; l = r.readLine()) {
				sb.append(l + "\n");
			}

			String textOut = sb.toString();
			CritterInfo curcrit = gson.fromJson(textOut, CritterInfo.class);
			return ClientCritter.toClientCritter(curcrit);

		} catch (IOException e) {
			System.err.println("IO exception: " + e.getMessage());
		}
		return null;
	}

	/**
	 * Delete a critter from server
	 * 
	 * @param id int corresponding to critter that must be deleted
	 * 
	 * @return ResponseCode
	 * 
	 *         Note: 401 = Unauthorized, 404 = not found
	 */

	public int deleteCritter(int id) {
		try {
			String myurl = Server + "/critter/" + String.valueOf(id) + "?session_id=" + String.valueOf(SessionID);
			URL url = new URL(myurl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("DELETE");
			connection.connect();
			return connection.getResponseCode();
		} catch (IOException e) {
			System.err.println("IO exception: " + e.getMessage());
		}

		return 404;
	}

	/**
	 * newCrit creates a new critter on the server
	 * 
	 * @param payload is a CreateCritter containing critter details (to be posted)
	 * 
	 * @return int ResponseCode
	 * 
	 *         Note: 401 = Unauthorized, 404 = Not Found
	 */

	public int newCrit(CreateCritter payload) {
		try {
			String myurl = Server + "/critters?session_id=" + String.valueOf(SessionID);
			URL url = new URL(myurl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true); // send a POST message
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			PrintWriter w = new PrintWriter(connection.getOutputStream());
			w.write(gson.toJson(payload));
			w.flush();
			return connection.getResponseCode();
		} catch (IOException e) {
			System.err.println("IO exception: " + e.getMessage());
		}
		return 404;
	}

	/**
	 * Sends a new world request to the server
	 * 
	 * @param description world description
	 * @return response code
	 */
	public int newWorld(String description) {
		try {
			String myurl = Server + "/world?session_id=" + String.valueOf(SessionID);
			URL url = new URL(myurl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			PrintWriter w = new PrintWriter(connection.getOutputStream());
			String payloadstr = ("{  \"description\": \"" + description + "\"}");
			w.write(payloadstr);
			w.flush();
			return connection.getResponseCode();

		} catch (IOException e) {
			System.err.println("IO exception: " + e.getMessage());
		}

		return 404;
	}

	/**
	 * Advance world (when simulation rate = 0)
	 * 
	 * @param count int representing number of timesteps to advance
	 * 
	 * @return int ResponseCode
	 * 
	 *         Note: 404= Not Found, 406 = Not Acceptable 401 = Unauthorized
	 */
	public int advanceWorld(int count) {
		// count defaults to 1
		String jsonPost = "{ \"count\": " + String.valueOf(count) + "}";

		try {
			String myurl = Server + "/step?session_id=" + String.valueOf(SessionID);
			URL url = new URL(myurl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			PrintWriter w = new PrintWriter(connection.getOutputStream());
			w.write(jsonPost);
			w.flush();
			return connection.getResponseCode();

		} catch (IOException e) {
			System.err.println("IO exception: " + e.getMessage());
		}

		return 404;
	}

	/**
	 * Tells the server to run continuously
	 * 
	 * @param rate The number of timesteps per second
	 * 
	 * @return int Response Code
	 * 
	 *         Note: 404= Not Found, 406 = Not Acceptable 401 = Unauthorized
	 */

	public int runContinuous(double rate) {
		String jsonPost = "{ \"rate\": " + String.valueOf(rate) + "}";
		try {
			String myurl = Server + "/run?session_id=" + String.valueOf(SessionID);
			URL url = new URL(myurl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			PrintWriter w = new PrintWriter(connection.getOutputStream());
			w.write(jsonPost);
			w.flush();
			return connection.getResponseCode();

		} catch (IOException e) {
			System.err.println("IO exception: " + e.getMessage());
		}

		return 404;
	}

	/**
	 * Runs the server simulation, prints session ID to console
	 */
	public void run() {
		login("read", "bilbo");
		System.out.println(SessionID);

	}

	public static void main(final String[] args) {
		CritterWorldClient c = new CritterWorldClient("http://34.66.156.108:8080");
		c.run();
		c.loadWorldState();
	}

	/**
	 * @return server string
	 */
	public String getServer() {
		return Server;
	}

}
