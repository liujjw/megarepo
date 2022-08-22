package ast.mutation;

import ast.Mutation;
import ast.Node;
import ast.Program;

import java.util.List;
import java.util.Random;

public class Swap extends AbstractMutation {
    public Program mutate(Program root, int index) {
        Node newProgram = root.clone();
        Node parentOfChildrenToSwap = newProgram.nodeAt(index);
        if (!parentOfChildrenToSwap.isChildrenSwappable()) return root;
        List<Node> children = parentOfChildrenToSwap.getChildren();
        if (children.size() <= 1) return root;

        Random r = new Random();
        int r1 = r.nextInt(children.size());
        int r2 = r.nextInt(children.size());
        while (r2 == r1) r2 = r.nextInt(children.size());

        Node child1 = children.get(r1);
        Node child2 = children.get(r2);

        children.set(r1, child2);
        children.set(r2, child1);

        return (Program) newProgram;
    }

}
