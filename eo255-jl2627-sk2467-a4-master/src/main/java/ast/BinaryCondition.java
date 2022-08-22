package ast;

import model.Critter;

/** A representation of a binary Boolean condition: 'and' or 'or' */
public class BinaryCondition extends Condition {

	private Operator operator;

	/**
	 * Create an AST representation of l op r.
	 *
	 * @param l  Left condition
	 * @param op operator
	 * @param r  right condition
	 * @param p  parent node. Can be null
	 */
	public BinaryCondition(Condition l, Operator op, Condition r, Node p) {
		childs.add(l);
		childs.add(r);
		operator = op;
		parent = p;
	}

	/** An enumeration of all possible binary condition operators. */
	public enum Operator {
		OR, AND;
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {

		if (hasBraces()) {
			for (int i = 0; i < numBraces; i++)
				sb.append("{ ");
		}
		childs.get(0).prettyPrint(sb);
		if (operator == Operator.AND) {
			sb.append(" and ");
		} else {
			sb.append(" or ");
		}
		childs.get(1).prettyPrint(sb);

		if (hasBraces()) {
			for (int i = 0; i < numBraces; i++)
				sb.append(" }");
		}

		return sb;
	}

	@Override
	public Node clone() {
		Condition left = (Condition) childs.get(0).clone();
		Condition right = (Condition) childs.get(1).clone();
		BinaryCondition b = new BinaryCondition(left, operator, right, null);
		left.setParent(b);
		right.setParent(b);
		b.numBraces = this.numBraces;
		return b;
	}

	@Override
	public int getVal(Critter crit) {
		Condition left = (Condition) childs.get(0);
		Condition right = (Condition) childs.get(1);
		int val = 0;
		switch (operator) {
		case OR:
			val = (left.getVal(crit) + right.getVal(crit) > 0) ? 1 : 0;
			break;
		case AND:
			val = (left.getVal(crit) + right.getVal(crit) == 2) ? 1 : 0;
			break;
		default:
		}

		return val;
	}
}
