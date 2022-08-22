package ast;

/** An abstract class representing a Boolean condition in a critter program. */
public abstract class Condition extends AbstractNode {
	protected int numBraces = 0; // Represents how many braces {} surround the condition

	/**
	 * Checks whether the condition is surrounded by braces
	 * 
	 * @return whether the condition object has braces
	 */
	public boolean hasBraces() {
		return numBraces > 0;
	}

	/**
	 * Increases the number of braces surrounding the {@code Condition} by 1
	 * 
	 */
	public void increaseBraces() {
		numBraces++;
	}

	@Override
	public boolean isCondition() {
		return true;
	}

}
