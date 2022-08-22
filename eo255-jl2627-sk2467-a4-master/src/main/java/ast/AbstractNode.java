package ast;

import java.util.ArrayList;
import java.util.List;

import model.Critter;

public abstract class AbstractNode implements Node {
	protected List<Node> childs = new ArrayList<Node>(); // Called childs so that setChildren() can be implemented
															// without changing its
															// Invariant: childs not null and does not contain any null
															// Nodes as elements
	protected Node parent; // Represents parent node. Can be null for a Program, or temporarily (in the
							// constructor) if it will be updated later through a call to setParent(Node p).

	@Override
	public int size() {
		int count = 1;
		for (Node child : childs) {
			if (child != null)
				count += child.size();
		}
		return count;
	}

	@Override
	public Node nodeAt(int index) {
		List<Node> children = new ArrayList<Node>();
		addAllChildren(this, children);
		return children.get(index);
	}

	private void addAllChildren(Node root, List<Node> nodes) {
		for (Node child : root.getChildren())
			addAllChildren(child, nodes);
		nodes.add(root);
	}


	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		for (Node child : childs) {
			child.prettyPrint(sb);
		}
		return sb;
	}

	@Override
	public String toString() {
		return prettyPrint(new StringBuilder()).toString();
	}

	public abstract Node clone();

	@Override
	public List<Node> getChildren() {
		return childs;
	}

	/**
	 * You can remove this method if you don't like it.
	 *
	 * Sets the children of this {@code Node}.
	 *
	 * @param children A list of the children this {@code Node}.
	 */
	@Override
	public void setChildren(List<Node> children) {
		childs = children;
	}

	@Override
	public Node getParent() {
		return parent;
	}

	/**
	 * You can remove this method if you don't like it.
	 *
	 * Sets the parent of this {@code Node}.
	 *
	 * @param p the node to set as this {@code Node}'s parent.
	 */
	public void setParent(Node p) {
		parent = p;

	}

	/**
	 * Override if a node has variable children, otherwise leave as is.
	 */
	public boolean hasVariableChildren() {
		return false;
	}

	/**
	 * Override if node cannot be duplicated, otherwise leave as is. Use in tandem
	 * with hasVariableChildren()
	 */
	public boolean isDuplicable() {
		return true;
	}

	/**
	 * Override if children cannot be swapped, otherwise leave as is.
	 */
	public boolean isChildrenSwappable() {
		return true;
	}


	/**
	 * Override if node is a logical condition, otherwise leave as is.
	 */
	public boolean isCondition() {
		return false;
	}

	/**
	 * Override if a node is meant to represent a value, otherwise leave as is.
	 */
	public boolean isValue() {
		return false;
	}
	
	// A stub that is always overridden
	public int getVal(Critter crit) {
		return 0;
	}
}
