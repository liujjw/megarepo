package foodSensingTest;

import controller.Controller;
import controller.ControllerImpl;
import model.WorldImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static model.CritterImpl.NO_FOOD_SMELL_VAL;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class searchTest {

    /**
     * Test the expression for getting the relative direction of a hextile
     * to a critter's current orientation.
     * */
    @Disabled
    @Test
    void testRelDir() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                int diff = (Math.abs(i-j));
                int relativeDirection =  diff <= 3 ? diff : (diff == 4 ? 2 : 1);
                System.out.println(i + ", " + j + ": " + relativeDirection);
            }
        }
    }

    /******************
     USAGE: change paths to absolute paths of file location in resources folder of this project (classloader not supported) by
     right clicking the file on the left for a copy path of the prefix of a file path.
     *******************/
    // change this prefix
    private static final String a7world_pre = "C:\\Users\\jackj\\IdeaProjects\\eo255-jl2627-sk2467-a4\\src\\test\\resources\\";
    private static final WorldImpl.Vector critLoc = new WorldImpl.Vector(4,8); // critter doesnt move so this is always true
    private static final String smellCrit = "C:\\Users\\jackj\\IdeaProjects\\eo255-jl2627-sk2467-a4\\src\\test\\resources\\smell-critter.txt";


    /**
     * Test based off of A7 food sensing explanation in the spec; ie
     * we try to match the smell values to the ones described.
     * */
    @Test
    void testSmell1() {
        Controller c = new ControllerImpl();
        c.loadWorld(a7world_pre + "a7-world.txt");
        c.loadOneCritter_t(smellCrit, critLoc.row, critLoc.col, 1);
        c.advanceTime(1);
        // food f is at distance 4000
        assertTrue(getLastSmell(c) == 4000, "last smell was: " +
                getLastSmell(c));
    }
    @Test
    void testSmell2() {
        Controller c = new ControllerImpl();
        // return food c or d at 6000 or 6005 if f gone
        c.loadWorld(a7world_pre + "a7-world-no-f.txt");
        c.loadOneCritter_t(smellCrit, critLoc.row, critLoc.col, 1);
        c.advanceTime(1);
        assertTrue(getLastSmell(c) == 6000 ||
                getLastSmell(c) == 6005, "last smell was: " +
                getLastSmell(c));
    }

    @Test
    void testSmell3() {
        Controller c = new ControllerImpl();
        // no cdf, e at 8000
        c.loadWorld(a7world_pre + "a7-world-no-fcd.txt");
        c.loadOneCritter_t(smellCrit, critLoc.row, critLoc.col, 1);
        c.advanceTime(1);
        assertTrue(getLastSmell(c) == 8000, "last smell was: " +
                getLastSmell(c));
    }

    @Test
    void testSmell4() {
        Controller c = new ControllerImpl();
        // no cdfe, rest of the foods are past max-smell-distance
        c.loadWorld(a7world_pre + "a7-world-no-fcde.txt");
        c.loadOneCritter_t(smellCrit, critLoc.row, critLoc.col, 1);
        c.advanceTime(1);
        assertTrue(getLastSmell(c) == NO_FOOD_SMELL_VAL, "last smell was: " +
                getLastSmell(c));
    }

    private int getLastSmell(Controller c){
        return c.getReadOnlyWorld().getReadOnlyCritter(critLoc.col, critLoc.row).getLastSmell();
    }

}
