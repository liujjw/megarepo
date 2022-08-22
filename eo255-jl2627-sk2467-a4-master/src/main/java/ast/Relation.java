package ast;

import model.Critter;
import parse.TokenCategory;
import parse.TokenType;

/** Represents a relation node in the AST */
public class Relation extends Condition {

	private TokenType relop; // Represents the type of relational operator between the two children
								// Invariant: relop is of category TokenCategory.RELOP

	/**
	 * Constructs a relation object between left expression, right expression, with
	 * given relational operator
	 * 
	 * @param left  left expression
	 * @param tt    relational operator, must be a TokenType of category RELOP
	 * @param right right expression
	 * @param p     parent node. Can be null
	 */
	public Relation(Expr left, TokenType tt, Expr right, Node p) {
		assert (tt.category().equals(TokenCategory.RELOP));
		relop = tt;
		childs.add(left);
		childs.add(right);
		parent = p;
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		if (hasBraces()) {
			for (int i = 0; i < numBraces; i++)
				sb.append("{ ");
		}
		childs.get(0).prettyPrint(sb);
		sb.append(" " + relop.toString() + " ");
		childs.get(1).prettyPrint(sb);

		if (hasBraces()) {
			for (int i = 0; i < numBraces; i++)
				sb.append(" }");
		}
		return sb;
	}

	public Node clone() {
		Expr left = (Expr) childs.get(0).clone();
		Expr right = (Expr) childs.get(1).clone();
		Relation r = new Relation(left, relop, right, null);
		left.setParent(r);
		right.setParent(r);
		return r;
	}

	@Override
	public int getVal(Critter crit) {
		Expr left = (Expr) childs.get(0);
		Expr right = (Expr) childs.get(1);
		int val = 0;
		switch (relop) {
		case LT:
			val = left.getVal(crit) < right.getVal(crit) ? 1 : 0;
			break;
		case LE:
			val = left.getVal(crit) <= right.getVal(crit) ? 1 : 0;
			break;
		case EQ:
			val = left.getVal(crit) == right.getVal(crit) ? 1 : 0;
			break;
		case GE:
			val = left.getVal(crit) >= right.getVal(crit) ? 1 : 0;
			break;
		case GT:
			val = left.getVal(crit) > right.getVal(crit) ? 1 : 0;
			break;
		case NE:
			val = left.getVal(crit) != right.getVal(crit) ? 1 : 0;
			break;
		default:
		}
		return val;
	}

}
