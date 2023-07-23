package Sinking.Game.Data;

public abstract class Tile {
    protected TileState state = TileState.UNKNOWN;

    public TileState getState() {
        return state;
    }

    public boolean wasHit() {
        return state == TileState.HIT;
    }

    public abstract boolean shoot();
}
