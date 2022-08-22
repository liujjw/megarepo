package ast;

import model.Critter;

/** Represents a number in the AST */
public class Number extends Expr {

	public int value;

	/**
	 * Created a number with value i and parent node p
	 * 
	 * @param i integer represented
	 * @param p parent node. Can be null
	 */
	public Number(int i, Node p) {
		value = i;
		parent = p;
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {

		if(getNumNegatives() != 0) {
			for (int i = 0; i < getNumNegatives(); i++) {
				sb.append("-");
			}
		}

		if (hasParentheses()) {
			for (int i = 0; i < numParentheses; i++) {
				sb.append("( ");
			}
		}
		sb.append(String.valueOf(value));

		if (hasParentheses()) {
			for (int i = 0; i < numParentheses; i++) {
				sb.append(" )");
			}
		}
		return sb;
	}

	@Override
	public Node clone() {
		Number n = new Number(value, null);
		n.numNegatives = this.numNegatives;
		n.numParentheses = this.numParentheses;
		return n;
	}


	@Override
	public int getVal(Critter crit) {
		int val = value;
		if (getNumNegatives() % 2 == 1) {
			val = val * -1;
		}
		return val;
	}
}
