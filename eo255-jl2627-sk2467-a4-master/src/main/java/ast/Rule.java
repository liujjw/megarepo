package ast;

/** A representation of a critter rule. */
public class Rule extends AbstractNode {

	/**
	 * Constructs a critter rule with given condition and command
	 * 
	 * @param condition condition represented by the rule
	 * @param command   command represented by rule
	 * @param p         parent node. Can be null
	 */
	public Rule(Condition condition, Command command, Node p) {
		childs.add(condition);
		childs.add(command);
		parent = p;
	}

	@Override

	public boolean isChildrenSwappable() {
		return false;
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		childs.get(0).prettyPrint(sb);
		sb.append(" --> ");
		childs.get(1).prettyPrint(sb);
		sb.append(" ;\n");
		return sb;
	}

	@Override
	public Node clone() {
		Condition left = (Condition) childs.get(0).clone();
		Command right = (Command) childs.get(1).clone();
		Rule r = new Rule(left, right, null);
		left.setParent(r);
		right.setParent(r);
		return r;
	}

}
