package ast;

import java.util.Random;

/** A data structure representing a critter program. */
public class ProgramImpl extends AbstractNode implements Program {

	@Override
	public boolean hasVariableChildren() {
		return true;
	}

	public Node clone() {
		ProgramImpl p = new ProgramImpl();
		for (Node n : this.childs) {
			Node clone = n.clone();
			p.childs.add(clone);
		}
		return p;
	}


	@Override
	public Program mutate() { // guaranteed rule set change
		Random r = new Random();
		Program returning = null;
		String type = "";
		switch (r.nextInt(3)) {
			case 0:
				type = "Swap";
				returning = MutationFactory.getSwap().mutate(this);
				break;
			case 1:
				type = "Replace";
				returning = MutationFactory.getReplace().mutate(this);
				break;
			case 2:
				type = "Duplicate";
				returning = MutationFactory.getDuplicate().mutate(this);
				break;
		}
		// We must print the type of mutation at this stage in order for the command
		// line functionality to work as desired.
		System.out.println(type + " mutation applied");
		return returning;
	}

	@Override
	public Program mutate(int index, Mutation m) {
		if (index >= this.size()) {
			throw new IndexOutOfBoundsException();
		}
		return m.mutate(this, index);
	}

	/*
	 * public StringBuilder prettyPrint(StringBuilder sb) { List<Node> theChildren =
	 * getChildren(); for return sb; }
	 * 
	 * public String toString() { StringBuilder sb = new StringBuilder("");
	 * System.out.println(sb.toString()); }
	 */
}
