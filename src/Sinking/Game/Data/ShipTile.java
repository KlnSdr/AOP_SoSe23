package Sinking.Game.Data;

public class ShipTile extends Tile {
    @Override
    public boolean shoot() {
        this.state = TileState.HIT;
        return true;
    }
}
