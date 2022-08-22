package ClientTests;

import distributedView.CritterWorldClient;
import distributedView.CritterWorldServer;
import view.CritterWorld;

public class RunServer {
    public static void main(String[] args) {
        CritterWorldServer c = new CritterWorldServer(8080, "r", "w", "a");
        c.run();
        CritterWorldClient c2 = new CritterWorldClient("http://0.0.0.0:8080/");
        c2.login("Admin", "a");
        c2.newWorld("name world\nsize 20 20\nrock 4 4\nrock 7 7");
    }
}
