
package view;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import model.ReadOnlyCritter;
import model.ReadOnlyWorld;
import model.WorldImpl.Vector;

public class HexGridRender {
	private final Canvas canvas;
	private final GraphicsContext gc;
	private ReadOnlyWorld world;

	private double h; // hexagon height
	// Side Length of Regular Hexagon
	private double SL;
	// The largest of the other sides
	private double BS;
	// The smaller other side
	private double LS;
	private final double MAX_HEX_HEIGHT;
	private final double MIN_HEX_HEIGHT;
	private final double MIN_FOOD_SIZE = 3;
	private static double ZOOM_SCALE = 1.1;
	private static double MAX_FOOD_DISPLAY = 10;
	private final double paddingX = 40;
	private final double paddingY = 30;
	private final String backgroundColor = "#F5F3F5";
	private Vector selected = null; // represents selected hex
	private Vector lastSelected = null; // represents selected hex
	private Integer[][] toReDraw;

	private int zoom = -1;

	private Integer[][] lastDraw; // represents the last state of the world upon drawing, so it knows when to
									// clear and when not to.
	// KEY for lastDraw:
	// 0 if empty
	// -1 if rock
	// -(X-1) for X food
	// d+1 for critter direction d

	/** Receives a canvas and a world to draw with. */
	public HexGridRender(ReadOnlyWorld world, Canvas canvas) {
		lastDraw = new Integer[world.numRows()][world.numCols()];
		toReDraw = new Integer[world.numRows()][world.numCols()];
		this.canvas = canvas;
		this.world = world;
		this.gc = canvas.getGraphicsContext2D();
		if (world.getHeight() < world.getWidth()) {
			h = (canvas.getHeight() - 2 * paddingY) / world.getHeight() * 2;
			assignWidth();
		} else {
			h = (canvas.getWidth() - 2 * paddingX) / world.getWidth();
			assignHeight();
		}
		MAX_HEX_HEIGHT = h * Math.pow(1.1, 15);
		MIN_HEX_HEIGHT = h / Math.pow(1.1, 15);
		SL = h / Math.sqrt(3);
		BS = h / 2;
		LS = h / (2 * Math.sqrt(3));
	}

	/**
	 * Returns whether the given coordinate (x,y) is in bounds of the world
	 * 
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return whether (x,y) is on the world
	 */
	public boolean inBounds(double x, double y) {
		return ((x - paddingX) / h < world.getWidth() && (y - paddingY) / h < world.getHeight());
	}

	/**
	 * Draws critters, rocks, hexes, food onto a canvas. WARNING: Only call draw
	 * from an application thread, not the current thread.
	 *
	 * @param world               the world to draw
	 * @param redrawHexesAndRocks whether to redraw hexes and rocks; false if
	 *                            already drawn
	 * @param tl                  top left coordinate of view frame
	 * @param br                  bottom right coordinate of view frame
	 * @return a Map<Coordinate, Vector> object of hex tile centers to world
	 *         coordinates.
	 */
	public Map<Coordinate, Vector> draw(ReadOnlyWorld world, boolean redrawHexesAndRocks, Coordinate tl,
			Coordinate br) {
		// clear before drawing
		this.world = world;
		try {
			if (redrawHexesAndRocks || zoom != 0)
				canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

			tl = new Coordinate(paddingX + LS, paddingY);
			return drawAllHexes(tl, br, redrawHexesAndRocks);
		} catch (Exception e) {
			System.out.println(e.getCause());
		}
		return null;
	}

	/**
	 * Zooms in the world, does NOT redraw canvas.
	 * 
	 * @return whether it will be possible to zoom in again, i.e. max hex height
	 *         won't be reached next time
	 */
	public boolean zoomIn() {
		zoom++;
		double newH = h * ZOOM_SCALE;
		boolean returning = true;
		if (newH * ZOOM_SCALE > MAX_HEX_HEIGHT)
			returning = false;
		h = newH;
		assignWidth();
		assignHeight();
		SL = h / Math.sqrt(3);
		BS = h / 2;
		LS = h / (2 * Math.sqrt(3));
		return returning;
	}

