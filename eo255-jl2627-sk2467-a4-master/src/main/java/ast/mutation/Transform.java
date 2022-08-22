package ast.mutation;

import ast.Mutation;
import ast.Node;
import ast.Program;

public class Transform extends AbstractMutation {
    public Program mutate(Program root, int index) {
         throw new UnsupportedOperationException();
//        Node newProgram = root.clone();
//        Node typeOfNodeToReplace = newProgram.nodeAt(index);
//        if (typeOfNodeToReplace.getClass().getSimpleName().equals("Number")) {
//            Node newNumber = new Number(); // would need to add a method to node interface that
//            // can access the number
//            super.set(newProgram, index, adjustedNumber);
//        } else {
//            Node sameTypeOfNode = (super.find(root, typeOfNodeToReplace)).clone();
//            if (sameTypeOfNode == null) return null;
//            super.set(newProgram, index, sameTypeOfNode);
//            // keep children the same
//        }
//        return (Program) newProgram;
    }

}
