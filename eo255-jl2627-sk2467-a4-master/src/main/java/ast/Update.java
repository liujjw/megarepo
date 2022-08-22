package ast;

/** Represents an Update node in the AST, in the form mem[e1] := e2 */
public class Update extends AbstractNode {

	/**
	 * Constructs an Update node of the form mem[e1] := e2
	 * 
	 * @param e1 Left expression to be evaluated in mem
	 * @param e2 Right expression
	 * @param p  parent node. Can be null
	 */
	public Update(Expr e1, Expr e2, Node p) {
		childs.add(e1);
		childs.add(e2);
		parent = p;
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append("mem[");
		childs.get(0).prettyPrint(sb);
		sb.append("] := ");
		childs.get(1).prettyPrint(sb);
		return sb;
	}

	@Override
	public Node clone() {
		Expr left = (Expr) childs.get(0).clone();
		Expr right = (Expr) childs.get(1).clone();
		Update u = new Update(left, right, null);
		left.setParent(u);
		right.setParent(u);
		return u;
	}
}
