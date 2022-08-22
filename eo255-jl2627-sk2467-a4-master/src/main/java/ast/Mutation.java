package ast;

/** A mutation to the AST */
public interface Mutation {
    /**
     * Compares the type of this mutation to {@code m}
     *
     * @param m The mutation to compare with
     * @return Whether this mutation is the same type as {@code m}
     */
    boolean equals(Mutation m);

    /**
     * Mutates a given program according to the type of mutation in concrete subtypes
     * at a random node.
     *
     * @param root The root node of the program to mutate
     * @return A mutated program, or null if the program is invalid after mutation */
     Program mutate(Program root);

    /**
     * Mutates a given program according to the type of mutation in concrete subtypes.
     * Requires index be valid with respect to the program AST.
     *
     * @param root The root node of the program to mutate
     * @param index The index of the program node to mutate.
     * @return A mutated program, or null if the program is invalid after mutation */
     Program mutate(Program root, int index);

}
