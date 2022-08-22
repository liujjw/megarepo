package model;

import java.util.List;

import ast.Action;
import ast.Command;
import ast.Node;
import ast.Program;
import ast.Rule;
import ast.Update;
import parse.TokenType;

/** A class to handle the interpretation of critter rules */
public class Interpreter {
	/**
	 * Interprets the given critter's rules
	 * 
	 * @param crit critter to be interpreted
	 */
	public static void interpret(Critter crit) {
		boolean acted = false;
		int moveN = 1;
		Program p = crit.getProgram();
		List<Node> rules = p.getChildren();

		// Set move number
		crit.setPass(moveN);
		while (!acted && moveN <= Constants.MAX_RULES_PER_TURN) {
			int condCount = 0;
			for (Node r : rules) {
				boolean conditionSatisfied = false;

				r = (Rule) r;

				Node condition = r.getChildren().get(0);
				if (condition.getVal(crit) == 1) {
					conditionSatisfied = true;
				}
				Node command = (Command) r.getChildren().get(1);
				List<Node> steps = command.getChildren();
				if (conditionSatisfied) {
					condCount += 1;
					crit.setLastExecutedRule((Rule) r);
					for (Node n : steps) {
						if (n instanceof Update) {
							List<Node> upDetails = n.getChildren();
							int index = (upDetails.get(0)).getVal(crit);
							int val = (upDetails.get(1)).getVal(crit);
							crit.setMemFromRule(index, val);
						} else {
							// action which will always be last node
							execute(crit, (Action) n);
							acted = true;
						}
					}
					if (acted == true) {
						// if action taken, terminate process
						break;
					} else {
						// Increment and repeat loop if no action taken
						moveN++;
						crit.setPass(moveN);
						break;
					}
				}
			}
			
			if (condCount == 0) {
				// no condition satisfied
				// critters turn immediately ends
				// performs a wait function
				// Because acted must be false this is equivalent to breaking loop
				break;
			}
		}
		if (!acted) {
			crit.waiting();
		}
	}

	/**
	 * Executes the given action on the given critter
	 * 
	 * @param crit critter to performed action
	 * @param n    action to be performed
	 */
	static void execute(Critter crit, Action n) {
		TokenType t = n.getActionType();

		List<Node> AChild = n.getChildren();
		if (AChild.size() != 0) {
			int exprval = (AChild.get(0)).getVal(crit);
			crit.serve(exprval);
		} else {
			switch (t) {
			case WAIT:
				crit.waiting();
				break;
			case FORWARD:
				crit.moveForward();
				break;
			case BACKWARD:
				crit.moveBackwards();
				break;
			case LEFT:
				crit.turnLeft();
				break;
			case RIGHT:
				crit.turnRight();
				break;
			case EAT:
				crit.eat();
				break;
			case ATTACK:
				crit.attack();
				break;
			case GROW:
				crit.grow();
				break;
			case BUD:
				crit.bud();
				break;
			case MATE:
				crit.mate();
				break;
			default:
			}
		}
	}
}
