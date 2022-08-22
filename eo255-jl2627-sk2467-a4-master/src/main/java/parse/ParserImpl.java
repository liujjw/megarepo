package parse;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import ast.Action;
import ast.BinaryArithmetic;
import ast.BinaryArithmetic.ArithmeticOperator;
import ast.BinaryCondition;
import ast.BinaryCondition.Operator;
import ast.Command;
import ast.Condition;
import ast.Expr;
import ast.IndexNotation;
import ast.Number;
import ast.Program;
import ast.ProgramImpl;
import ast.Relation;
import ast.Rule;
import ast.Update;
import exceptions.SyntaxError;

/** An implementation class of interface parser */
public class ParserImpl implements Parser {

	@Override
	public Program parse(Reader r) {
		Tokenizer t = new Tokenizer(r);
		try {
			return ParserImpl.parseProgram(t);
		} catch (SyntaxError e) {
			System.err.println("Parser " + e.getMessage());
			// As per the method specs, we return null if there is a syntax error
			return null;
		}
	}

	/**
	 * Parses a program from the stream of tokens provided by the Tokenizer,
	 * consuming tokens representing the program. All following methods with a name
	 * "parseX" have the same spec except that they parse syntactic form X.
	 *
	 * @return the created AST
	 * @throws SyntaxError if there the input tokens have invalid syntax
	 */
	public static ProgramImpl parseProgram(Tokenizer t) throws SyntaxError {
		ProgramImpl p = new ProgramImpl();
		while (t.hasNext()) {
			Rule rule = parseRule(t);
			rule.setParent(p);
			p.getChildren().add(rule);
		}
		if (p.size() <= 1)
			throw new SyntaxError("Error: An empty string is not a program");
		return p;
	}

	public static Rule parseRule(Tokenizer t) throws SyntaxError {
		Condition condition = parseCondition(t);
		consume(t, TokenType.ARR);
		Command command = parseCommand(t);
		consume(t, TokenType.SEMICOLON);
		Rule r = new Rule(condition, command, null);
		condition.setParent(r);
		command.setParent(r);
		return r;
	}

	public static Condition parseCondition(Tokenizer t) throws SyntaxError {
		// Parses everything up until the arrow -->
		Condition leftConj = parseConjunction(t);
		while (!willConsume(t, TokenType.ARR) && willConsume(t, TokenType.OR)) {
			consume(t, TokenType.OR);
			Condition rightConj = parseConjunction(t);
			Condition newConj = new BinaryCondition(leftConj, Operator.OR, rightConj, null);
			leftConj.setParent(newConj);
			rightConj.setParent(newConj);
			leftConj = newConj;
		}
		return leftConj;
	}

	public static Condition parseConjunction(Tokenizer t) throws SyntaxError {
		Condition leftRel = parseRelation(t);
		while (willConsume(t, TokenType.AND)) {
			consume(t, TokenType.AND);
			Condition rightRel = parseConjunction(t);
			Condition newRel = new BinaryCondition(leftRel, Operator.AND, rightRel, null);
			leftRel.setParent(newRel);
			rightRel.setParent(newRel);
			leftRel = newRel;
		}
		return leftRel;
	}

	public static Condition parseRelation(Tokenizer t) throws SyntaxError {
		Condition r;
		if (willConsume(t, TokenType.LBRACE)) {
			consume(t, TokenType.LBRACE);
			r = parseCondition(t);
			// No need to set parent in this case as we are simply passing the condition up
			r.increaseBraces();
			consume(t, TokenType.RBRACE);
		} else {
			Expr left = parseExpression(t);
			TokenType tt = t.peek().getType();
			if (!tt.category().equals(TokenCategory.RELOP))
				throw new SyntaxError("Line " + t.peek().lineNumber()
						+ ": Expected relational operator between expressions, but found " + tt.toString()
						+ " instead");
			consume(t, tt);
			Expr right = parseExpression(t);
			// OK to pass in tt because we already checked that it is a RELOP category
			r = new Relation(left, tt, right, null);
			left.setParent(r);
			right.setParent(r);
		}
		return r;
	}

	public static Command parseCommand(Tokenizer t) throws SyntaxError {
		List<Update> updates = new ArrayList<Update>();
		// The end of the command is a semicolon
		Action action = null;
		while (!willConsume(t, TokenType.SEMICOLON)) {
			// Check if it's about to be an action
			if (t.peek().isAction()) {
				// If it is, parse the action and then break
				action = parseAction(t);
				break;
			}
			// Otherwise it is not an action, and therefore an update
			Update u = parseUpdate(t);
			updates.add(u);
		}
		if (updates.size() == 0 && action == null)
			throw new SyntaxError(
					"Line " + t.peek().lineNumber() + ": Must have at least one update or action, but none found");
		Command command = new Command(updates, action, null);
		for (Update node : updates)
			node.setParent(command);
		if (action != null)
			action.setParent(command);
		return command;
	}

	public static Update parseUpdate(Tokenizer t) throws SyntaxError {
		Expr e1 = parseMem(t);
		consume(t, TokenType.ASSIGN);
		Expr e2 = parseExpression(t);
		Update update = new Update(e1, e2, null);
		e1.setParent(update);
		e2.setParent(update);
		return update;
	}

	public static Action parseAction(Tokenizer t) throws SyntaxError {
		TokenType type = t.peek().getType();
		if (!type.category().equals(TokenCategory.ACTION))
			throw new SyntaxError(
					"Line " + t.peek().lineNumber() + ": Action was expected, but " + type.toString() + " found");
		Action action;
		if (type.equals(TokenType.SERVE)) {
			consume(t, type);
			consume(t, TokenType.LBRACKET);
			Expr e = parseExpression(t);
			consume(t, TokenType.RBRACKET);
			action = new Action(type, e, null);
			e.setParent(action);
		} else {
			// Then there is no bracketed expression, so use a different constructor
			consume(t, type);
			action = new Action(type, null);
		}
		return action;
	}

