package model;

/**
 * A collection of the types of tiles possible in the world, with tiles having
 * varying amounts and types of data.
 */
public abstract class HexTile {

    public abstract WorldImpl.Vector getCoord();

    public int type = 0; // Represents the type of the tile so as not to have to use indexof
    // 0 - empty (although an empty tile is just null)
    // 1 - critter
    // 2 - food
    // 3 - rock

    /**
     * Whether this HexTile is of type CritterTile
     *
     * @return is critter
     */
    public boolean isCritter() {
        return type == 1;
    }

    /**
     * Whether this HexTile is of type FoodTile
     *
     * @return is critter
     */
    public boolean isFood() {
        return type == 2;
    }

    /**
     * Whether this HexTile is of type RockTile
     *
     * @return is critter
     */
    public boolean isRock() {
        return type == 3;
    }
}
