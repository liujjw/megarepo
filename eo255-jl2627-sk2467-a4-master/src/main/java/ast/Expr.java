package ast;

/** A critter program expression that has an integer value. */
public abstract class Expr extends AbstractNode {
	protected int numParentheses = 0; // Represents the number of parentheses enclosing the given expressions
	protected int numNegatives = 0; // Represents the number of negative signs in front of the given expression

	/**
	 * Get the number of negative signs in front of this expression
	 * 
	 * @return number of negatives
	 */
	public int getNumNegatives() {
		return numNegatives;
	}

	/**
	 * Increases the number of negatives in front of this expression
	 * 
	 */
	public void increaseNegatives() {
		numNegatives++;
	}

	/**
	 * Check whether the given expression is surrounded by parentheses
	 * 
	 * @return whether the expression is surrounded by parentheses
	 */
	public boolean hasParentheses() {
		return numParentheses > 0;
	}

	/**
	 * Increases the number of parentheses enclosing this expression
	 * 
	 */
	public void increaseParentheses() {
		numParentheses = 1;
	}

}
