package model;

/** Represents an empty, inbounds tile in the HexGrid.*/
public class EmptyTile extends HexTile {

    private WorldImpl.Vector coord;

    /**
     * @return coordinate of this tile
     * */
    public WorldImpl.Vector getCoord() {
        return coord;
    }

    /**
     * @param coord specify a world coordinate this tile is placed in
     * */
    public EmptyTile(WorldImpl.Vector coord) {
        this.coord = coord;
    }
}
