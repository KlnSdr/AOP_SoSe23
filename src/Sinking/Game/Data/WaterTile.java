package Sinking.Game.Data;

public class WaterTile extends Tile {
    @Override
    public boolean shoot() {
        this.state = TileState.MISS;
        return false;
    }

    @Override
    public String toString() {
        return "Water";
    }
}
