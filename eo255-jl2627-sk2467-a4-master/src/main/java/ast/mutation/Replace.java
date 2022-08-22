package ast.mutation;

import ast.Node;
import ast.Program;

import java.util.List;

public class Replace extends AbstractMutation {
    public Program mutate(Program root, int index) {
        Node newProgram = root.clone();

        Node nodeToReplace = newProgram.nodeAt(index);
        Node validReplacement = (super.find(newProgram, nodeToReplace));
        // we can also replace by any concrete expr/condition with any other
        if (validReplacement == null) {
            return root;
        }
        validReplacement = validReplacement.clone();
        Node parent = nodeToReplace.getParent();
        if (parent == null) { // rules
            parent = newProgram;
        }
        List<Node> siblings = parent.getChildren();
        siblings.set(siblings.indexOf(nodeToReplace), validReplacement);

        return (Program) newProgram;
    }


}