	/**
	 * Zooms out of the world, does NOT redraw canvas.
	 * 
	 * @return whether it will be possible to zoom out again, i.e. min hex height
	 *         won't be reached next time
	 */
	public boolean zoomOut() {
		zoom--;
		double newH = h / ZOOM_SCALE;
		boolean returning = true;
		if (newH / ZOOM_SCALE < MIN_HEX_HEIGHT)
			returning = false;
		h = newH;
		assignWidth();
		assignHeight();
		SL = h / Math.sqrt(3);
		BS = h / 2;
		LS = h / (2 * Math.sqrt(3));
		return returning;
	}

	/**
	 * Resizes canvas using a tried and tested formula for the ideal width of the
	 * canvas based on hexagon height
	 */
	private void assignWidth() {
		canvas.setWidth(2 * paddingX + world.getWidth() * h + h);
	}

	/**
	 * Resizes canvas using a tried and tested formula for the ideal height of the
	 * canvas based on hexagon height
	 */
	private void assignHeight() {
		canvas.setHeight(2 * paddingY + world.getHeight() / 2 * h + h);
	}

	/**
	 * Draws either everything on the board or only food and critters, depending on
	 * drawHexesAndRocks
	 * 
	 * @param tl                top left coordinate of view frame
	 * @param br                bottom right coordinate of view frame
	 * @param drawHexesAndRocks whether to redraw hexes and rocks or only critters
	 *                          and food
	 * @return a Map<Coordinate, Vector> object of hex tile centers to world
	 *         coordinates.
	 */
	public Map<Coordinate, Vector> drawAllHexes(Coordinate tl, Coordinate br, boolean drawHexesAndRocks) {
		int cols = world.numCols();
		int rows = world.numRows();
		double tlx = tl.x;
		double tly = tl.y;
		Map<Coordinate, Vector> coordMap = new HashMap<Coordinate, Vector>();

		double maxY = canvas.getHeight() - h;// + (rows - 1 - ((0 + 1) / 2)) * h;

		// Iterate through rows:
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (world.inBounds(new Vector(c, r))) {

					// Draw the Hex
					double xshift = (3 * h) / (2 * Math.sqrt(3));

					double x = tlx + c * xshift;
					double y = tly + (r - ((c + 1) / 2)) * h;
					if (c % 2 == 1) {
						y = h / 2 + y;
					}
//					 If odd column, push up by half height

					y = maxY - y;

					Coordinate coord = new Coordinate(x + SL / 2, y + BS);
					Vector vect = new Vector(c, r);
					coordMap.put(coord, vect);

					int val = world.getCritterDirection(c, r) + 1;// != -1
					if (val == 0) {
						val = world.getTerrainInfo(c, r);
					} else { // mem[3] = SIZE
						val += 10 * world.getReadOnlyCritter(c, r).getMemory(3); // so that size and direction are
																					// represented, the only graphical
																					// components of the critter
					}
					boolean thisOneSelected = selected != null && r == selected.row && c == selected.col;
					boolean thisOneSelectedLast = false;
					if (!thisOneSelected)
						thisOneSelectedLast = lastSelected != null && r == lastSelected.row && c == lastSelected.col;

					if (toReDraw[r][c] == null && zoom == 0 && lastDraw[r][c] != null && val == lastDraw[r][c]
							&& !thisOneSelected && !thisOneSelectedLast) {
						continue;
					}
					lastDraw[r][c] = val;
					if (!thisOneSelected || thisOneSelectedLast) {
						// Equivalent of clearing the hex
						drawRock(x, y, backgroundColor, true);
					} else {
						if (toReDraw[r][c] != null) {
							drawRock(x, y, backgroundColor, true);
							drawRock(x, y, "#ddccff55", true);
						}
						toReDraw[r][c] = null;
					}

//					if (x < tlx - LS - SL || x > br.x)
//						continue;
//					if (y < tly - h || y > br.y)
//						continue;

					if (val > 0) {
						ReadOnlyCritter crit = world.getReadOnlyCritter(c, r);
						if (crit == null)
							continue; // critter could have died in the span of a time step while we are updating the
										// render

						drawCritter(x, y, crit);
					} else {
						if (val == -1) {
							drawRock(x, y, "gray", false);
						} else if (val < 0) {
							int amt = -1 * (val) - 1;
							drawFood(x, y, amt);
						}

					}

				}
			}
			// center x = x + SL/2
			// center y = y + h
		}
		zoom = 0;
		return coordMap;

	}

	/**
	 * Sets the hex at vector v to selected style containint critter critter
	 * 
	 * @param v       vector location
	 * @param critter ReadOnlyCritter that was there
	 */
	public void setSelectedHexCritter(Vector v, ReadOnlyCritter critter) {
		if (critter == null)
			return;
		lastSelected = selected;
		selected = v;
		if (selected != null)
			toReDraw[selected.row][selected.col] = 1;
		if (lastSelected != null)
			toReDraw[lastSelected.row][lastSelected.col] = 1;
	}

	/**
	 * @return selected vector
	 */
	public Vector getSelected() {
		return selected;
	}

	/**
	 * Gets the coordinates in the grid corresponding to the given vector v
	 * 
	 * @param v vector to convert to coordinates
	 * @return coordinate equivalent of vector
	 */
	private Coordinate getCoords(Vector v) {
		Coordinate tl = new Coordinate(paddingX + LS, paddingY);
		int row = v.row;
		int col = v.col;
		double xshift = (3 * h) / (2 * Math.sqrt(3));
		double maxY = canvas.getHeight() - h;// + (rows - 1 - ((0 + 1) / 2)) * h;

		double x = tl.x + col * xshift;
		double y = tl.y + (row - ((col + 1) / 2)) * h;
		if (col % 2 == 1) {
			y = h / 2 + y;
		}
//		 If odd column, push up by half height

		y = maxY - y;
		return new Coordinate(x, y);
	}

	/**
	 * Deselects all tiles and redraws given tile
	 * 
	 * @param v vector to deselect, can be null
	 */
	public void setNothingSelected(Vector v) {
		lastSelected = selected;
		selected = null;
		if (lastSelected != null)
			toReDraw[lastSelected.row][lastSelected.col] = 1;
//		if (v != null) {
//			lastDraw[v.row][v.col] = null;
//		}
	}

	/**
	 * Sets the hex at v to selected style, with the amount of food drawn on top of
	 * it
	 * 
	 * @param v      vector to draw selected style
	 * @param amount amount of food
	 */
	public void setSelectedHexFood(Vector v, int amount) {
		lastSelected = selected;
		selected = v;
		if (selected != null)
			toReDraw[selected.row][selected.col] = 1;
		if (lastSelected != null)
			toReDraw[lastSelected.row][lastSelected.col] = 1;
	}

	/**
	 * Draws a critter with a top left corner at (x,y)
	 * 
	 * @param x    top left x coordinate
	 * @param y    top left y coordinate
	 * @param crit Critter to be drawn
	 */
	public void drawCritter(double x, double y, ReadOnlyCritter crit) {

		// between zero and 5
		int direction = crit.getDirection();
		int MouthSize = 20;
		int Sangle = 90 + MouthSize;
		int Eangle = 360 - 2 * MouthSize;
		Sangle -= 60 * (direction);

		try {
			gc.setFill(Paint.valueOf(IntToRGBA(crit.getSpecies().hashCode())));
		} catch (IllegalArgumentException e) {
			gc.setFill(Paint.valueOf("yellow"));
		}

		double d = tanScale(crit.getMemory(3), 5, h);

		gc.fillArc(x + SL / 2 - d / 2, y + BS - d / 2, d, d, Sangle, Eangle, ArcType.ROUND);
		gc.setFill(Paint.valueOf("black"));

		gc.strokeArc(x + SL / 2 - d / 2, y + BS - d / 2, d, d, Sangle, Eangle, ArcType.ROUND);

	}

	/**
	 * Draws a rock with a top left corner at (x,y)
	 * 
	 * @param x top left x coordinate
	 * @param y top left y coordinate
	 */
	public void drawRock(double x, double y, String color, boolean lines) {
		// make the hex grey
		gc.setFill(Paint.valueOf(color));

		double[] xVals = new double[] { x, x + SL, x + SL + LS, x + SL, x, x - LS };
		double[] yVals = new double[] { y, y, y + BS, y + h, y + h, y + BS };
		gc.fillPolygon(xVals, yVals, 6);
		if (lines) {
			gc.strokeLine(x, y, x + SL, y);
			gc.strokeLine(x + SL, y, x + SL + LS, y + BS);

			gc.strokeLine(x + SL + LS, y + BS, x + SL, y + h);
			gc.strokeLine(x + SL, y + h, x, y + h);
			gc.strokeLine(x, y + h, x - LS, y + BS);
			gc.strokeLine(x - LS, y + BS, x, y);
		}
	}

	/**
	 * Draws food with a top left corner at (x,y)
	 * 
	 * @param x top left x coordinate
	 * @param y top left y coordinate
	 */
	public void drawFood(double x, double y, double foodNum) {

		// Draw Circle for Food based on size of food

		if (foodNum > MAX_FOOD_DISPLAY) {
			MAX_FOOD_DISPLAY = foodNum;
		}
		double d = tanScale(foodNum, 0.75 * MAX_FOOD_DISPLAY, h);
		double xc = x + SL / 2;
		double yc = y + h / 2;
		gc.setFill(Paint.valueOf("green"));
		gc.fillOval(xc - d / 2, yc - d / 2, d, d);
	}

	/**
	 * Returns a double d such that 0 < d < max and d is logarithmically scaled,
	 * i.e. passing in higher and higher nums won't change d as much
	 * 
	 * @param num     num to be scaled
	 * @param halfway value of num that should give d = max/2
	 * @param max     Max value of num
	 * @return logarithmically scaled double
	 */
	private double tanScale(double num, double halfway, double max) {
		return (Math.atan(num / halfway) / (Math.PI / 2) * (max - MIN_FOOD_SIZE) + MIN_FOOD_SIZE);
	}

	/**
	 * Converts an integer to an RGBA string using bit shifts
	 * 
	 * @param i integer to convert
	 * @return hex string of color
	 */
	private static String IntToRGBA(int i) {
		StringBuffer sb = new StringBuffer();
		sb.append(Integer.toHexString((i & 0xFF)));
		sb.append(Integer.toHexString(((i >> 8) & 0xFF)));
		sb.append(Integer.toHexString(((i >> 16) & 0xFF)));
		sb.append(Integer.toHexString(((i >> 24) & 0xFF)));
		String s = sb.toString();
		int diff = 8 - s.length();
		for (int j = 0; j < diff; j++)
			s = "0" + s;
		s = s.substring(0, 8);
		return s;
	}

	/** Represents a 2d coordinate of doubles */
	public static class Coordinate {
		public double x;
		public double y;

		/**
		 * Constructs a coordinate object with the given coordinates
		 * 
		 * @param x x coordinate
		 * @param y y coordinate
		 */
		public Coordinate(double x, double y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * Represents a collection of two maps, one from Coordinate to Vectors, and the
		 * other from Vectors to ReadOnlyCritters
		 */
		public static class TwoMap {
			public Map<Coordinate, Vector> map1;
			public Map<Vector, ReadOnlyCritter> map2;

			/**
			 * Constructs a twomap object holding both maps in public fields
			 * 
			 * @param m1 first map
			 * @param m2 second map
			 */
			public TwoMap(Map<Coordinate, Vector> m1, Map<Vector, ReadOnlyCritter> m2) {
				map1 = m1;
				map2 = m2;
			}
		}
	}

}
