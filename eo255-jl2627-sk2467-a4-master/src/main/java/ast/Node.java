package ast;

import java.util.List;

import model.Critter;

/** A node in the abstract syntax tree of a program. */
public interface Node extends Cloneable {

    /**
     * The number of nodes in the AST rooted at this node, including this node
     *
     * @return The size of the AST rooted at this node
     */
    int size();

    /**
     * Returns the node at {@code index} in the AST rooted at this node. Indices are defined such
     * that:<br>
     * 1. Indices are in the range {@code [0, size())}<br>
     * 2. {@code this.nodeAt(0) == this} for all nodes in the AST <br>
     * 3. All nodes in the AST rooted at {@code this} must be reachable by a call to {@code
     * this.nodeAt(i)} with an appropriate index {@code i}
     *
     * @param index The index of the node to retrieve
     * @return The node at {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is not in the range of valid indices
     */
    Node nodeAt(int index);

    /**
     * Appends the program represented by this node prettily to the given StringBuilder.
     *
     * <p>The output of this method must be consistent with both the critter grammar and itself;
     * that is:<br>
     * 1. It must be possible to put the result of this method into a valid critter program<br>
     * 2. Placing the result of this method into a valid critter program then parsing the program
     * must yield an AST which contains a subtree identical to the one rooted at {@code this}
     *
     * @param sb The {@code StringBuilder} to which the program will be appended
     * @return The {@code StringBuilder} to which this program was appended
     */
    StringBuilder prettyPrint(StringBuilder sb);

    /**
     * Returns the pretty-print of the abstract syntax subtree rooted at this {@code Node}.
     *
     * <p>This method returns the same result as {@code prettyPrint(...).toString()}
     *
     * @return The pretty-print of the AST rooted at this {@code Node}.
     */
    @Override
    String toString();

    /**
     * Makes a deep copy of this {@code Node}.
     *
     * @return A deep copy of this {@code Node}.
     */
    Node clone();

    /**
     * Gets the children of this {@code Node}.
     *
     * @return A list of the children this {@code Node}.
     */
    List<Node> getChildren();

    /**
     * Set the children of a {@code Node}.
     * */
    void setChildren(List<Node> children);

    /**
     * Gets the parent of this {@code Node}. Null is returned if this {@code Node} is the root.
     *
     * @return the parent of this {@code Node}, or {@code null} if it is the root.
     */
    Node getParent();

    /**
     * Whether the node has a variable number of children.
     *
     * @return true if node has variable children, fals e otherwise
     * */
    boolean hasVariableChildren();

    /**
     * Whether the node is an Action node.
     *
     * @return true if so, otherwise false
     * */
    boolean isDuplicable();

    /**
     * @return whether it makes sense to swap its children */
    boolean isChildrenSwappable();


    /**
     * @return whether a node is meant to represent a logical condition*/
    boolean isCondition();

    /**
     * @return whether a node is meant to represent a value */
    boolean isValue();

	/**
	 * 
	 * @return simplified numeric representation of all possible node values
	 */

	int getVal(Critter crit);

}