	public static Expr parseExpression(Tokenizer t) throws SyntaxError {
		// Keep going until no more ADDOP
		Expr leftTerm = parseTerm(t);
		while (t.peek().isAddOp()) {
			ArithmeticOperator op;
			if (willConsume(t, TokenType.PLUS)) {
				op = ArithmeticOperator.PLUS;
				consume(t, TokenType.PLUS);
			} else {
				// It will either be plus or minus
				op = ArithmeticOperator.MINUS;
				consume(t, TokenType.MINUS);
			}
			Expr rightTerm = parseTerm(t);
			Expr newTerm = new BinaryArithmetic(leftTerm, op, rightTerm, null);
			leftTerm.setParent(newTerm);
			rightTerm.setParent(newTerm);
			leftTerm = newTerm;
		}
		return leftTerm;
	}

	public static Expr parseTerm(Tokenizer t) throws SyntaxError {
		// Very similar structure to parseExpression: keep going until no more MULOP
		Expr leftFactor = parseFactor(t);
		while (t.peek().isMulOp()) {
			ArithmeticOperator op;
			if (willConsume(t, TokenType.MUL)) {
				op = ArithmeticOperator.TIMES;
				consume(t, TokenType.MUL);
			} else if (willConsume(t, TokenType.DIV)) {
				op = ArithmeticOperator.DIVIDE;
				consume(t, TokenType.DIV);
			} else {
				// If not a times or a divide, then a modulo
				op = ArithmeticOperator.MOD;
				consume(t, TokenType.MOD);
			}
			Expr rightFactor = parseFactor(t);
			Expr newFactor = new BinaryArithmetic(leftFactor, op, rightFactor, null);
			leftFactor.setParent(newFactor);
			rightFactor.setParent(newFactor);
			leftFactor = newFactor;
		}
		return leftFactor;
	}

	public static Expr parseFactor(Tokenizer t) throws SyntaxError {
		// There are many cases for this one
		Token token = t.peek();
		Expr e;
		IndexNotation i;
		switch (token.getType()) {
		case NUM:
			consume(t, TokenType.NUM);
			return new Number(token.toNumToken().getValue(), null);
		case MEM:
			e = parseMem(t);
			i = new IndexNotation(TokenType.MEM, e, null);
			e.setParent(i);
			return i;
		case LPAREN:
			consume(t, TokenType.LPAREN);
			e = parseExpression(t);
			e.increaseParentheses();
			consume(t, TokenType.RPAREN);
			return e;
		case MINUS:
			consume(t, TokenType.MINUS);
			e = parseFactor(t);
			e.increaseNegatives();
			return e;
		default:
			if (token.isMemSugar()) {
				e = parseMem(t);
				i = new IndexNotation(TokenType.MEM, e, null);
				e.setParent(i);
				return i;
			} else
				return parseSensor(t);
		}
	}

	public static Expr parseSensor(Tokenizer t) throws SyntaxError {
		Token token = t.peek();
		if (!token.isSensor()) {
			throw new SyntaxError(
					"Line " + token.lineNumber() + ": Sensor expected, got " + token.toString() + " instead");
		}
		consume(t, token.getType());
		if (token.getType().equals(TokenType.SMELL)) {
			return new IndexNotation(token.getType(), null);
		}
		consume(t, TokenType.LBRACKET);
		Expr e = parseExpression(t);
		consume(t, TokenType.RBRACKET);
		IndexNotation i = new IndexNotation(token.getType(), e, null);
		e.setParent(i);
		return i;
	}

	/** Returns the expression inside of the brackets, i.e. mem[expr] */
	public static Expr parseMem(Tokenizer t) throws SyntaxError {
		Token token = t.peek();
		if (token.isMemSugar()) {
			int index;
			switch (token.getType()) {
			case ABV_MEMSIZE:
				index = 0;
				break;
			case ABV_DEFENSE:
				index = 1;
				break;
			case ABV_OFFENSE:
				index = 2;
				break;
			case ABV_SIZE:
				index = 3;
				break;
			case ABV_ENERGY:
				index = 4;
				break;
			case ABV_PASS:
				index = 5;
				break;
			case ABV_POSTURE:
				index = 6;
				break;
			default:
				throw new SyntaxError(
						"Line " + token.lineNumber() + ": Expected MEMSUGAR but got " + token.toString() + " instead");
			}
			consume(t, token.getType());
			return new Number(index, null);
		}

		// Otherwise it has been written explicitly as mem[expr]
		consume(t, TokenType.MEM);
		consume(t, TokenType.LBRACKET);
		Expr e1 = parseExpression(t);
		consume(t, TokenType.RBRACKET);
		return e1;
	}

	/**
	 * Consumes a token of the expected type.
	 *
	 * @throws SyntaxError if the wrong kind of token is encountered.
	 */
	public static void consume(Tokenizer t, TokenType tt) throws SyntaxError {
		if (!t.peek().getType().equals(tt))
			throw new SyntaxError("Line " + t.peek().lineNumber() + ": Token " + t.peek().toString()
					+ " was not of expected type " + tt.toString());
		t.next();
	}

	/**
	 * Reports whether the next token is of the expected type without consuming it
	 * 
	 * @param t  tokenizer to be used
	 * @param tt expected TokenType
	 * @return whether the next token is of type tt
	 * @throws SyntaxError if there is an error in peek()
	 */
	public static boolean willConsume(Tokenizer t, TokenType tt) throws SyntaxError {
		return (t.peek().getType().equals(tt));
	}

}
