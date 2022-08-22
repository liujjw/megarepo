package ast.mutation;

import ast.Mutation;
import ast.Node;
import ast.Program;

import java.util.List;

public class Remove extends AbstractMutation {
    public Program mutate(Program root, int index) {
        // binary arithmetic needs one operand replacing it
        // number can't be mutated; has no children
        // indexnotation needs its child replacing it
        // relationals need a child to be a relational
        // logicals need a child to be a conditional
        // rule can just be removed, unless there is only one rule
        // commands can't be removed because there needs to be a command
        // updates can be removed, unless there is only one update
        // actions can be removed
        throw new UnsupportedOperationException();
//        Node nodeToRemove = root.nodeAt(index);
//        Node parentOfNodeToRemove = root.nodeAt(index).getParent();
//        Node newProgram = root.clone();
//        if (needsReplacement(parentOfNodeToRemove)) {
//            List<Node> children = nodeToRemove.getChildren();
//            // set the parent to point to a child
//        } else {
//            // set the parent node's pointer to either null
//        }
//        return (Program) newProgram;
    }

}
