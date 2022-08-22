package ast;

import java.util.ArrayList;
import java.util.List;

/** Represents a command in the AST */
public class Command extends AbstractNode {

	private boolean containsAction = false;

	/**
	 * Constructs a Command object
	 * 
	 * @param updates a list of {@code Update} nodes represented by the command
	 * @param a       possible action node associated with this command. Can be null
	 *                if there is no action
	 * @param p       parent Node. Can be null
	 */
	public Command(List<Update> updates, Action a, Node p) {
		for (Update u : updates)
			childs.add(u); // Since we can't simply say childs = updates
		if (a != null) {
			containsAction = true;
			childs.add(a);
		}
		parent = p;
	}

	@Override
	public boolean hasVariableChildren() {
		return true;
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		for (int i = 0; i < childs.size(); i++) {
			Node child = childs.get(i);
			child.prettyPrint(sb);
			if (i != childs.size() - 1) {
				sb.append(" ");
			}
		}
		return sb;
	}

	@Override
	public Node clone() {
		List<Update> clonedUpdates = new ArrayList<Update>();
		for (int i = 0; i < this.childs.size() - 1; i++) {
			clonedUpdates.add((Update) this.childs.get(i).clone());
		}
		Node last = this.childs.get(this.childs.size() - 1).clone();
		Command c;
		Action a = null;
		if (this.containsAction) {
			a = (Action) last;
		} else {
			clonedUpdates.add((Update) last);
		}
		c = new Command(clonedUpdates, a, null);
		for (Update u : clonedUpdates) {
			u.setParent(c);
		}
		if (a != null)
			a.setParent(c);
		return c;
	}
}
