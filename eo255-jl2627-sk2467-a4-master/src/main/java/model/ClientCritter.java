package model;

import distributedView.CritterInfo;
import distributedView.TerrainInfo;

/** Represents a critter as needed by the client */
public class ClientCritter extends CritterInfo implements ReadOnlyCritter {

	private String[] rule; // represents a list of all rules

	public ClientCritter(int id, String species_id, String program, int row, int col, int direction, int[] mem,
			Integer recently_executed_rule) {
		super(id, species_id, program, row, col, direction, mem, recently_executed_rule);
	}

	/**
	 * Converts a CritterInfo object to a ClientCritter object
	 * 
	 * @param info CritterInfo to be converted
	 * @return equivalent ClientCritter object
	 */
	public static ClientCritter toClientCritter(CritterInfo info) {
		return new ClientCritter(info.id, info.species_id, info.program, info.row, info.col, info.direction, info.mem,
				info.recently_executed_rule);
	}

	/**
	 * Converts a TerrainInfo object to a ClientCritter object
	 * 
	 * @param info TerrainInfo to be converted
	 * @return equivalent ClientCritter object
	 */
	public static ClientCritter toClientCritter(TerrainInfo info) {
		if (info.isCritter() != 1) {
			return null;
		}
		return new ClientCritter(info.id, info.species_id, info.program, info.row, info.col, info.direction, info.mem,
				info.recently_executed_rule);
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public String getSpecies() {
		return species_id;
	}

	@Override
	public int getDirection() {
		return direction;
	}

	@Override
	public int getMemory(int index) {
		if (index < 0 || index > mem.length) {
			return -1;
		}
		return mem[index];
	}

	@Override
	public int[] getMemory() {
		int[] newMem = new int[mem.length];
		for (int i = 0; i < newMem.length; i++)
			newMem[i] = mem[i];
		return newMem;
	}

	@Override
	public String getProgramString() {
		return program;
	}

	@Override
	public String getLastRuleString() {
		if (program == null || recently_executed_rule == null) {
			return null;
		}
		if (recently_executed_rule == -1)
			return "None";
		int count = 0;
		int index = 0;
		for (int i = 0; i < program.length(); i++) {
			if (program.charAt(i) == ';') {
				if (count == recently_executed_rule) {
					return program.substring(index, i);
				}
				count++;
				index = i + 2;
			}
		}
		return "Could not find rule.";
	}

	@Override
	public int getLastRuleIndex() {
		return recently_executed_rule;
	}

	@Override
	public int getLastSmell() {
		throw new UnsupportedOperationException();
	}

}
