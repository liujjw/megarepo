package ast.mutation;

import ast.Node;
import ast.Program;

import java.util.List;
import java.util.Random;

public class Duplicate extends AbstractMutation {
    public Program mutate(Program root, int index) {
        Node newProgram = root.clone();
        Node nodeToMutate = newProgram.nodeAt(index);
        if (!nodeToMutate.hasVariableChildren()) {
            return root;
        }
        List<Node> children = nodeToMutate.getChildren();
        if ((children.size() == 1 && !(children.get(0)).isDuplicable()) || children.size() == 0) {
            return root;
        }
        Random r = new Random();
        Node duplicateChild = children.get(r.nextInt(children.size())).clone();
        while(!duplicateChild.isDuplicable()) duplicateChild = children.get(r.nextInt(children.size())).clone();
        children.add(duplicateChild);
        return (Program) newProgram;
    }


}
