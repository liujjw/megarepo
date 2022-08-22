package ast;

import parse.TokenCategory;
import parse.TokenType;

/** Represents an Action in the AST */
public class Action extends AbstractNode {

	private TokenType action; // Holds the given action represented by this node. Invariant: must be of
								// TokenCategory ACTIOn

	/**
	 * Constructs an Action object with given action and possible expression child
	 * 
	 * @param a TokenType of action represented. Must be of TokenCategory ACTION
	 * @param p parent node. Can be null
	 */
	public Action(TokenType a, Node p) {
		assert a.category().equals(TokenCategory.ACTION);
		action = a;
		parent = p;
	}

	/**
	 * Constructs an Action object with given action and expression: serve[e]
	 * 
	 * @param a TokenType of action represented. Must be TokenType.SERVE
	 * @param e Expression of index
	 * @param p parent node. Can be null
	 */
	public Action(TokenType a, Expr e, Node p) {
		assert a.equals(TokenType.SERVE);
		action = a;
		childs.add(e);
		parent = p;
	}

	@Override
	public boolean isDuplicable() {
		return false;
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append(action.toString());
		if (childs.size() > 0) {
			sb.append(" [ ");
			childs.get(0).prettyPrint(sb);
			sb.append(" ]");
		}
		return sb;
	}

	@Override
	public Node clone() {
		if (action.equals(TokenType.SERVE)) {
			Expr e = (Expr) childs.get(0).clone();
			Action a = new Action(action, e, null);
			e.setParent(a);
			return a;
		} else {
			return new Action(action, null);
		}
	}
	
	public TokenType getActionType() {
		return action;
	}
	
}