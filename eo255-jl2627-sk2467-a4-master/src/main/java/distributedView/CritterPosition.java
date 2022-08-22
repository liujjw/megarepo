package distributedView;

/** A data structure to hold a critter position (row, col, and direction) */
public class CritterPosition {
	public int row;
	public int col;
	public int direction;

	/**
	 * Creates a CritterPosition object with given attributes
	 * 
	 * @param row       row
	 * @param col       col
	 * @param direction direction
	 */
	public CritterPosition(int row, int col, int direction) {
		this.row = row;
		this.col = col;
		this.direction = direction;
	}
}
