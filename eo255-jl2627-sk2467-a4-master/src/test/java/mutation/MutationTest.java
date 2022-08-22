package mutation;

import ast.Mutation;
import ast.MutationFactory;
import ast.Program;
import org.junit.jupiter.api.*;
import parse.Parser;
import parse.ParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MutationTest {
    private static final String UNMUTATED_PROGRAM = "parser/unmutated_critter.txt";
    private static final String SMALL_UNMUTATED_PROGRAM = "parser/draw_critter.txt";

    private static Program getProgram(String path) {
        InputStream in = ClassLoader.getSystemResourceAsStream(path);
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        return parser.parse(r);
    }

    // doesnt work very well for duplicate
    void testSpecificRandomMutation(Mutation m, String name) {
        Program unmutated = getProgram(UNMUTATED_PROGRAM);
        Random r = new Random();
        Program mutated = unmutated.mutate(r.nextInt(unmutated.size()), m);
        printProgram(mutated, name);
    }

    private static void printProgram(Program p, String info) {
        StringBuilder sb = new StringBuilder("\n" + info + "\n" + "=".repeat(80) + "\n");
        p.prettyPrint(sb);
        sb.append("=".repeat(80) + "\n");
        System.out.println(sb);
    }

    @Test
    void testNegatives() {
        printProgram(getProgram(UNMUTATED_PROGRAM), "");
    }

    // @Disabled
    @RepeatedTest(50)
    void testRandomMutation(RepetitionInfo info) {
        Program unmutated = getProgram(UNMUTATED_PROGRAM);
        Program mutated = unmutated.mutate();
        printProgram(mutated, "Random mutation " + info.getCurrentRepetition());
        // inspect mutated program visually if valid program
    }

    // @Disabled
    @RepeatedTest(50)
    void testRandomReplace(RepetitionInfo repetitionInfo) {
        testSpecificRandomMutation(MutationFactory.getReplace(), "Random Replace mutation " +
                repetitionInfo.getCurrentRepetition());
    }
    // @Disabled
    @RepeatedTest(50)
    void testRandomSwap(RepetitionInfo repetitionInfo) {
        testSpecificRandomMutation(MutationFactory.getSwap(),  "Random Swap mutation " +
                repetitionInfo.getCurrentRepetition());
    }

    // @Disabled
    @RepeatedTest(50)
    void testDuplicate(RepetitionInfo repetitionInfo) {
        testSpecificRandomMutation(MutationFactory.getDuplicate(), "Random Duplicate mutation " +
                repetitionInfo.getCurrentRepetition());
    }

    // @Disabled
    @Test
    void testMultipleMutation() {
        int d = 1000;
        Program p = getProgram(UNMUTATED_PROGRAM);
        printProgram(p, "Unmutated program");
        for (int i = 0; i < d; i++) {
            p = p.mutate();
        }
        printProgram(p, "Mutated " + d + " times");
    }

}

