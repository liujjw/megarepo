package ast;

import model.Critter;

/** Represents a Binary Arithmetic (+,-,*,/,%) expression in the AST */
public class BinaryArithmetic extends Expr {

	public ArithmeticOperator operator; // The operator represented by this arithmetic.
										// Invariant: not null (implied)

	/**
	 * Constructs a BinaryArithmetic object representing l op r
	 * 
	 * @param l  left expression
	 * @param op operator
	 * @param r  right expression
	 * @param p  parent node. Can be null
	 */
	public BinaryArithmetic(Expr l, ArithmeticOperator op, Expr r, Node p) {
		childs.add(l);
		childs.add(r);
		operator = op;
		parent = p;
	}

	/** An enumeration of all possible Arithmetic operators */
	public enum ArithmeticOperator {
		PLUS, MINUS, TIMES, DIVIDE, MOD
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		for (int i = 0; i < numNegatives; i++) {
			sb.append("-");
		}

		if (hasParentheses()) {
			for (int i = 0; i < numParentheses; i++) {
				sb.append("( ");
			}

		}

		childs.get(0).prettyPrint(sb);
		switch (operator) {
		case PLUS:
			sb.append(" + ");
			break;
		case MINUS:
			sb.append(" - ");
			break;
		case TIMES:
			sb.append(" * ");
			break;
		case DIVIDE:
			sb.append(" / ");
			break;
		case MOD:
			sb.append(" mod ");
			break;
		}

		childs.get(1).prettyPrint(sb);

		if (hasParentheses()) {
			for (int i = 0; i < numParentheses; i++) {
				sb.append(" )");
			}
		}

		return sb;
	}

	@Override
	public Node clone() {
		Expr left = (Expr) childs.get(0).clone();
		Expr right = (Expr) (Expr) childs.get(1).clone();
		BinaryArithmetic b = new BinaryArithmetic(left, operator, right, null);
		left.setParent(b);
		right.setParent(b);
		b.numNegatives = this.numNegatives;
		b.numParentheses = this.numParentheses;
		return b;
	}

	@Override
	public int getVal(Critter crit) {

		int val = 0;
		int l = childs.get(0).getVal(crit);
		int r = childs.get(1).getVal(crit);
		switch (operator) {
		case PLUS:
			val = l + r;
			break;
		case MINUS:
			val = l - r;
			break;
		case TIMES:
			val = l * r;
			break;
		case DIVIDE:
			if (r == 0)
				val = 0;
			else
				val = l / r;
			break;
		case MOD:
			if (r == 0)
				val = 0;
			else
				val = l % r;
			break;
		}
		

		if (getNumNegatives() % 2 == 1) {
			val = val * -1;
		}
		return (val);
	}
}
