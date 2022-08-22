package testGUI;

import org.junit.jupiter.api.Test;
import view.CritterWorld;

public class testGUI {
    @Test
    void runManySteps() {
        final int steps = 100000;
        CritterWorld.main(new String[0]);
        // not automated :)
    }

}
