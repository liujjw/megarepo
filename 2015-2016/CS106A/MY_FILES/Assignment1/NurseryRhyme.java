
/*
 * Solves this nurery rhyme for the amount of people coming from St. Ives
 * As I was going to St. Ives,
I met a man with seven wives, Each wife had seven sacks,
Each sack had seven cats,
Each cat had seven kits:
Kits, cats, sacks, and wives,
How many were going to St. Ives?
*/

import acm.program.*;

public class NurseryRhyme extends ConsoleProgram {
	private static final int MAN = 1;
	private static final int WIVES_OF_MAN = 7;
	private static final int SACKS_OWNED_BY_ONE_WIFE = 7;
	private static final int CATS_IN_A_SACK = 7;
	private static final int KITS_OF_ONE_CAT = 7;
	
	public void run(){
		int assembledParty;
		assembledParty = (MAN * (WIVES_OF_MAN * (SACKS_OWNED_BY_ONE_WIFE * (CATS_IN_A_SACK * KITS_OF_ONE_CAT))));
		println(assembledParty);
	}
}
