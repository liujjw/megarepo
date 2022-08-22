package ast.mutation;

import ast.Mutation;
import ast.Node;
import ast.Program;
import ast.ProgramImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractMutation implements Mutation {

    public static final int NUM_TIMES_TO_TRY = 10;
    /**
     * Find all other instances of a similar node type in an AST except the reference node itself.
     *
     * @param root the root of the subtree to start searching
     * @param type the type of node to look for
     * @return a List of other instances of a given node type
     * */
    public List<Node> findAll(Node root, Node type, List<Node> others) {
        for (Node n : root.getChildren()) {
            findAll(n, type, others);
            if (n.getClass().getSimpleName().equals(type.getClass().getSimpleName())) {
                if (type != n) { // if its not the same node
                    others.add(n);
                }
            } else if (type.isValue() && n.isValue()) {
                if (type != n) { // if its not the same node
                    others.add(n);
                }
            } else if (type.isCondition() && n.isCondition()) {
                if (type != n) { // if its not the same node
                    others.add(n);
                }
            }
        }
        return others;
    }

    /**
     * Find a random node of similar type in an AST.
     * Requires the type node be from the same AST.
     *
     * @param root the root of the subtree to start searching
     * @param type the type of node to look for
     * @return a pointer to another node of the same type, or null if couldn't find one
     * */
    public Node find(Node root, Node type) {
        List<Node> others = findAll(root, type, new ArrayList<Node>());
        if (others.isEmpty()) return null;
        return others.get((new Random()).nextInt(others.size()));
    }

    public Program mutate(Program root) {
        Random r = new Random();
        int counter = 1;
        Program mutation = mutate(root, r.nextInt(root.size()));
        while (mutation == root && counter < NUM_TIMES_TO_TRY) {
            mutation = mutate(root, r.nextInt(root.size()));
            counter++;
        }
        return mutation;
    }

    abstract public Program mutate(Program root, int index);


    public boolean equals(Mutation m) {
        return (m.getClass().getSimpleName().equals(this.getClass().getSimpleName()));
    }

}
