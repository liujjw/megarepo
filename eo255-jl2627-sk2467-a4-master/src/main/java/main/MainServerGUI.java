package main;

import distributedView.CritterWorldServer;
import view.DistributedCritterWorld;

/**
 * The main class of our distributed view simulation. Can either launch GUI or
 * server based on command line arguments
 */
public class MainServerGUI {
	/** Launches either the GUI or the server depending on command line arguments */
	public static void main(String[] args) {
		if (args.length == 0) {
			DistributedCritterWorld.main(args);
		} else {
			if (args.length != 4) {
				System.err.println(
						"(Server) Error: Need to specify exactly 4 arguments to launch server: port, readPassword, writePassword, and adminPassword.");
				return;
			}
			int port;
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.err.println("(Server) Error: Port " + args[0] + " not a valid port number.");
				return;
			}
			String readPassword = args[1];
			String writePassword = args[2];
			String adminPassword = args[3];

			CritterWorldServer server = new CritterWorldServer(port, readPassword, writePassword, adminPassword);
			System.out.println("Launching on port " + port + "...");
			server.run();
		}
	}
}
