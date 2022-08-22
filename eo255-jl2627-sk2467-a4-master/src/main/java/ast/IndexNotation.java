package ast;

import java.util.Random;

import model.Critter;
import parse.TokenCategory;
import parse.TokenType;
/** Represents an index notation in an AST */
public class IndexNotation extends Expr {
	public TokenType tokentype; // Will either be TokenType.MEM or of category TokenCategory.SENSOR

	/**
	 * Constructs an IndexNotation of the form tt[e]
	 * 
	 * @param tt tokenType of subject to be indexed. Must be TokenType.MEM or of
	 *           category TokenCategory.SENSOR. If TokenType is SMELL, then the
	 *           no-expr constructor should be called.
	 * @param e  expression to index
	 * @param p  parent node. Can be null
	 */
	public IndexNotation(TokenType tt, Expr e, Node p) {
		assert tt.equals(TokenType.MEM) || tt.category().equals(TokenCategory.SENSOR);
		assert !tt.equals(TokenType.SMELL);
		tokentype = tt;
		childs.add(e);
		parent = p;
	}

	/**
	 * Represents smell
	 * 
	 * @param tt tokenType of subject to be indexed. Must be TokenType.SMELL
	 * @param p  parent node. Can be null
	 */
	public IndexNotation(TokenType tt, Node p) {
		assert (tt.equals(TokenType.SMELL));
		tokentype = tt;
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

		sb.append(tokentype.toString());
		if (!tokentype.equals(TokenType.SMELL)) {
			sb.append("[");
			childs.get(0).prettyPrint(sb);
			sb.append("]");
		}

		if (hasParentheses()) {
			for (int i = 0; i < numParentheses; i++) {
				sb.append(" )");
			}
		}
		return sb;
	}

	@Override
	public Node clone() {
		IndexNotation i;
		if (tokentype.equals(TokenType.SMELL)) {
			i = new IndexNotation(tokentype, null);
		} else {
			Expr e = (Expr) childs.get(0).clone();
			i = new IndexNotation(tokentype, e, null);
			e.setParent(i);
		}
		i.numParentheses = this.numParentheses;
		i.numNegatives = this.numNegatives;
		return i;
	}
	
	@Override
	public int getVal(Critter crit) {
		// TODO assuming smell is the only getVal with no children
		int val = 0;
		if (childs.size() == 0) {
			val = crit.smell();
		} else {
			int exprAsNumber = childs.get(0).getVal(crit);
			switch (tokentype) {
				case NEARBY:
					val = crit.getNearby(exprAsNumber);
					break;
				case AHEAD:
					val = crit.getAhead(exprAsNumber);
					break;
				case RANDOM:
					Random r = new Random();
					if (exprAsNumber < 2)
						val = 0;
					else
						val = r.nextInt(exprAsNumber);
					break;
				case SMELL:
					val = crit.smell();
					break;
				case MEM:
					val = crit.getMemory(exprAsNumber);
					break;
				default:
			}
		}

		if (getNumNegatives() % 2 == 1) {
			val = val * -1;
		}

		return val;
	}
}
